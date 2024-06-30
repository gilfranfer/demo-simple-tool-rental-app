package com.gillab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@AllArgsConstructor @Getter @Setter
public class RentalAgreement {
    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private double dailyRentalCharge;
    private int chargeDays;
    private double preDiscountCharge;
    private int discountPercent;
    private double discountAmount;
    private double finalCharge;

   @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat percentFormatter = NumberFormat.getPercentInstance(Locale.US);
        percentFormatter.setMinimumFractionDigits(0);

        return "Rental Agreement:\n" +
                "Tool Code: " + toolCode + "\n" +
                "Tool Type: " + toolType + "\n" +
                "Tool Brand: " + toolBrand + "\n" +
                "Rental Days: " + rentalDays + "\n" +
                "Checkout Date: " + checkoutDate.format(dateFormatter) + "\n" +
                "Due Date: " + dueDate.format(dateFormatter) + "\n" +
                "Daily Rental Charge: " + currencyFormatter.format(dailyRentalCharge) + "\n" +
                "Charge Days: " + chargeDays + "\n" +
                "Pre-discount Charge: " + currencyFormatter.format(preDiscountCharge) + "\n" +
                "Discount Percent: " + percentFormatter.format(discountPercent / 100.0) + "\n" +
                "Discount Amount: " + currencyFormatter.format(discountAmount) + "\n" +
                "Final Charge: " + currencyFormatter.format(finalCharge);
    }

}
