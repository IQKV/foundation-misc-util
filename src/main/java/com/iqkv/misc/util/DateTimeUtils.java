package com.iqkv.misc.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for common date and time operations. Provides static methods for date/time formatting, parsing, and calculations.
 */
@Slf4j
public final class DateTimeUtils {

  // Common date/time formatters
  public static final DateTimeFormatter ISO_LOCAL_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
  public static final DateTimeFormatter ISO_ZONED_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
  public static final DateTimeFormatter SIMPLE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public static final DateTimeFormatter SIMPLE_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
  public static final DateTimeFormatter DISPLAY_DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
  public static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  public static final DateTimeFormatter API_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

  // Common time zones
  public static final ZoneId UTC = ZoneId.of("UTC");
  public static final ZoneId SYSTEM_DEFAULT = ZoneId.systemDefault();

  private DateTimeUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * Gets the current LocalDateTime.
   *
   * @return Current LocalDateTime
   */
  public static LocalDateTime now() {
    return LocalDateTime.now();
  }

  /**
   * Gets the current LocalDateTime in UTC.
   *
   * @return Current LocalDateTime in UTC
   */
  public static LocalDateTime nowUtc() {
    return LocalDateTime.now(UTC);
  }

  /**
   * Gets the current ZonedDateTime in UTC.
   *
   * @return Current ZonedDateTime in UTC
   */
  public static ZonedDateTime nowZonedUtc() {
    return ZonedDateTime.now(UTC);
  }

  /**
   * Converts LocalDateTime to UTC ZonedDateTime.
   *
   * @param localDateTime The LocalDateTime to convert
   * @return ZonedDateTime in UTC
   */
  public static ZonedDateTime toUtcZoned(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    return localDateTime.atZone(SYSTEM_DEFAULT).withZoneSameInstant(UTC);
  }

  /**
   * Converts LocalDateTime to ZonedDateTime in specified zone.
   *
   * @param localDateTime The LocalDateTime to convert
   * @param zoneId        The target zone ID
   * @return ZonedDateTime in the specified zone
   */
  public static ZonedDateTime toZoned(LocalDateTime localDateTime, ZoneId zoneId) {
    if (localDateTime == null || zoneId == null) {
      return null;
    }
    return localDateTime.atZone(SYSTEM_DEFAULT).withZoneSameInstant(zoneId);
  }

  /**
   * Converts ZonedDateTime to LocalDateTime.
   *
   * @param zonedDateTime The ZonedDateTime to convert
   * @return LocalDateTime
   */
  public static LocalDateTime toLocal(ZonedDateTime zonedDateTime) {
    if (zonedDateTime == null) {
      return null;
    }
    return zonedDateTime.toLocalDateTime();
  }

  /**
   * Converts legacy Date to LocalDateTime.
   *
   * @param date The Date to convert
   * @return LocalDateTime
   */
  public static LocalDateTime fromDate(Date date) {
    if (date == null) {
      return null;
    }
    return date.toInstant().atZone(SYSTEM_DEFAULT).toLocalDateTime();
  }

  /**
   * Converts LocalDateTime to legacy Date.
   *
   * @param localDateTime The LocalDateTime to convert
   * @return Date
   */
  public static Date toDate(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    return Date.from(localDateTime.atZone(SYSTEM_DEFAULT).toInstant());
  }

  /**
   * Formats LocalDateTime using the specified formatter.
   *
   * @param localDateTime The LocalDateTime to format
   * @param formatter     The DateTimeFormatter to use
   * @return Formatted date/time string
   */
  public static String format(LocalDateTime localDateTime, DateTimeFormatter formatter) {
    if (localDateTime == null || formatter == null) {
      return null;
    }
    try {
      return localDateTime.format(formatter);
    } catch (Exception e) {
      log.warn("Failed to format LocalDateTime: {}", e.getMessage());
      return null;
    }
  }

  /**
   * Formats LocalDateTime for API responses (ISO format with Z suffix).
   *
   * @param localDateTime The LocalDateTime to format
   * @return Formatted date/time string for API
   */
  public static String formatForApi(LocalDateTime localDateTime) {
    return format(localDateTime, API_DATE_TIME);
  }

  /**
   * Formats LocalDateTime for display purposes.
   *
   * @param localDateTime The LocalDateTime to format
   * @return Formatted date/time string for display
   */
  public static String formatForDisplay(LocalDateTime localDateTime) {
    return format(localDateTime, DISPLAY_DATE_TIME);
  }

  /**
   * Parses a date/time string using the specified formatter.
   *
   * @param dateTimeString The date/time string to parse
   * @param formatter      The DateTimeFormatter to use
   * @return Parsed LocalDateTime or null if parsing fails
   */
  public static LocalDateTime parse(String dateTimeString, DateTimeFormatter formatter) {
    if (ValidationUtils.isBlank(dateTimeString) || formatter == null) {
      return null;
    }
    try {
      return LocalDateTime.parse(dateTimeString.trim(), formatter);
    } catch (DateTimeParseException e) {
      log.warn("Failed to parse date/time string '{}': {}", dateTimeString, e.getMessage());
      return null;
    }
  }

  /**
   * Parses an API date/time string (ISO format).
   *
   * @param dateTimeString The date/time string to parse
   * @return Parsed LocalDateTime or null if parsing fails
   */
  public static LocalDateTime parseFromApi(String dateTimeString) {
    return parse(dateTimeString, API_DATE_TIME);
  }

  /**
   * Calculates the number of days between two LocalDateTime instances.
   *
   * @param start Start date/time
   * @param end   End date/time
   * @return Number of days between the dates
   */
  public static long daysBetween(LocalDateTime start, LocalDateTime end) {
    if (start == null || end == null) {
      return 0;
    }
    return ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate());
  }

  /**
   * Calculates the number of hours between two LocalDateTime instances.
   *
   * @param start Start date/time
   * @param end   End date/time
   * @return Number of hours between the dates
   */
  public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
    if (start == null || end == null) {
      return 0;
    }
    return ChronoUnit.HOURS.between(start, end);
  }

  /**
   * Calculates the number of minutes between two LocalDateTime instances.
   *
   * @param start Start date/time
   * @param end   End date/time
   * @return Number of minutes between the dates
   */
  public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
    if (start == null || end == null) {
      return 0;
    }
    return ChronoUnit.MINUTES.between(start, end);
  }

  /**
   * Checks if a LocalDateTime is before another LocalDateTime.
   *
   * @param dateTime  The date/time to check
   * @param reference The reference date/time
   * @return true if dateTime is before reference, false otherwise
   */
  public static boolean isBefore(LocalDateTime dateTime, LocalDateTime reference) {
    if (dateTime == null || reference == null) {
      return false;
    }
    return dateTime.isBefore(reference);
  }

  /**
   * Checks if a LocalDateTime is after another LocalDateTime.
   *
   * @param dateTime  The date/time to check
   * @param reference The reference date/time
   * @return true if dateTime is after reference, false otherwise
   */
  public static boolean isAfter(LocalDateTime dateTime, LocalDateTime reference) {
    if (dateTime == null || reference == null) {
      return false;
    }
    return dateTime.isAfter(reference);
  }

  /**
   * Checks if a LocalDateTime is within a specified range.
   *
   * @param dateTime The date/time to check
   * @param start    The start of the range (inclusive)
   * @param end      The end of the range (inclusive)
   * @return true if dateTime is within the range, false otherwise
   */
  public static boolean isWithinRange(LocalDateTime dateTime, LocalDateTime start, LocalDateTime end) {
    if (dateTime == null || start == null || end == null) {
      return false;
    }
    return (dateTime.isEqual(start) || dateTime.isAfter(start)) && (dateTime.isEqual(end) || dateTime.isBefore(end));
  }

  /**
   * Gets the start of the day for a given LocalDateTime.
   *
   * @param dateTime The LocalDateTime
   * @return LocalDateTime representing the start of the day
   */
  public static LocalDateTime startOfDay(LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return dateTime.toLocalDate().atStartOfDay();
  }

  /**
   * Gets the end of the day for a given LocalDateTime.
   *
   * @param dateTime The LocalDateTime
   * @return LocalDateTime representing the end of the day
   */
  public static LocalDateTime endOfDay(LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return dateTime.toLocalDate().atTime(LocalTime.MAX);
  }

  /**
   * Gets the start of the week for a given LocalDateTime.
   *
   * @param dateTime The LocalDateTime
   * @return LocalDateTime representing the start of the week (Monday)
   */
  public static LocalDateTime startOfWeek(LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return dateTime.toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay();
  }

  /**
   * Gets the end of the week for a given LocalDateTime.
   *
   * @param dateTime The LocalDateTime
   * @return LocalDateTime representing the end of the week (Sunday)
   */
  public static LocalDateTime endOfWeek(LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return dateTime.toLocalDate().with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX);
  }

  /**
   * Gets the start of the month for a given LocalDateTime.
   *
   * @param dateTime The LocalDateTime
   * @return LocalDateTime representing the start of the month
   */
  public static LocalDateTime startOfMonth(LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return dateTime.toLocalDate().withDayOfMonth(1).atStartOfDay();
  }

  /**
   * Gets the end of the month for a given LocalDateTime.
   *
   * @param dateTime The LocalDateTime
   * @return LocalDateTime representing the end of the month
   */
  public static LocalDateTime endOfMonth(LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return dateTime.toLocalDate().withDayOfMonth(dateTime.toLocalDate().lengthOfMonth()).atTime(LocalTime.MAX);
  }

  /**
   * Checks if a LocalDateTime represents today.
   *
   * @param dateTime The LocalDateTime to check
   * @return true if the dateTime is today, false otherwise
   */
  public static boolean isToday(LocalDateTime dateTime) {
    if (dateTime == null) {
      return false;
    }
    return dateTime.toLocalDate().isEqual(LocalDate.now());
  }

  /**
   * Checks if a LocalDateTime represents yesterday.
   *
   * @param dateTime The LocalDateTime to check
   * @return true if the dateTime is yesterday, false otherwise
   */
  public static boolean isYesterday(LocalDateTime dateTime) {
    if (dateTime == null) {
      return false;
    }
    return dateTime.toLocalDate().isEqual(LocalDate.now().minusDays(1));
  }

  /**
   * Checks if a LocalDateTime represents tomorrow.
   *
   * @param dateTime The LocalDateTime to check
   * @return true if the dateTime is tomorrow, false otherwise
   */
  public static boolean isTomorrow(LocalDateTime dateTime) {
    if (dateTime == null) {
      return false;
    }
    return dateTime.toLocalDate().isEqual(LocalDate.now().plusDays(1));
  }
}
