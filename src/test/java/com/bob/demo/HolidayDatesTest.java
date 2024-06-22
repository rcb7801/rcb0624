package com.bob.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

class HolidayDatesTest {

    // 2024 - July 4th on Thursday (leap year)
    // 2020 - July 4th on Saturday (observed 7/3) (leap year)
    // 2015 - July 4th on Saturday (observed 7/3)
    // 2010 - July 4th on Sunday (observed 7/5)
    @Test
    void isJulyFourthHoliday() {
        Assertions.assertTrue(Repository.isHoliday(LocalDate.of(2024, Month.JULY, 4)));
        Assertions.assertTrue(Repository.isHoliday(LocalDate.of(2020, Month.JULY, 3)));
        Assertions.assertTrue(Repository.isHoliday(LocalDate.of(2015, Month.JULY, 3)));
        Assertions.assertTrue(Repository.isHoliday(LocalDate.of(2010, Month.JULY, 5)));

        Assertions.assertFalse(Repository.isHoliday(LocalDate.of(2024, Month.JULY, 3)));
        Assertions.assertFalse(Repository.isHoliday(LocalDate.of(2020, Month.JULY, 4)));
        Assertions.assertFalse(Repository.isHoliday(LocalDate.of(2015, Month.JULY, 5)));
        Assertions.assertFalse(Repository.isHoliday(LocalDate.of(2010, Month.JULY, 4)));
    }

    // 2024 - Labor day September 2nd (leap year)
    // 2020 - Labor day September 7th (leap year)
    // 2015 - Labor day September 7th
    // 2010 - Labor day September 6th
    @Test
    void isLaborDayHoliday() {
        Assertions.assertTrue(Repository.isHoliday(LocalDate.of(2024, Month.SEPTEMBER, 2)));
        Assertions.assertTrue(Repository.isHoliday(LocalDate.of(2020, Month.SEPTEMBER, 7)));
        Assertions.assertTrue(Repository.isHoliday(LocalDate.of(2015, Month.SEPTEMBER, 7)));
        Assertions.assertTrue(Repository.isHoliday(LocalDate.of(2010, Month.SEPTEMBER, 6)));

        Assertions.assertFalse(Repository.isHoliday(LocalDate.of(2024, Month.SEPTEMBER, 9)));
        Assertions.assertFalse(Repository.isHoliday(LocalDate.of(2020, Month.SEPTEMBER, 14)));
        Assertions.assertFalse(Repository.isHoliday(LocalDate.of(2015, Month.SEPTEMBER, 1)));
        Assertions.assertFalse(Repository.isHoliday(LocalDate.of(2010, Month.SEPTEMBER, 7)));
    }
}