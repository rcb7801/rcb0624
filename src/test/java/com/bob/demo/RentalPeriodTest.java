package com.bob.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

class RentalPeriodTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            2015, 9, 3, 5, 1, 2, 2
            2020, 7, 2, 3, 1, 2, 0
            2015, 7, 2, 5, 1, 2, 2
            2015, 9, 3, 6, 1, 2, 3
            2015, 7, 2, 9, 1, 3, 5
            2020, 7, 2, 4, 1, 2, 1
            """)
    void periodTests(int checkoutYear, int checkoutMonth, int checkoutDay,
                     int rentalDays,
                     int expectedHolidays, int expectedWeekendDays, int expectedWeekDays) {
        RentalPeriod rp = new RentalPeriod(LocalDate.of(checkoutYear,checkoutMonth,checkoutDay), rentalDays);

        Assertions.assertEquals(expectedHolidays, rp.getHolidays());
        Assertions.assertEquals(expectedWeekendDays, rp.getWeekendDays());
        Assertions.assertEquals(expectedWeekDays, rp.getWeekDays());
    }
}