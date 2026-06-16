package hms.util;

import hms.config.Constants;
import java.time.LocalDate;

public final class ValidationUtil {

    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        if (email.length() > Constants.MAX_EMAIL_LENGTH) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return phone.replaceAll("[^0-9]", "").length() <= Constants.MAX_PHONE_LENGTH;
    }

    public static boolean isValidName(String name) {
        return name != null && !name.isBlank() && name.matches("^[a-zA-Z\\s]+$");
    }

    public static boolean isFutureDate(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    public static boolean isPastDate(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    public static boolean isDateInRange(LocalDate date, LocalDate min, LocalDate max) {
        if (date == null || min == null || max == null) {
            return false;
        }
        return !date.isBefore(min) && !date.isAfter(max);
    }

    public static boolean isPositive(double value) {
        return value > 0;
    }

    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean isPositive(int value) {
        return value > 0;
    }

    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean isValidLength(String value, int maxLength) {
        if (value == null) {
            return true;
        }
        return value.length() <= maxLength;
    }

    public static boolean matchesPattern(String value, String regex) {
        return value != null && value.matches(regex);
    }
}
