package com.iqkv.misc.util;

import java.util.Collection;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for common validation operations. Provides static methods for validating strings, emails, collections, and other common data types.
 */
@Slf4j
public final class ValidationUtils {

  // Email validation regex pattern
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

  // Phone number validation regex pattern (international format)
  private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[1-9]\\d{1,14}$");

  // UUID validation regex pattern
  private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

  // URL validation regex pattern
  private static final Pattern URL_PATTERN = Pattern.compile("^https?://[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?$");

  private ValidationUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * Checks if a string is null, empty, or contains only whitespace.
   *
   * @param str The string to check
   * @return true if the string is blank, false otherwise
   */
  public static boolean isBlank(String str) {
    return StringUtils.isBlank(str);
  }

  /**
   * Checks if a string is not null, not empty, and contains non-whitespace characters.
   *
   * @param str The string to check
   * @return true if the string is not blank, false otherwise
   */
  public static boolean isNotBlank(String str) {
    return StringUtils.isNotBlank(str);
  }

  /**
   * Validates an email address format.
   *
   * @param email The email address to validate
   * @return true if the email format is valid, false otherwise
   */
  public static boolean isValidEmail(String email) {
    if (isBlank(email)) {
      return false;
    }
    return EMAIL_PATTERN.matcher(email.trim()).matches();
  }

  /**
   * Validates a phone number format (international format).
   *
   * @param phoneNumber The phone number to validate
   * @return true if the phone number format is valid, false otherwise
   */
  public static boolean isValidPhoneNumber(String phoneNumber) {
    if (isBlank(phoneNumber)) {
      return false;
    }
    String cleanPhone = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");
    return PHONE_PATTERN.matcher(cleanPhone).matches();
  }

  /**
   * Validates a UUID format.
   *
   * @param uuid The UUID string to validate
   * @return true if the UUID format is valid, false otherwise
   */
  public static boolean isValidUUID(String uuid) {
    if (isBlank(uuid)) {
      return false;
    }
    return UUID_PATTERN.matcher(uuid.trim()).matches();
  }

  /**
   * Validates a URL format.
   *
   * @param url The URL to validate
   * @return true if the URL format is valid, false otherwise
   */
  public static boolean isValidURL(String url) {
    if (isBlank(url)) {
      return false;
    }
    return URL_PATTERN.matcher(url.trim()).matches();
  }

  /**
   * Checks if a collection is null or empty.
   *
   * @param collection The collection to check
   * @return true if the collection is null or empty, false otherwise
   */
  public static boolean isEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  /**
   * Checks if a collection is not null and not empty.
   *
   * @param collection The collection to check
   * @return true if the collection is not null and not empty, false otherwise
   */
  public static boolean isNotEmpty(Collection<?> collection) {
    return !isEmpty(collection);
  }

  /**
   * Validates that a string length is within specified bounds.
   *
   * @param str       The string to validate
   * @param minLength Minimum required length (inclusive)
   * @param maxLength Maximum allowed length (inclusive)
   * @return true if the string length is within bounds, false otherwise
   */
  public static boolean isValidLength(String str, int minLength, int maxLength) {
    if (str == null) {
      return minLength <= 0;
    }
    int length = str.length();
    return length >= minLength && length <= maxLength;
  }

  /**
   * Validates that a number is within specified range.
   *
   * @param number The number to validate
   * @param min    Minimum value (inclusive)
   * @param max    Maximum value (inclusive)
   * @return true if the number is within range, false otherwise
   */
  public static boolean isInRange(Number number, Number min, Number max) {
    if (number == null) {
      return false;
    }
    double value = number.doubleValue();
    double minValue = min.doubleValue();
    double maxValue = max.doubleValue();
    return value >= minValue && value <= maxValue;
  }

  /**
   * Validates that a string contains only alphanumeric characters.
   *
   * @param str The string to validate
   * @return true if the string contains only alphanumeric characters, false otherwise
   */
  public static boolean isAlphanumeric(String str) {
    if (isBlank(str)) {
      return false;
    }
    return str.matches("^[a-zA-Z0-9]+$");
  }

  /**
   * Validates that a string contains only alphabetic characters.
   *
   * @param str The string to validate
   * @return true if the string contains only alphabetic characters, false otherwise
   */
  public static boolean isAlphabetic(String str) {
    if (isBlank(str)) {
      return false;
    }
    return str.matches("^[a-zA-Z]+$");
  }

  /**
   * Validates that a string contains only numeric characters.
   *
   * @param str The string to validate
   * @return true if the string contains only numeric characters, false otherwise
   */
  public static boolean isNumeric(String str) {
    if (isBlank(str)) {
      return false;
    }
    return str.matches("^[0-9]+$");
  }

  /**
   * Validates a password strength based on common criteria. Requires at least 8 characters, one uppercase, one lowercase, one digit, and one special character.
   *
   * @param password The password to validate
   * @return true if the password meets strength criteria, false otherwise
   */
  public static boolean isStrongPassword(String password) {
    if (isBlank(password) || password.length() < 8) {
      return false;
    }

    boolean hasUppercase = password.matches(".*[A-Z].*");
    boolean hasLowercase = password.matches(".*[a-z].*");
    boolean hasDigit = password.matches(".*[0-9].*");
    boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

    return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
  }

  /**
   * Sanitizes a string by removing potentially dangerous characters. Useful for preventing XSS attacks in user input.
   *
   * @param input The input string to sanitize
   * @return Sanitized string with dangerous characters removed
   */
  public static String sanitizeInput(String input) {
    if (isBlank(input)) {
      return input;
    }

    return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&#x27;").replaceAll("/", "&#x2F;").replaceAll("\\\\", "&#x5C;").trim();
  }

  /**
   * Validates that all provided objects are not null.
   *
   * @param objects Objects to check for null values
   * @return true if all objects are not null, false if any object is null
   */
  public static boolean areNotNull(Object... objects) {
    if (objects == null) {
      return false;
    }
    for (final Object obj : objects) {
      if (obj == null) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if any of the provided objects is null.
   *
   * @param objects Objects to check for null values
   * @return true if any object is null, false if all objects are not null
   */
  public static boolean hasNull(Object... objects) {
    return !areNotNull(objects);
  }
}
