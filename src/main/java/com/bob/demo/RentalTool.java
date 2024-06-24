package com.bob.demo;

public record RentalTool(String code,
                         String type,
                         String brand,
                         long dailyCharge,
                         boolean weekdayCharge,
                         boolean weekendCharge,
                         boolean holidayCharge) {}

