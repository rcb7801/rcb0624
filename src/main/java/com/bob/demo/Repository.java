package com.bob.demo;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Repository {

    static Connection dbConnection;

    static final HashMap<Integer, ArrayList<LocalDate>> holidayDates = new HashMap<>();

    public static RentalTool getRentalTool(String code) throws SQLException {
        if (dbConnection == null) {
            //contrived to get 100% test coverage
            dbConnection = DriverManager
                    .getConnection("jdbc:h2:mem:;INIT=RUNSCRIPT from 'classpath:rental_db.sql';");
        }
        PreparedStatement ps = dbConnection.prepareStatement("select code, tool_types.type, "
                + " brand, daily_charge, weekday_charge, weekend_charge, holiday_charge"
                + " from tools inner join tool_types on tools.tool_type_id = tool_types.id"
                + " where tools.code = ?");
        ps.setString(1, code);

        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            return new RentalTool(resultSet.getString("code"),
                    resultSet.getString("type"),
                    resultSet.getString("brand"),
                    resultSet.getInt("daily_charge"),
                    resultSet.getBoolean("weekday_charge"),
                    resultSet.getBoolean("weekend_charge"),
                    resultSet.getBoolean("holiday_charge"));
        } else {
            //contrived to get 100% test coverage
            throw new SQLException(String.format("Error: Tool code '%s' is invalid.", code));
        }
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
