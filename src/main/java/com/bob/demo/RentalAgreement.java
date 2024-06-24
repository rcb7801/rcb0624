package com.bob.demo;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentalAgreement {
    RentalTool rentalTool;
    long rentalDayCount;
    long discountPercent;
    LocalDate checkoutDate;

    //calculated...
    LocalDate dueDate;
    RentalPeriod rentalPeriod;
    long chargeDays;
    long nochargeDays;
    long preDiscountCharge;
    long discountAmount;
    long finalCharge;

    public RentalAgreement(RentalTool rentalTool, long rentalDayCount,
                           long discountPercent, LocalDate checkoutDate) {
        setRentalTool(rentalTool);
        setRentalDayCount(rentalDayCount);
        setDiscountPercent(discountPercent);
        setCheckoutDate(checkoutDate);
        calculations();
    }

    void calculations() {
        setDueDate(checkoutDate.plusDays(rentalDayCount));
        setRentalPeriod(new RentalPeriod(checkoutDate, rentalDayCount));

        updateChargeDays(rentalPeriod.getHolidays(), rentalTool.holidayCharge());
        updateChargeDays(rentalPeriod.getWeekendDays(), rentalTool.weekendCharge());
        updateChargeDays(rentalPeriod.getWeekDays(), rentalTool.weekdayCharge());

        setPreDiscountCharge(getChargeDays() * rentalTool.dailyCharge());
        setDiscountAmount(Math.round((getPreDiscountCharge() * getDiscountPercent()) / 100.0));

        setFinalCharge(getPreDiscountCharge() - getDiscountAmount());
    }

    void setRentalTool(RentalTool rentalTool) {
        this.rentalTool = rentalTool;
    }

    void setRentalDayCount(long rentalDayCount) {
        this.rentalDayCount = rentalDayCount;
    }

    void setDiscountPercent(long discountPercent) {
        this.discountPercent = discountPercent;
    }

    void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    void setRentalPeriod(RentalPeriod rentalPeriod) {
        this.rentalPeriod = rentalPeriod;
    }

    void updateChargeDays(long days, boolean chargeable) {
        if (chargeable)
            this.chargeDays += days;
        else
            this.nochargeDays += days;
    }

    void setPreDiscountCharge(long preDiscountCharge) {
        this.preDiscountCharge = preDiscountCharge;
    }

    void setDiscountAmount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    void setFinalCharge(long finalCharge) {
        this.finalCharge = finalCharge;
    }

    public static String asMoney(long amountInCents) {
        //input amount is in whole cents
        double dollars = amountInCents / 100.0;
        DecimalFormat moneyFormatter = new DecimalFormat("###,##0.00");
        String moneyDollars = moneyFormatter.format(dollars);
        return "$" + moneyDollars;
    }

    public static String asPercent(long percent) {
        return percent + "%";
    }

    public static String asDate(LocalDate d) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
        return d.format(dtf);
    }

    public String asString() {
        return "Tool code: " + rentalTool.code() +
                "\nTool type: " + rentalTool.type() +
                "\nTool brand: " + rentalTool.brand() +
                "\nRental days: " + rentalDayCount +
                "\nCheckout date: " + asDate(checkoutDate) +
                "\nDue date: " + asDate(dueDate) +
                "\nDaily rental charge: " + asMoney(rentalTool.dailyCharge()) +
                "\nCharge days: " + getChargeDays() +
                "\nNocharge days: " + getNochargeDays() +
                "\nPre-discount charge: " + asMoney(getPreDiscountCharge()) +
                "\nDiscount percent: " + asPercent(getDiscountPercent()) +
                "\nDiscount amount: " + asMoney(getDiscountAmount()) +
                "\nFinal charge: " + asMoney(finalCharge);
    }

    public void logToConsole() {
        System.out.println(asString());
    }

    public RentalTool getRentalTool() {
        return rentalTool;
    }

    public long getRentalDayCount() {
        return rentalDayCount;
    }

    public long getDiscountPercent() {
        return discountPercent;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public long getChargeDays() {
        return chargeDays;
    }

    public long getNochargeDays() {
        return nochargeDays;
    }

    public long getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public long getFinalCharge() {
        return finalCharge;
    }
}
