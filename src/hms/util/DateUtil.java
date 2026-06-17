package hms.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public final class DateUtil {

    private DateUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Calculates the number of nights between two dates.
     * Returns 0 if dates are null or checkIn is after checkOut.
     *
     * @param checkIn The check-in date
     * @param checkOut The check-out date
     * @return The number of nights, or 0 if invalid
     */
    public static long calculateNights(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || checkIn.isAfter(checkOut)) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    /**
     * Formats a date to ISO format (yyyy-MM-dd).
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Formats a date using a custom pattern.
     */
    public static String formatDate(LocalDate date, String pattern) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Formats a date-time to yyyy-MM-dd HH:mm:ss.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Formats a date-time using a custom pattern.
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Parses a date string in ISO format (yyyy-MM-dd).
     * Returns null on invalid input.
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Parses a date string using a custom pattern.
     * Returns null on invalid input.
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Generates a list of dates from start to end (inclusive).
     */
    public static List<LocalDate> getDateRange(LocalDate start, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        if (start == null || end == null || start.isAfter(end)) {
            return dates;
        }
        LocalDate current = start;
        while (!current.isAfter(end)) {
            dates.add(current);
            current = current.plusDays(1);
        }
        return dates;
    }
}
