package com.gillab.service;

import com.gillab.exception.ApplicationException;
import com.gillab.model.RentalAgreement;
import com.gillab.model.ToolTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static com.gillab.TestConstants.*;
import static com.gillab.exception.ApplicationErrors.ErrorCodesEnum.INVALID_DISCOUNT;
import static com.gillab.exception.ApplicationErrors.ErrorCodesEnum.INVALID_RENTAL_DAYS;
import static com.gillab.exception.ApplicationErrors.getErrorSummary;
import static com.gillab.exception.ApplicationErrors.getFormattedErrorMessage;
import static com.gillab.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CheckoutService Tests")
class CheckoutServiceTest {

    @Mock
    private ToolService toolService;
    @Mock
    private HolidayService holidayService;

    @InjectMocks
    private CheckoutService checkoutService;

    @Nested
    @DisplayName("Validate rental days")
    class ValidateRentalDaysTests {

        @ParameterizedTest
        @ValueSource(ints = {-1, 0, 366})
        @DisplayName("Should throw exception for invalid rental days")
        void testValidateRentalDays_Invalid(int rentalDays) {
            ApplicationException exception = assertThrows(
                    ApplicationException.class,
                    () -> checkoutService.validateRentalDays(rentalDays, CORRELATION_ID),
                    "Expected validateRentalDays to throw, but it didn't"
            );

            assertAll(
                    ()->{
                        assertEquals(INVALID_RENTAL_DAYS.getDisplayName(), exception.getErrorCode(), "Error code should match");
                        assertEquals(CORRELATION_ID, exception.getCorrelationId(), "Error summary should match");
                        assertEquals(getErrorSummary(INVALID_RENTAL_DAYS), exception.getErrorSummary(), "Error summary should match");
                        assertEquals(getFormattedErrorMessage(INVALID_RENTAL_DAYS, String.valueOf(rentalDays), String.valueOf(MIN_DISCOUNT_PERCENTAGE), String.valueOf(MAX_DISCOUNT_PERCENTAGE)), exception.getErrorMessage(), "Error message should match");
                    }
            );

            verifyNoInteractions(toolService);
            verifyNoInteractions(holidayService);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 365})
        @DisplayName("Should not throw exception for valid rental days")
        void testValidateRentalDays_Valid(int rentalDays) {
            assertDoesNotThrow(() -> {
                checkoutService.validateRentalDays(rentalDays, CORRELATION_ID);
            }, "Should not throw ApplicationException for valid rental days");

            verifyNoInteractions(toolService);
            verifyNoInteractions(holidayService);
        }

        @Test
        @DisplayName("Should throw NullPointerException when rental days is null")
        void testValidateRentalDays_nullToolCode() {
            assertThrows(NullPointerException.class,
                    () -> checkoutService.validateRentalDays(null, CORRELATION_ID),
                    "Expected validateRentalDays to throw NullPointerException for null rental days"
            );

            verifyNoInteractions(toolService);
            verifyNoInteractions(holidayService);
        }

        @Test
        @DisplayName("Should throw NullPointerException when correlation ID is null")
        void testValidateRentalDays_nullCorrelationId() {
            assertThrows(NullPointerException.class,
                    () -> checkoutService.validateRentalDays(MAX_RENTAL_DAYS, null),
                    "Expected validateRentalDays to throw NullPointerException for null correlation ID"
            );

            verifyNoInteractions(toolService);
            verifyNoInteractions(holidayService);
        }
    }

    @Nested
    @DisplayName("Validate discount percentage")
    class ValidateDiscountPercentageTests {

        @ParameterizedTest
        @ValueSource(ints = {-1, 101})
        @DisplayName("Should throw exception for invalid discount percentage")
        void testValidateDiscountPercentage_Invalid(int discount) {
           ApplicationException exception = assertThrows(
                    ApplicationException.class,
                    () -> checkoutService.validateDiscountPercentage(discount, CORRELATION_ID),
                    "Expected validateDiscountPercentage to throw, but it didn't"
            );

            assertAll(
                    ()->{
                        assertEquals(INVALID_DISCOUNT.getDisplayName(), exception.getErrorCode(), "Error code should match");
                        assertEquals(CORRELATION_ID, exception.getCorrelationId(), "Error summary should match");
                        assertEquals(getErrorSummary(INVALID_DISCOUNT), exception.getErrorSummary(), "Error summary should match");
                        assertEquals(getFormattedErrorMessage(INVALID_DISCOUNT, String.valueOf(discount), String.valueOf(MIN_DISCOUNT_PERCENTAGE), String.valueOf(MAX_DISCOUNT_PERCENTAGE)), exception.getErrorMessage(), "Error message should match");
                    }
            );

            verifyNoInteractions(toolService);
            verifyNoInteractions(holidayService);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 100})
        @DisplayName("Should not throw exception for valid discount percentage")
        void testValidateDiscountPercentage_Valid(int discount) {
            assertDoesNotThrow(() -> {
                checkoutService.validateDiscountPercentage(discount, CORRELATION_ID);
            }, "Should not throw ApplicationException for valid discount percentage");

            verifyNoInteractions(toolService);
            verifyNoInteractions(holidayService);
        }

        @Test
        @DisplayName("Should throw NullPointerException when discount percentage is null")
        void testValidateDiscountPercentage_nullToolCode() {
            assertThrows(NullPointerException.class,
                    () -> checkoutService.validateDiscountPercentage(null, CORRELATION_ID),
                    "Expected validateDiscountPercentage to throw NullPointerException for null discount percentage"
            );

            verifyNoInteractions(toolService);
            verifyNoInteractions(holidayService);
        }

        @Test
        @DisplayName("Should throw NullPointerException when correlation ID is null")
        void testValidateDiscountPercentage_nullCorrelationId() {
            assertThrows(NullPointerException.class,
                    () -> checkoutService.validateDiscountPercentage(MAX_RENTAL_DAYS, null),
                    "Expected validateDiscountPercentage to throw NullPointerException for null correlation ID"
            );

            verifyNoInteractions(toolService);
            verifyNoInteractions(holidayService);
        }
    }

    @Nested
    @DisplayName("Is chargeable day")
    class IsChargeableDayTests {

        @Test
        @DisplayName("Should throw NullPointerException when current date is null")
        void testIsChargeableDay_nullCorrelationId() {
            assertThrows(NullPointerException.class,
                    () -> checkoutService.isChargeableDay(null,true, true, true),
                    "Expected isChargeableDay to throw NullPointerException for null current date"
            );

            verifyNoInteractions(toolService);
            verifyNoInteractions(holidayService);
        }

        @Test
        @DisplayName("Should return true for holiday chargeable day")
        void testIsChargeableDay_HolidayChargeable() {
            LocalDate date = LocalDate.of(2024, Month.JULY, 23);
            when(holidayService.isHoliday(date)).thenReturn(true);

            assertTrue(checkoutService.isChargeableDay(date, true, false, false), "July 23rd should be chargeable as a holiday");

            verify(holidayService).isHoliday(date);
            verifyNoInteractions(toolService);
        }

        @Test
        @DisplayName("Should return false for holiday chargeable day")
        void testIsChargeableDay_HolidayNonChargeable() {
            LocalDate date = LocalDate.of(2024, Month.JULY, 23);
            when(holidayService.isHoliday(date)).thenReturn(true);

            assertFalse(checkoutService.isChargeableDay(date, false, false, false), "July 23rd should be chargeable as a holiday");

            verify(holidayService).isHoliday(date);
            verifyNoInteractions(toolService);
        }

        @Test
        @DisplayName("Should return true for weekday chargeable day")
        void testIsChargeableDay_WeekdayChargeable() {
            LocalDate weekdayDate = LocalDate.of(2024, Month.JULY, 23);

            when(holidayService.isHoliday(weekdayDate)).thenReturn(false);

            boolean isChargeable = checkoutService.isChargeableDay(weekdayDate, false, true, false);

            assertTrue(isChargeable, "July 23rd 2024 should be chargeable as a weekday");

            verify(holidayService).isHoliday(weekdayDate);
            verifyNoInteractions(toolService);
        }

        @Test
        @DisplayName("Should return false for weekday non chargeable day")
        void testIsChargeableDay_WeekdayNonChargeable() {
            LocalDate weekdayDate = LocalDate.of(2024, Month.JULY, 23);

            when(holidayService.isHoliday(weekdayDate)).thenReturn(false);

            boolean isChargeable = checkoutService.isChargeableDay(weekdayDate, false, false, false);

            assertFalse(isChargeable, "July 23rd 2024 should not be chargeable as a weekday");

            verify(holidayService).isHoliday(weekdayDate);
            verifyNoInteractions(toolService);
        }

        @Test
        @DisplayName("Should return true for weekend chargeable day")
        void testIsChargeableDay_WeekendChargeable() {
            LocalDate weekendDate = LocalDate.of(2024, Month.JULY, 20);

            when(holidayService.isHoliday(weekendDate)).thenReturn(false);

            boolean isChargeable = checkoutService.isChargeableDay(weekendDate, false, false, true);

            assertTrue(isChargeable, "July 20th should be chargeable as a weekend day");

            verify(holidayService).isHoliday(weekendDate);
            verifyNoInteractions(toolService);
        }

        @Test
        @DisplayName("Should return false for weekend non chargeable day")
        void testIsChargeableDay_WeekendNonChargeable() {
            LocalDate weekendDate = LocalDate.of(2024, Month.JULY, 20);

            when(holidayService.isHoliday(weekendDate)).thenReturn(false);

            boolean isChargeable = checkoutService.isChargeableDay(weekendDate, false, false, false);

            assertFalse(isChargeable, "July 20th should not be chargeable as a weekend day");

            verify(holidayService).isHoliday(weekendDate);
            verifyNoInteractions(toolService);
        }

    }

    @Nested
    @DisplayName("Calculate chargeable days")
    class CalculateChargeableDaysTests {

        @Test
        @DisplayName("Should correctly calculate chargeable days with weekday charges")
        void testCalculateChargeableDays_WeekdayCharges() {
            LocalDate checkoutDate = LocalDate.of(2024, Month.JULY, 1);
            LocalDate dueDate = LocalDate.of(2024, Month.JULY, 7);

            when(holidayService.isHoliday(any())).thenReturn(false);

            int chargeableDays = checkoutService.calculateChargeableDays(checkoutDate, dueDate, true, false, false);

            assertEquals(5, chargeableDays, "Chargeable days should be 5 (only weekdays are chargeable)");

            verifyNoInteractions(toolService);
            verify(holidayService, times(7)).isHoliday(any());
        }

        @Test
        @DisplayName("Should correctly calculate chargeable days with weekend charges")
        void testCalculateChargeableDays_WeekendCharges() {
            LocalDate checkoutDate = LocalDate.of(2024, Month.JULY, 1);
            LocalDate dueDate = LocalDate.of(2024, Month.JULY, 7);

            when(holidayService.isHoliday(any())).thenReturn(false);

            int chargeableDays = checkoutService.calculateChargeableDays(checkoutDate, dueDate, false, true, false);

            assertEquals(2, chargeableDays, "Chargeable days should be 2 (only weekends are chargeable)");

            verifyNoInteractions(toolService);
            verify(holidayService, times(7)).isHoliday(any());
        }

        @Test
        @DisplayName("Should correctly calculate chargeable days with holiday charges")
        void testCalculateChargeableDays_HolidayCharges() {
            LocalDate checkoutDate = LocalDate.of(2024, Month.JULY, 3);
            LocalDate independenceDay = LocalDate.of(2024, Month.JULY, 4);

            when(holidayService.isHoliday(checkoutDate)).thenReturn(false);
            when(holidayService.isHoliday(independenceDay)).thenReturn(true);

            int chargeableDays = checkoutService.calculateChargeableDays(checkoutDate, independenceDay, false, false, true);

            assertEquals(1, chargeableDays, "Chargeable days should be 1 (only holiday is chargeable)");

            verifyNoInteractions(toolService);
            verify(holidayService).isHoliday(checkoutDate);
            verify(holidayService).isHoliday(independenceDay);
        }

    }


}
