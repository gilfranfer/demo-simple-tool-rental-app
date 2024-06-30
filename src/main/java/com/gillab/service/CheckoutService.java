package com.gillab.service;

import com.gillab.model.RentalAgreement;
import com.gillab.model.Tool;
import lombok.NonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

import static com.gillab.exception.ApplicationErrors.ErrorCodesEnum.INVALID_DISCOUNT;
import static com.gillab.exception.ApplicationErrors.ErrorCodesEnum.INVALID_RENTAL_DAYS;
import static com.gillab.exception.ExceptionBuilder.buildBusinessApplicationException;
import static com.gillab.util.Constants.*;
import static java.util.Objects.isNull;

public class CheckoutService {

    private static CheckoutService INSTANCE;

    private final ToolService toolService;
    private final HolidayService holidayService;

    private CheckoutService(ToolService toolService, HolidayService holidayService) {
        this.toolService = toolService;
        this.holidayService = holidayService;
    }

    public static CheckoutService getInstance(@NonNull final ToolService toolService, @NonNull final HolidayService holidayService) {
        if(isNull(INSTANCE)){
            INSTANCE = new CheckoutService(toolService, holidayService);
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
     * Assumption: We are charging for the checkout date, and not charging for the due date. For example:
     * If the tool was checkout out on Friday, June 28th, for 1 day, then:
     * - Checkout date is 06/28/2024
     * - Due date is 06/29/2024
     * - We are charging only for 1 day, that is the Friday, June 28th (assuming the tool has a charge on weekday and 06/28 is not a holiday).
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
        LocalDate currentDate = checkoutDate;

        while (currentDate.isBefore(dueDate)) {
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
        boolean isHoliday = holidayService.isHoliday(currentDate);
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
    public void validateRentalDays(@NonNull final Integer rentalDays, @NonNull final UUID correlationId) {
        if(!IS_VALID_RENTAL_DAYS.test(rentalDays)) {
            throw buildBusinessApplicationException(correlationId, INVALID_RENTAL_DAYS, String.valueOf(rentalDays), String.valueOf(MIN_RENTAL_DAYS), String.valueOf(MAX_RENTAL_DAYS));
        }
    }

    /**
     * Validate that a given discount percentage is valid (within the established range).
     * @param discountPercentage the percentage to validate.
     * @param correlationId ID to track the request.
     */
    public void validateDiscountPercentage(@NonNull final Integer discountPercentage, @NonNull final UUID correlationId) {
        if(!IS_VALID_DISCOUNT.test(discountPercentage)) {
            throw buildBusinessApplicationException(correlationId, INVALID_DISCOUNT, String.valueOf(discountPercentage), String.valueOf(MIN_DISCOUNT_PERCENTAGE), String.valueOf(MAX_DISCOUNT_PERCENTAGE));
        }
    }

}
