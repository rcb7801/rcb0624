package com.bob.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

class RentalPeriodTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            2015, 9, 3, 5, 2015, 9, 8, 1, 2, 2
            2020, 7, 2, 3, 2020, 7, 5, 1, 2, 0
            2015, 7, 2, 5, 2015, 7, 7, 1, 2, 2
            2015, 9, 3, 6, 2015, 9, 9, 1, 2, 3
            2015, 7, 2, 9, 2015, 7,11, 1, 3, 5
            2020, 7, 2, 4, 2020, 7, 6, 1, 2, 1
            """)
    void periodTests(int checkoutYear, int checkoutMonth, int checkoutDay,
                     int rentalDays,
                     int dueDateYear, int dueDateMonth, int dueDateDay,
                     int expectedHolidays, int expectedWeekendDays, int expectedWeekDays) {
        RentalPeriod rp = new RentalPeriod(LocalDate.of(checkoutYear,checkoutMonth,checkoutDay), rentalDays);
        Assertions.assertEquals(LocalDate.of(dueDateYear,dueDateMonth,dueDateDay),rp.getDueDate());
        Assertions.assertEquals(expectedHolidays, rp.getHolidays());
        Assertions.assertEquals(expectedWeekendDays, rp.getWeekendDays());
        Assertions.assertEquals(expectedWeekDays, rp.getWeekDays());
    }
}