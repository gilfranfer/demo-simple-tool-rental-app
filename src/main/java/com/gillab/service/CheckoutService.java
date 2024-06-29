package com.gillab.service;

import com.gillab.model.RentalAgreement;
import com.gillab.model.Tool;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

import static com.gillab.exception.ApplicationErrors.ErrorCodesEnum.INVALID_DISCOUNT;
import static com.gillab.exception.ExceptionBuilder.buildBusinessApplicationException;
import static com.gillab.util.Constants.*;
import static java.util.Objects.isNull;

@Slf4j
public class CheckoutService {

    private static CheckoutService INSTANCE;

    private final ToolService toolService;

    private CheckoutService(ToolService toolService) {
        this.toolService = toolService;
    }

    public static CheckoutService getInstance(@NonNull final ToolService toolService) {
        if(isNull(INSTANCE)){
            INSTANCE = new CheckoutService(toolService);
        }
        return INSTANCE;
    }

    public RentalAgreement checkout(
            final String toolCode,
            final int rentalDays,
            final int discountPercentage,
            @NonNull final LocalDate checkoutDate,
            @NonNull final UUID correlationId
    ) {
        validateRentalDays(rentalDays, correlationId);
        validateDiscountPercentage(discountPercentage, correlationId);
        Tool tool = toolService.getToolByCode(toolCode, correlationId);

        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        int chargeDays = calculateChargeableDays(checkoutDate, dueDate,
                toolService.isToolTypeWeekdayChargeable(tool.getType()),
                toolService.isToolTypeWeekendChargeable(tool.getType()),
                toolService.isToolTypeHolidayChargeable(tool.getType()));
        double dailyRentalCharge = toolService.getDailyRentalChargeByToolType(tool.getType());
        double preDiscountCharge = chargeDays * dailyRentalCharge;
        double discountAmount = preDiscountCharge * (discountPercentage / 100.0);
        double finalCharge = preDiscountCharge - discountAmount;

        return new RentalAgreement(
                tool.getCode(),
                tool.getType().getDisplayName(),
                tool.getBrand(),
                rentalDays,
                checkoutDate,
                dueDate,
                dailyRentalCharge,
                chargeDays,
                preDiscountCharge,
                discountPercentage,
                discountAmount,
                finalCharge
        );
    }

    /**
     * Calculate the number of days subject to rental charge, based on the checkout date and due date.
     *
     * @param checkoutDate the date when the tool was rented.
     * @param dueDate  the date when the tool will be returned.
     * @param hasHolidayCharge whether the tool has a rental charge on holiday or not.
     * @param hasWeekdayCharge whether the tool has a rental charge on a non-holiday weekday or not.
     * @param hasWeekendCharge whether the tool has a rental charge on a non-holiday weekend or not.
     * @return number of days subject to rental charge
     */
    public int calculateChargeableDays(
            @NonNull final LocalDate checkoutDate,
            @NonNull final LocalDate dueDate,
            final boolean hasWeekdayCharge,
            final boolean hasWeekendCharge,
            final boolean hasHolidayCharge
    ) {
        int chargeableDays = 0;
        LocalDate currentDate = checkoutDate.plusDays(1);

        while (!currentDate.isAfter(dueDate)) {
            chargeableDays = ( isChargeableDay(currentDate, hasHolidayCharge, hasWeekdayCharge, hasWeekendCharge) ) ? chargeableDays+1:chargeableDays;
            currentDate = currentDate.plusDays(1);
        }
        return chargeableDays;
    }

    /**
     * Determine if the current date is a chargeable day or not.
     * Assumption: There is a table, in the requirements document, with a column indicating whether the tool has a
     * charge on weekdays or not. There is another column for holiday charge. Given that the two valid holidays always
     * occur on a weekday, I am assuming the value in "Holiday charge" has precedence over the one in "Weekday charge".
     * @param currentDate the date to evaluate.
     * @param hasHolidayCharge whether the tool has a rental charge on holiday or not.
     * @param hasWeekdayCharge whether the tool has a rental charge on a non-holiday weekday or not.
     * @param hasWeekendCharge whether the tool has a rental charge on a non-holiday weekend or not.
     * @return a boolean value
     */
    public boolean isChargeableDay(
            @NonNull final LocalDate currentDate,
            final boolean hasHolidayCharge,
            final boolean hasWeekdayCharge,
            final boolean hasWeekendCharge
    ) {
        boolean isHoliday = false;
        boolean isWeekend = currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY;
        return (isHoliday && hasHolidayCharge) ||
                (!isHoliday && isWeekend && hasWeekendCharge) ||
                (!isHoliday && !isWeekend && hasWeekdayCharge);
    }


    /**
     * Validate that a given number of rental days is valid (within the established range).
     * @param rentalDays the number of days to validate.
     * @param correlationId ID to track the request.
     */
    public void validateRentalDays(final Integer rentalDays, final UUID correlationId) {
        if(!IS_VALID_RENTAL_DAYS.test(rentalDays)) {
            throw buildBusinessApplicationException(correlationId, INVALID_DISCOUNT, String.valueOf(MIN_DISCOUNT_PERCENTAGE), String.valueOf(MAX_DISCOUNT_PERCENTAGE));
        }
    }

    /**
     * Validate that a given discount percentage is valid (within the established range).
     * @param discountPercentage the percentage to validate.
     * @param correlationId ID to track the request.
     */
    public void validateDiscountPercentage(final Integer discountPercentage, final UUID correlationId) {
        if(!IS_VALID_DISCOUNT.test(discountPercentage)) {
            throw buildBusinessApplicationException(correlationId, INVALID_DISCOUNT, String.valueOf(MIN_DISCOUNT_PERCENTAGE), String.valueOf(MAX_DISCOUNT_PERCENTAGE));
        }
    }

}
