package hms.util;

import hms.config.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class StringUtil {

    private StringUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Truncates a string to maxLength characters, appending "...".
     */
    public static String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        if (value.length() <= maxLength) {
            return value;
        }
        int truncateAt = Math.max(0, maxLength - 3);
        return value.substring(0, truncateAt) + "...";
    }

    /**
     * Capitalizes the first character and lowercases the rest.
     */
    public static String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String trimmed = value.trim();
        return Character.toUpperCase(trimmed.charAt(0))
                + trimmed.substring(1).toLowerCase();
    }

    /**
     * Capitalizes the first character of each word.
     */
    public static String capitalizeWords(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String[] words = value.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            String word = words[i];
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * Trims whitespace and collapses internal whitespace to single spaces.
     */
    public static String sanitize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replaceAll("\\s+", " ");
    }

    /**
     * Generates a reservation ID in format RES-YYYYMMDD-XXXXX.
     */
    public static String generateReservationId() {
        String datePart = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomPart = ThreadLocalRandom.current().nextInt(100000);
        return Constants.RES_ID_PREFIX + "-" + datePart + "-"
                + String.format("%05d", randomPart);
    }

    /**
     * Generates a reservation ID with a specific sequence number.
     */
    public static String generateReservationId(int sequenceNumber) {
        String datePart = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = Math.max(0, Math.min(sequenceNumber, 99999));
        return Constants.RES_ID_PREFIX + "-" + datePart + "-"
                + String.format("%05d", seq);
    }
}
