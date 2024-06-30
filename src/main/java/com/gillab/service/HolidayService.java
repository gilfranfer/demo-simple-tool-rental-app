package com.gillab.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HolidayService {

    private static final HolidayService INSTANCE = new HolidayService();
    private static final Map<Integer, Set<LocalDate>> holidaysPerYear = new HashMap<>();

    public static HolidayService getInstance() {
        return INSTANCE;
    }

    /**
     * Determine if the given date is a holiday, considering that there are only two (2) holidays in the calendar:
     * 1. Independence Day, July 4th - If falls on weekend, it is observed on the closest weekday (if Sat, then Friday before, if Sunday, then Monday after).
     * 2. Labor Day - First Monday in September
     * A HashMap in this class is used to store the calculated holiday dates, to reduce computing.
     *
     * @param date the date to validate
     * @return true if the date is a holiday, otherwise false.
     */
    public boolean isHoliday(@NonNull final LocalDate date) {
        return holidaysPerYear.computeIfAbsent(date.getYear(),
                        year -> Set.of(getIndependenceDay(year), getLaborDay(year)))
                .contains(date);
    }

    /**
     * Find the exact date when the Independence day will be celebrated, for the given year:
     * Independence Day, July 4th - If falls on weekend, it is observed on the closest weekday (if Sat, then Friday before, if Sunday, then Monday after).
     * @param year used to find the holiday.
     * @return the exact date when the holiday is celebrated.
     */
    private static LocalDate getIndependenceDay(int year) {
        LocalDate independenceDay = LocalDate.of(year, Month.JULY, 4);
        DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();

        if (dayOfWeek == DayOfWeek.SATURDAY) {
            independenceDay = independenceDay.minusDays(1);
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            independenceDay = independenceDay.plusDays(1);
        }

        return independenceDay;
    }

    /**
     * Find the exact date when the Independence day will be celebrated, for the given year:
     * Labor Day - First Monday in September
     * @param year used to find the holiday.
     * @return the exact date when the holiday is celebrated.
     */
    private static LocalDate getLaborDay(int year) {
        return LocalDate.of(year, Month.SEPTEMBER, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }

}
