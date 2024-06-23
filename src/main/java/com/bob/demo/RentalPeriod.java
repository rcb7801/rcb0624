package com.bob.demo;

import java.time.LocalDate;

public class RentalPeriod {
    long weekendDays;
    long weekDays;
    long holidays;

    public RentalPeriod(LocalDate checkoutDate, long rentalDays) {
        weekendDays = 0;
        weekDays = 0;
        holidays = 0;

        //checkoutDate doesn't count as a rentalDay
        for (long day = 1; day <= rentalDays; day++) {
            LocalDate rentalDay = checkoutDate.plusDays(day);
            if (Repository.isHoliday(rentalDay)) {
                holidays++;
            } else {
                switch (rentalDay.getDayOfWeek()) {
                    case SATURDAY, SUNDAY:
                        weekendDays++;
                        break;
                    default:
                        weekDays++;
                }
            }
        }
    }

    public long getWeekendDays() {
        return weekendDays;
    }

    public long getWeekDays() {
        return weekDays;
    }

    public long getHolidays() {
        return holidays;
    }
}
