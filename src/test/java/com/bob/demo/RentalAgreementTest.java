package com.bob.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RentalAgreementTest {

    @Test
    void asMoney() {
        long amountInCents = 999999; // $9,999.99
        String money = RentalAgreement.asMoney(amountInCents);
        assertEquals("$9,999.99", money);

        amountInCents = 5;
        money = RentalAgreement.asMoney(amountInCents);
        assertEquals("$0.05", money);

        long discountCents = Math.round((395 * 10) / 100.0); // $0.395
        money = RentalAgreement.asMoney(discountCents);
        assertEquals("$0.40", money);
    }

    @Test
    void asPercent() {
        long percent = 0;
        String discount = RentalAgreement.asPercent(percent);
        assertEquals("0%", discount);

        percent = 5;
        discount = RentalAgreement.asPercent(percent);
        assertEquals("5%", discount);

        percent = 100;
        discount = RentalAgreement.asPercent(percent);
        assertEquals("100%", discount);
    }
}