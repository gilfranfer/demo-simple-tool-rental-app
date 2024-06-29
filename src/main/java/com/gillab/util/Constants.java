package com.gillab.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final Integer MIN_DISCOUNT_PERCENTAGE = 0;
    public static final Integer MAX_DISCOUNT_PERCENTAGE = 100;
    public static final Predicate<Integer> IS_VALID_DISCOUNT = i -> i != null && i >= MIN_DISCOUNT_PERCENTAGE && i <= MAX_DISCOUNT_PERCENTAGE;

    public static final Integer MIN_RENTAL_DAYS = 1;
    public static final Integer MAX_RENTAL_DAYS = 365;
    public static final Predicate<Integer> IS_VALID_RENTAL_DAYS = i -> i != null && i >= MIN_RENTAL_DAYS && i <= MAX_RENTAL_DAYS;

    public static final Double LADDER_DAILY_CHARGE = 1.99;
    public static final Double CHAINSAW_DAILY_CHARGE = 1.49;
    public static final Double JACKHAMMER_DAILY_CHARGE = 1.99;

}
