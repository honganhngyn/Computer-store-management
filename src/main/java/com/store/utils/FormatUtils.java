package com.store.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatUtils {
    public static String formatCurrency(double price) {
        // Định dạng theo chuẩn Việt Nam
        return String.format("%,.0f", price).replace(",", ".");
    }

    public static String formatDateTime(LocalDateTime dateTime) { // Nhận LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(formatter);
    }

}
