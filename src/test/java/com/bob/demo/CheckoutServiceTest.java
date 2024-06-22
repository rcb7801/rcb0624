package com.bob.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

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
                                                       long preDiscountCharge,
                                                       long discount,
                                                       long finalCharge) throws Exception {
        RentalAgreement ra = CheckoutService.checkout(toolCode, rentalDayCount,
                discountPercent, checkoutDate);

        Assertions.assertEquals(chargeDays, ra.getChargeDays());
        Assertions.assertEquals(preDiscountCharge, ra.getPreDiscountCharge());
        Assertions.assertEquals(discount, ra.getDiscountAmount());
        Assertions.assertEquals(finalCharge, ra.getFinalCharge());
        List<String> errors = new ArrayList<>();
        Assertions.assertEquals(CheckoutService.stringToLocalDate(dueDate, errors), ra.getDueDate());

        String rentalAgreement = ra.asString();

        List<String> lines=expectedAgreementInfo();
        for (String line : lines) {
            assertTrue(rentalAgreement.contains(line));
        }
    }

    List<String> expectedAgreementInfo() {
        ArrayList<String> names=new ArrayList<>();

        names.add("Tool code: ");
        names.add("Tool type: ");
        names.add("Tool brand: ");
        names.add("Rental days: ");
        names.add("Checkout date: ");
        names.add("Due date: ");
        names.add("Daily rental charge: ");
        names.add("Charge days: ");
        names.add("Nocharge days: ");
        names.add("Pre-discount charge: ");
        names.add("Discount percent: ");
        names.add("Discount amount: ");
        names.add("Final charge: ");
        return names;
    }

    @ParameterizedTest
    @CsvSource({"JAKR, 5, 101, 9/3/15", "JAKR, 5, -1, 9/3/15"})
    void checkoutWithInvalidDiscountShouldThrow(String code,
                                                int days,
                                                int discountPercent,
                                                String checkoutDate) throws Exception {

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            CheckoutService.checkout(code, days,
                    discountPercent, checkoutDate);
        });

        String expectedMessage = "Error: Discount percent is not in the range 0-100";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"JAKR, 0, 100, 9/3/15"})
    void checkoutWithInvalidRentalDayCountShouldThrow(String code,
                                                      int days,
                                                      int discountPercent,
                                                      String checkoutDate) throws Exception {

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            CheckoutService.checkout(code, days,
                    discountPercent, checkoutDate);
        });

        String expectedMessage = "Error: Rental day count is not 1 or greater.";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"BOBB, 5, 100, 9/3/15"})
    void checkoutWithInvalidToolCodeShouldThrow(String code,
                                                int days,
                                                int discountPercent,
                                                String checkoutDate) throws Exception {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            CheckoutService.checkout(code, days,
                    discountPercent, checkoutDate);
        });

        String expectedMessage = "Error: Tool code 'BOBB' is invalid.";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @CsvSource({"CHNS, 5, 100, 6", "CHNS, 5, 100, 6/31", "CHNS, 5, 100, 6/31/15"})
    void checkoutWithInvalidCheckoutDateShouldThrow(String code,
                                                    int days,
                                                    int discountPercent,
                                                    String checkoutDate) throws Exception {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            CheckoutService.checkout(code, days,
                    discountPercent, checkoutDate);
        });

        String actualMessage = e.getMessage();
        String expectedInvalidDayMessage = String.format("Error: '%s' has an invalid day number.", checkoutDate);
        String expectedInvalidLengthMessage = String.format("Error: '%s' is not a valid date.", checkoutDate);
        assertTrue(actualMessage.contains(expectedInvalidDayMessage) ||
                actualMessage.contains(expectedInvalidLengthMessage));
    }
}