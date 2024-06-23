package com.bob.demo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Repository {
    static final HashMap<String, ToolType> toolTypes = new HashMap<>();
    static final HashMap<String, RentalTool> rentalTools = new HashMap<>();
    static final HashMap<Integer, ArrayList<LocalDate>> holidayDates = new HashMap<>();

    static {
        toolTypes.put("Ladder", new ToolType("Ladder", 199, true, true, false));
        toolTypes.put("Chainsaw", new ToolType("Chainsaw", 149, true, false, true));
        toolTypes.put("Jackhammer", new ToolType("Jackhammer", 299, true, false, false));
        toolTypes.put("Promotional", new ToolType("Promotional", 0, false, false, false));

        rentalTools.put("CHNS", new RentalTool("CHNS", "Stihl", getToolType("Chainsaw")));
        rentalTools.put("LADW", new RentalTool("LADW", "Werner", getToolType("Ladder")));
        rentalTools.put("JAKD", new RentalTool("JAKD", "Dewalt", getToolType("Jackhammer")));
        rentalTools.put("JAKR", new RentalTool("JAKR", "Rigid", getToolType("Jackhammer")));
        rentalTools.put("PROM", new RentalTool("PROM", "Any", getToolType("Promotional")));
    }

    public static ToolType getToolType(String name) {
        return toolTypes.get(name);
    }

    public static RentalTool getRentalTool(String name) {
        return rentalTools.get(name);
    }

    static ArrayList<LocalDate> getHolidayDates(Integer year) {
        holidayDates.putIfAbsent(year, new ArrayList<>());

        ArrayList<LocalDate> dates = holidayDates.get(year);

        if (dates.isEmpty()) {
            LocalDate julyFourth = LocalDate.of(year, 7, 4);
            if (julyFourth.getDayOfWeek() == DayOfWeek.SATURDAY)
                dates.add(julyFourth.minusDays(1));
            else if (julyFourth.getDayOfWeek() == DayOfWeek.SUNDAY)
                dates.add(julyFourth.plusDays(1));
            else
                dates.add(julyFourth);

            LocalDate laborDay = LocalDate.of(year, 9, 1);
            while (laborDay.getDayOfWeek() != DayOfWeek.MONDAY)
                laborDay = laborDay.plusDays(1);
            dates.add(laborDay);
        }

        return dates;
    }

    public static boolean isHoliday(LocalDate date) {
        ArrayList<LocalDate> dates = getHolidayDates(date.getYear());
        return dates.contains(date);
    }
}
