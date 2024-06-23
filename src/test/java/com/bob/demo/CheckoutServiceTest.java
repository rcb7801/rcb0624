package com.bob.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class CheckoutServiceTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/validCheckouts.csv", numLinesToSkip = 2)
    void validCheckoutShouldReturnValidRentalAgreement(String toolCode,
                                                       int rentalDayCount,
                                                       int discountPercent,
                                                       String checkoutDate,
                                                       String dueDate,
                                                       long chargeDays,
                                                       long nochargeDays,
                                                       long preDiscountCharge,
                                                       long discount,
                                                       long finalCharge) {
        RentalAgreement ra = CheckoutService.checkout(toolCode, rentalDayCount,
                discountPercent, checkoutDate);

        Assertions.assertEquals(chargeDays, ra.getChargeDays());
        Assertions.assertEquals(nochargeDays, ra.getNochargeDays());
        Assertions.assertEquals(preDiscountCharge, ra.getPreDiscountCharge());
        Assertions.assertEquals(discount, ra.getDiscountAmount());
        Assertions.assertEquals(finalCharge, ra.getFinalCharge());
        List<String> errors = new ArrayList<>();
        Assertions.assertEquals(CheckoutService.stringToLocalDate(dueDate, errors), ra.getDueDate());

        String rentalAgreement = ra.asString();

        RentalTool tool=Repository.getRentalTool(toolCode);

        ArrayList<String> expectedLines=new ArrayList<>();
        expectedLines.add("Tool code: " + tool.code());
        expectedLines.add("Tool type: " + tool.toolType().type());
        expectedLines.add("Tool brand: " + tool.brand());
        expectedLines.add("Rental days: " + rentalDayCount);
        expectedLines.add("Checkout date: " + formattedDate(checkoutDate));
        expectedLines.add("Due date: " + formattedDate(dueDate));
        expectedLines.add("Daily rental charge: " + RentalAgreement.asMoney(tool.toolType().dailyCharge()));
        expectedLines.add("Charge days: " + chargeDays);
        expectedLines.add("Nocharge days: " + (rentalDayCount - chargeDays));
        expectedLines.add("Pre-discount charge: " + RentalAgreement.asMoney(preDiscountCharge));
        expectedLines.add("Discount percent: " + RentalAgreement.asPercent(discountPercent));
        expectedLines.add("Discount amount: " + RentalAgreement.asMoney(discount));
        expectedLines.add("Final charge: " + RentalAgreement.asMoney(finalCharge));

        int fails=0;
        for (String line : expectedLines) {
            boolean found = rentalAgreement.contains(line);
            if (found)
                assertTrue(found);
            else {
                String lineNotFound = "Not found " + line;
                System.out.println(lineNotFound);
            }
        }
        Assertions.assertEquals(0, fails);
    }

    LocalDate toDate(String date) {
        LocalDate dt;
        try {
            DateTimeFormatter fmtYY = DateTimeFormatter.ofPattern("M/d/yy");
            dt = LocalDate.from(fmtYY.parse(date));
        } catch  (Exception e) {
            DateTimeFormatter fmtYY = DateTimeFormatter.ofPattern("M/d/yyyy");
            dt = LocalDate.from(fmtYY.parse(date));
        }
        return dt;
    }

    String formattedDate(String date) {
        DateTimeFormatter fmtYY = DateTimeFormatter.ofPattern("M/d/yy");
        LocalDate dt=toDate(date);
        return RentalAgreement.asDate(dt);
    }

    @ParameterizedTest
    @CsvSource({"JAKR, 5, 101, 9/3/15", "JAKR, 5, -1, 9/3/15"})
    void checkoutWithInvalidDiscountShouldThrow(String code,
                                                int days,
                                                int discountPercent,
                                                String checkoutDate) {

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> CheckoutService.checkout(code, days,
                discountPercent, checkoutDate));

        String expectedMessage = "Error: Discount percent is not in the range 0-100";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"JAKR, 0, 100, 9/3/15"})
    void checkoutWithInvalidRentalDayCountShouldThrow(String code,
                                                      int days,
                                                      int discountPercent,
                                                      String checkoutDate) {

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> CheckoutService.checkout(code, days,
                discountPercent, checkoutDate));

        String expectedMessage = "Error: Rental day count is not 1 or greater.";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"BOBB, 5, 100, 9/3/15"})
    void checkoutWithInvalidToolCodeShouldThrow(String code,
                                                int days,
                                                int discountPercent,
                                                String checkoutDate) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> CheckoutService.checkout(code, days,
                discountPercent, checkoutDate));

        String expectedMessage = "Error: Tool code 'BOBB' is invalid.";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"CHNS, 5, 100, 6", "CHNS, 5, 100, 6/31", "CHNS, 5, 100, 6/31/15"})
    void checkoutWithInvalidCheckoutDateShouldThrow(String code,
                                                    int days,
                                                    int discountPercent,
                                                    String checkoutDate) {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> CheckoutService.checkout(code, days,
                discountPercent, checkoutDate));

        String actualMessage = e.getMessage();
        String expectedInvalidDayMessage = String.format("Error: '%s' has an invalid day number.", checkoutDate);
        String expectedInvalidLengthMessage = String.format("Error: '%s' is not a valid date.", checkoutDate);
        assertTrue(actualMessage.contains(expectedInvalidDayMessage) ||
                actualMessage.contains(expectedInvalidLengthMessage));
    }
}