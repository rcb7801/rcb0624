package com.bob.demo;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        RentalAgreement agreement = CheckoutService.checkout("LADW", 366 * 10,
                10, "1/1/2024");
        agreement.logToConsole();
    }
}