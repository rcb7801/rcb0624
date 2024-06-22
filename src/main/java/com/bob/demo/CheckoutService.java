package com.bob.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckoutService {

    public static RentalAgreement checkout(String code,
                                           long rentalDayCount,
                                           long discountPercentage,
                                           String checkoutDateStr) throws IllegalArgumentException {

        List<String> errors = new ArrayList<>();
        LocalDate checkoutDate = stringToLocalDate(checkoutDateStr, errors);

        if (rentalDayCount<1)
            errors.add("Error: Rental day count is not 1 or greater.");

        if (discountPercentage<0 || discountPercentage>100)
            errors.add("Error: Discount percent is not in the range 0-100");

        RentalTool rentalTool = Repository.getRentalTool(code);

        if (rentalTool == null)
            errors.add(String.format("Error: Tool code '%s' is invalid.", code));

        if (errors.isEmpty())
            return new RentalAgreement(rentalTool, rentalDayCount, discountPercentage, checkoutDate);
        else
            throw new IllegalArgumentException(String.join("\n", errors));
    }

    public static LocalDate stringToLocalDate(String date, List<String> errors) {
        DateTimeFormatter fmtYY = DateTimeFormatter.ofPattern("M/d/yy");
        DateTimeFormatter fmtYYYY = DateTimeFormatter.ofPattern("M/d/yyyy");
        String newDate="";
        LocalDate dt=null;
        try {
            dt = LocalDate.from(fmtYY.parse(date));
            newDate = dt.format(fmtYY);
        } catch (DateTimeParseException e) {
            try {
                dt = LocalDate.from(fmtYYYY.parse(date));
                newDate = dt.format(fmtYYYY);
            } catch (DateTimeParseException e2) {
                errors.add(String.format("Error: '%s' is not a valid date.", date));
            }
        }

        if (!newDate.isEmpty()) {
            // ResolverStyle(s) didn't do what I wanted...
            int[] inputInts = Arrays.stream(date.split("/")).mapToInt(Integer::parseInt).toArray();
            int[] newInts = Arrays.stream(newDate.split("/")).mapToInt(Integer::parseInt).toArray();
            // either successfully parsed date format has 3 ints.
            if (inputInts[1] != newInts[1])
                errors.add(String.format("Error: '%s' has an invalid day number.", date));
        }

        return dt;
    }
}
