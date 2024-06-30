package com.gillab.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HolidayService Tests")
class HolidayServiceTest {

    private final HolidayService holidayService = HolidayService.getInstance();

    @Nested
    @DisplayName("Is holiday")
    class IsHolidayTests {

        @Test
        @DisplayName("Should return false - July 4th 2021 was a Sunday")
        void testIsHoliday_IndependenceDayOnSunday() {
            LocalDate date = LocalDate.of(2021, Month.JULY, 4);
            boolean isHoliday = holidayService.isHoliday(date);
            assertFalse(isHoliday, "July 4th 2021 should not be a holiday");
        }

        @Test
        @DisplayName("Should return true - July 5th 2021 was the Monday after independence day")
        void testIsHoliday_IndependenceDayOnMondayAfter() {
            LocalDate date = LocalDate.of(2021, Month.JULY, 5);
            boolean isHoliday = holidayService.isHoliday(date);
            assertTrue(isHoliday, "July 5th 2021 should be a holiday");
        }

        @Test
        @DisplayName("Should return true - July 3rd 2020 was the Friday before independence day")
        void testIsHoliday_IndependenceDayOnFridayBefore() {
            LocalDate date = LocalDate.of(2020, Month.JULY, 3);
            boolean isHoliday = holidayService.isHoliday(date);
            assertTrue(isHoliday, "July 3rd 2020 should be a holiday");
        }

        @Test
        @DisplayName("Should return true - 2021 Labor Day was on September 6th")
        void testIsHoliday_LaborDay() {
            LocalDate date = LocalDate.of(2021, Month.SEPTEMBER, 6);
            boolean isHoliday = holidayService.isHoliday(date);
            assertTrue(isHoliday, "September 6th 2021 should be a holiday");
        }

        @Test
        @DisplayName("Should return false for non-holiday date")
        void testIsHoliday_NonHoliday() {
            LocalDate date = LocalDate.of(2023, Month.JULY, 23);
            boolean isHoliday = holidayService.isHoliday(date);
            assertFalse(isHoliday, "July 23rd is not a Holiday, but it should be (is my birthday).");
        }

        @Test
        @DisplayName("Should throw NullPointerException when date is null")
        void testIsHoliday_nullDate() {
            assertThrows(NullPointerException.class,
                    () -> holidayService.isHoliday(null),
                    "Expected isHoliday to throw NullPointerException for null date"
            );
        }
    }

}
