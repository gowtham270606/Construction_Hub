package com.gowthamX.constructionHub.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ParseHelper {

    private static final String DATE_PATTERN = "dd-MM-yyyy";

    private static final String EMPTY = "-";

    private ParseHelper() {
    }

    public static boolean isYes(String input) {
        if (input == null) return false;
        String t = input.trim();
        return !t.isEmpty() && (t.charAt(0) == 'Y' || t.charAt(0) == 'y');
    }

    public static Long parseDate(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, Locale.ROOT);
        sdf.setLenient(false);
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            return sdf.parse(input.trim()).getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(Long millis) {
        if (millis == null) return EMPTY;
        return new SimpleDateFormat(DATE_PATTERN, Locale.ROOT).format(new Date(millis));
    }

    public static boolean isSameDay(long a, long b) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, Locale.ROOT);
        return sdf.format(new Date(a)).equals(sdf.format(new Date(b)));
    }
}
