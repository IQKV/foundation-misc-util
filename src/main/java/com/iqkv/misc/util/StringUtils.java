package com.iqkv.misc.util;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for common string operations.
 * Provides static methods for string manipulation, formatting, and generation.
 *
 * @author Scaffolding Framework
 * @version 1.0
 */
@Slf4j
public final class StringUtils {

  private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final String NUMERIC_CHARS = "0123456789";
  private static final String ALPHABETIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static final Random RANDOM = new Random();

  private StringUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * Checks if a string is null or empty.
   *
   * @param str The string to check
   * @return true if the string is null or empty, false otherwise
   */
  public static boolean isEmpty(String str) {
    return str == null || str.isEmpty();
  }

  /**
   * Checks if a string is not null and not empty.
   *
   * @param str The string to check
   * @return true if the string is not null and not empty, false otherwise
   */
  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  /**
   * Checks if a string is null, empty, or contains only whitespace.
   *
   * @param str The string to check
   * @return true if the string is blank, false otherwise
   */
  public static boolean isBlank(String str) {
    return str == null || str.trim().isEmpty();
  }

  /**
   * Checks if a string is not null, not empty, and contains non-whitespace characters.
   *
   * @param str The string to check
   * @return true if the string is not blank, false otherwise
   */
  public static boolean isNotBlank(String str) {
    return !isBlank(str);
  }

  /**
   * Returns the string if it's not null, otherwise returns the default value.
   *
   * @param str          The string to check
   * @param defaultValue The default value to return if str is null
   * @return The original string or the default value
   */
  public static String defaultIfNull(String str, String defaultValue) {
    return str != null ? str : defaultValue;
  }

  /**
   * Returns the string if it's not blank, otherwise returns the default value.
   *
   * @param str          The string to check
   * @param defaultValue The default value to return if str is blank
   * @return The original string or the default value
   */
  public static String defaultIfBlank(String str, String defaultValue) {
    return isNotBlank(str) ? str : defaultValue;
  }

  /**
   * Trims whitespace from both ends of a string. Returns null if input is null.
   *
   * @param str The string to trim
   * @return Trimmed string or null
   */
  public static String trimToNull(String str) {
    if (str == null) {
      return null;
    }
    String trimmed = str.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  /**
   * Trims whitespace from both ends of a string. Returns empty string if input is null.
   *
   * @param str The string to trim
   * @return Trimmed string or empty string
   */
  public static String trimToEmpty(String str) {
    return str == null ? "" : str.trim();
  }

  /**
   * Capitalizes the first character of a string.
   *
   * @param str The string to capitalize
   * @return Capitalized string or null if input is null
   */
  public static String capitalize(String str) {
    if (isEmpty(str)) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }

  /**
   * Converts a string to camelCase.
   *
   * @param str The string to convert
   * @return camelCase string
   */
  public static String toCamelCase(String str) {
    if (isBlank(str)) {
      return str;
    }

    String[] words = str.split("[\\s_-]+");
    if (words.length == 0) {
      return str;
    }

    StringBuilder result = new StringBuilder(words[0].toLowerCase());
    for (int i = 1; i < words.length; i++) {
      result.append(capitalize(words[i]));
    }
    return result.toString();
  }

  /**
   * Converts a string to PascalCase.
   *
   * @param str The string to convert
   * @return PascalCase string
   */
  public static String toPascalCase(String str) {
    if (isBlank(str)) {
      return str;
    }

    String[] words = str.split("[\\s_-]+");
    return Arrays.stream(words).map(StringUtils::capitalize).collect(Collectors.joining());
  }

  /**
   * Converts a string to snake_case.
   *
   * @param str The string to convert
   * @return snake_case string
   */
  public static String toSnakeCase(String str) {
    if (isBlank(str)) {
      return str;
    }

    return str.replaceAll("([a-z])([A-Z])", "$1_$2").replaceAll("[\\s-]+", "_").toLowerCase();
  }

  /**
   * Converts a string to kebab-case.
   *
   * @param str The string to convert
   * @return kebab-case string
   */
  public static String toKebabCase(String str) {
    if (isBlank(str)) {
      return str;
    }

    return str.replaceAll("([a-z])([A-Z])", "$1-$2").replaceAll("[\\s_]+", "-").toLowerCase();
  }

  /**
   * Truncates a string to the specified length, adding ellipsis if truncated.
   *
   * @param str    The string to truncate
   * @param length The maximum length
   * @return Truncated string with ellipsis if needed
   */
  public static String truncate(String str, int length) {
    if (str == null || length < 0) {
      return str;
    }

    if (str.length() <= length) {
      return str;
    }

    if (length <= 3) {
      return str.substring(0, length);
    }

    return str.substring(0, length - 3) + "...";
  }

  /**
   * Masks part of a string with asterisks, keeping visible characters at start and end.
   *
   * @param str           The string to mask
   * @param visibleStart  Number of characters to keep visible at the start
   * @param visibleEnd    Number of characters to keep visible at the end
   * @param maskChar      Character to use for masking
   * @return Masked string
   */
  public static String mask(String str, int visibleStart, int visibleEnd, char maskChar) {
    if (isEmpty(str)) {
      return str;
    }

    int length = str.length();
    if (length <= visibleStart + visibleEnd) {
      return str; // String too short to mask
    }

    StringBuilder masked = new StringBuilder();
    masked.append(str, 0, visibleStart);

    int maskLength = length - visibleStart - visibleEnd;
    for (int i = 0; i < maskLength; i++) {
      masked.append(maskChar);
    }

    masked.append(str, length - visibleEnd, length);
    return masked.toString();
  }

  /**
   * Masks an email address, keeping first character and domain visible.
   *
   * @param email The email to mask
   * @return Masked email address
   */
  public static String maskEmail(String email) {
    if (isEmpty(email) || !email.contains("@")) {
      return email;
    }

    String[] parts = email.split("@");
    if (parts.length != 2) {
      return email;
    }

    String localPart = parts[0];
    String domain = parts[1];

    if (localPart.length() <= 1) {
      return email;
    }

    String maskedLocal = localPart.charAt(0) + "*".repeat(Math.max(1, localPart.length() - 1));
    return maskedLocal + "@" + domain;
  }

  /**
   * Generates a random alphanumeric string of specified length.
   *
   * @param length The length of the string to generate
   * @return Random alphanumeric string
   */
  public static String randomAlphanumeric(int length) {
    return randomString(length, ALPHANUMERIC_CHARS);
  }

  /**
   * Generates a random numeric string of specified length.
   *
   * @param length The length of the string to generate
   * @return Random numeric string
   */
  public static String randomNumeric(int length) {
    return randomString(length, NUMERIC_CHARS);
  }

  /**
   * Generates a random alphabetic string of specified length.
   *
   * @param length The length of the string to generate
   * @return Random alphabetic string
   */
  public static String randomAlphabetic(int length) {
    return randomString(length, ALPHABETIC_CHARS);
  }

  /**
   * Generates a random string from specified characters.
   *
   * @param length     The length of the string to generate
   * @param characters The characters to choose from
   * @return Random string
   */
  public static String randomString(int length, String characters) {
    if (length <= 0 || isEmpty(characters)) {
      return "";
    }

    StringBuilder result = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      result.append(characters.charAt(RANDOM.nextInt(characters.length())));
    }
    return result.toString();
  }

  /**
   * Joins a collection of strings with a delimiter.
   *
   * @param delimiter The delimiter to use
   * @param elements  The elements to join
   * @return Joined string
   */
  public static String join(String delimiter, Collection<String> elements) {
    if (elements == null || elements.isEmpty()) {
      return "";
    }
    return String.join(delimiter, elements);
  }

  /**
   * Joins an array of strings with a delimiter.
   *
   * @param delimiter The delimiter to use
   * @param elements  The elements to join
   * @return Joined string
   */
  public static String join(String delimiter, String... elements) {
    if (elements == null || elements.length == 0) {
      return "";
    }
    return String.join(delimiter, elements);
  }

  /**
   * Removes diacritics (accents) from a string.
   *
   * @param str The string to process
   * @return String without diacritics
   */
  public static String removeDiacritics(String str) {
    if (isEmpty(str)) {
      return str;
    }

    String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
    return normalized.replaceAll("\\p{M}", "");
  }

  /**
   * Creates a URL-friendly slug from a string.
   *
   * @param str The string to convert to slug
   * @return URL-friendly slug
   */
  public static String toSlug(String str) {
    if (isBlank(str)) {
      return "";
    }

    return removeDiacritics(str).toLowerCase().replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-").replaceAll("-+", "-").replaceAll("^-|-$", "");
  }

  /**
   * Counts the occurrences of a substring in a string.
   *
   * @param str    The string to search in
   * @param substr The substring to count
   * @return Number of occurrences
   */
  public static int countOccurrences(String str, String substr) {
    if (isEmpty(str) || isEmpty(substr)) {
      return 0;
    }

    int count = 0;
    int index = 0;
    while ((index = str.indexOf(substr, index)) != -1) {
      count++;
      index += substr.length();
    }
    return count;
  }

  /**
   * Reverses a string.
   *
   * @param str The string to reverse
   * @return Reversed string
   */
  public static String reverse(String str) {
    if (isEmpty(str)) {
      return str;
    }
    return new StringBuilder(str).reverse().toString();
  }

  /**
   * Checks if a string is a palindrome (reads the same forwards and backwards).
   *
   * @param str The string to check
   * @return true if the string is a palindrome, false otherwise
   */
  public static boolean isPalindrome(String str) {
    if (isEmpty(str)) {
      return true;
    }

    String cleaned = str.toLowerCase().replaceAll("\\s", "");
    return cleaned.equals(reverse(cleaned));
  }
}
