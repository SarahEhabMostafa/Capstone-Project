package com.sarahehabm.carbcalculator.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 Created by Sarah E. Mostafa on 03-Jun-16.
 */

public class Utility {
    public static final String DEFAULT_DATE_PATTERN = "EEE, d MMM yyyy h:mm a";
    public static final String DEFAULT_TIME_PATTERN = "hh:mm a";

    public static String formatDate(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        return dateFormat.format(date);
    }

    public static String formatDate(long millis, String pattern) {
        Date date = new Date(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static String formatTime(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_TIME_PATTERN);
        return dateFormat.format(date);
    }

    public static long getStartOfDayTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static long getEndOfDayTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTimeInMillis();
    }
}
