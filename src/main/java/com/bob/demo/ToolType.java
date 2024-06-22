package com.bob.demo;

public record ToolType (String type,
                        long dailyCharge,
                        boolean weekdayCharge,
                        boolean weekendCharge,
                        boolean holidayCharge) {}
