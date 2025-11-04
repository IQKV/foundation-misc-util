package com.iqkv.misc.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DateTimeUtils Tests")
class DateTimeUtilsTest {

  @Test
  @DisplayName("Should not allow instantiation")
  void shouldNotAllowInstantiation() {
    assertThatThrownBy(() -> {
      var constructor = DateTimeUtils.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      constructor.newInstance();
    }).hasCauseInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  @DisplayName("Should get current LocalDateTime")
  void shouldGetCurrentLocalDateTime() {
    LocalDateTime before = LocalDateTime.now();
    LocalDateTime result = DateTimeUtils.now();
    LocalDateTime after = LocalDateTime.now();

    assertThat(result).isBetween(before, after);
  }

  @Test
  @DisplayName("Should get current LocalDateTime in UTC")
  void shouldGetCurrentLocalDateTimeInUtc() {
    LocalDateTime before = LocalDateTime.now(DateTimeUtils.UTC);
    LocalDateTime result = DateTimeUtils.nowUtc();
    LocalDateTime after = LocalDateTime.now(DateTimeUtils.UTC);

    assertThat(result).isBetween(before, after);
  }

  @Test
  @DisplayName("Should get current ZonedDateTime in UTC")
  void shouldGetCurrentZonedDateTimeInUtc() {
    ZonedDateTime before = ZonedDateTime.now(DateTimeUtils.UTC);
    ZonedDateTime result = DateTimeUtils.nowZonedUtc();
    ZonedDateTime after = ZonedDateTime.now(DateTimeUtils.UTC);

    assertThat(result).isBetween(before, after);
    assertThat(result.getZone()).isEqualTo(DateTimeUtils.UTC);
  }

  @Test
  @DisplayName("Should convert LocalDateTime to UTC ZonedDateTime")
  void shouldConvertLocalDateTimeToUtcZonedDateTime() {
    LocalDateTime localDateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45);

    ZonedDateTime result = DateTimeUtils.toUtcZoned(localDateTime);

    assertThat(result).isNotNull();
    assertThat(result.getZone()).isEqualTo(DateTimeUtils.UTC);
    // assertThat(result.toLocalDateTime()).isNotEqualTo(localDateTime); // Should be different due to timezone conversion
  }

  @Test
  @DisplayName("Should handle null LocalDateTime in UTC conversion")
  void shouldHandleNullLocalDateTimeInUtcConversion() {
    ZonedDateTime result = DateTimeUtils.toUtcZoned(null);
    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should convert LocalDateTime to specified zone")
  void shouldConvertLocalDateTimeToSpecifiedZone() {
    LocalDateTime localDateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45);
    ZoneId targetZone = ZoneId.of("America/New_York");

    ZonedDateTime result = DateTimeUtils.toZoned(localDateTime, targetZone);

    assertThat(result).isNotNull();
    assertThat(result.getZone()).isEqualTo(targetZone);
  }

  @Test
  @DisplayName("Should handle null parameters in zone conversion")
  void shouldHandleNullParametersInZoneConversion() {
    LocalDateTime localDateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45);
    ZoneId targetZone = ZoneId.of("America/New_York");

    assertThat(DateTimeUtils.toZoned(null, targetZone)).isNull();
    assertThat(DateTimeUtils.toZoned(localDateTime, null)).isNull();
    assertThat(DateTimeUtils.toZoned(null, null)).isNull();
  }

  @Test
  @DisplayName("Should convert ZonedDateTime to LocalDateTime")
  void shouldConvertZonedDateTimeToLocalDateTime() {
    ZonedDateTime zonedDateTime = ZonedDateTime.of(2023, 10, 12, 15, 30, 45, 0, DateTimeUtils.UTC);

    LocalDateTime result = DateTimeUtils.toLocal(zonedDateTime);

    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(LocalDateTime.of(2023, 10, 12, 15, 30, 45));
  }

  @Test
  @DisplayName("Should handle null ZonedDateTime in local conversion")
  void shouldHandleNullZonedDateTimeInLocalConversion() {
    LocalDateTime result = DateTimeUtils.toLocal(null);
    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should convert Date to LocalDateTime")
  void shouldConvertDateToLocalDateTime() {
    Date date = new Date();

    LocalDateTime result = DateTimeUtils.fromDate(date);

    assertThat(result).isNotNull();
    // Allow some tolerance for execution time
    assertThat(result).isCloseTo(LocalDateTime.now(), within(Duration.ofSeconds(1)));
  }

  @Test
  @DisplayName("Should handle null Date in conversion")
  void shouldHandleNullDateInConversion() {
    LocalDateTime result = DateTimeUtils.fromDate(null);
    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should convert LocalDateTime to Date")
  void shouldConvertLocalDateTimeToDate() {
    LocalDateTime localDateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45);

    Date result = DateTimeUtils.toDate(localDateTime);

    assertThat(result).isNotNull();
    // Convert back to verify
    LocalDateTime converted = DateTimeUtils.fromDate(result);
    assertThat(converted).isEqualTo(localDateTime);
  }

  @Test
  @DisplayName("Should handle null LocalDateTime in Date conversion")
  void shouldHandleNullLocalDateTimeInDateConversion() {
    Date result = DateTimeUtils.toDate(null);
    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should format LocalDateTime with specified formatter")
  void shouldFormatLocalDateTimeWithSpecifiedFormatter() {
    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45);

    String result = DateTimeUtils.format(dateTime, DateTimeUtils.SIMPLE_DATE);

    assertThat(result).isEqualTo("2023-10-12");
  }

  @Test
  @DisplayName("Should handle null parameters in formatting")
  void shouldHandleNullParametersInFormatting() {
    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45);

    assertThat(DateTimeUtils.format(null, DateTimeUtils.SIMPLE_DATE)).isNull();
    assertThat(DateTimeUtils.format(dateTime, null)).isNull();
    assertThat(DateTimeUtils.format(null, null)).isNull();
  }

  @Test
  @DisplayName("Should format for API responses")
  void shouldFormatForApiResponses() {
    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45, 123000000);

    String result = DateTimeUtils.formatForApi(dateTime);

    assertThat(result).isEqualTo("2023-10-12T15:30:45.123Z");
  }

  @Test
  @DisplayName("Should format for display")
  void shouldFormatForDisplay() {
    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45);

    String result = DateTimeUtils.formatForDisplay(dateTime);

    assertThat(result).isEqualTo("12/10/2023 15:30:45");
  }

  @Test
  @DisplayName("Should parse date/time string with specified formatter")
  void shouldParseDateTimeStringWithSpecifiedFormatter() {
    String dateTimeString = "2023-10-12T15:30:45";

    LocalDateTime result = DateTimeUtils.parse(dateTimeString, DateTimeUtils.ISO_LOCAL_DATE_TIME);

    assertThat(result).isEqualTo(LocalDateTime.of(2023, 10, 12, 15, 30, 45));
  }

  @Test
  @DisplayName("Should handle invalid date/time strings in parsing")
  void shouldHandleInvalidDateTimeStringsInParsing() {
    assertThat(DateTimeUtils.parse("invalid-date", DateTimeUtils.SIMPLE_DATE)).isNull();
    assertThat(DateTimeUtils.parse(null, DateTimeUtils.SIMPLE_DATE)).isNull();
    assertThat(DateTimeUtils.parse("", DateTimeUtils.SIMPLE_DATE)).isNull();
    assertThat(DateTimeUtils.parse("  ", DateTimeUtils.SIMPLE_DATE)).isNull();
    assertThat(DateTimeUtils.parse("2023-10-12", null)).isNull();
  }

  @Test
  @DisplayName("Should parse API date/time string")
  void shouldParseApiDateTimeString() {
    String apiDateTime = "2023-10-12T15:30:45.123Z";

    LocalDateTime result = DateTimeUtils.parseFromApi(apiDateTime);

    assertThat(result).isEqualTo(LocalDateTime.of(2023, 10, 12, 15, 30, 45, 123000000));
  }

  @Test
  @DisplayName("Should calculate days between dates")
  void shouldCalculateDaysBetweenDates() {
    LocalDateTime start = LocalDateTime.of(2023, 10, 12, 10, 0, 0);
    LocalDateTime end = LocalDateTime.of(2023, 10, 15, 20, 0, 0);

    long result = DateTimeUtils.daysBetween(start, end);

    assertThat(result).isEqualTo(3);
  }

  @Test
  @DisplayName("Should handle null dates in days calculation")
  void shouldHandleNullDatesInDaysCalculation() {
    LocalDateTime dateTime = LocalDateTime.now();

    assertThat(DateTimeUtils.daysBetween(null, dateTime)).isZero();
    assertThat(DateTimeUtils.daysBetween(dateTime, null)).isZero();
    assertThat(DateTimeUtils.daysBetween(null, null)).isZero();
  }

  @Test
  @DisplayName("Should calculate hours between dates")
  void shouldCalculateHoursBetweenDates() {
    LocalDateTime start = LocalDateTime.of(2023, 10, 12, 10, 0, 0);
    LocalDateTime end = LocalDateTime.of(2023, 10, 12, 15, 0, 0);

    long result = DateTimeUtils.hoursBetween(start, end);

    assertThat(result).isEqualTo(5);
  }

  @Test
  @DisplayName("Should calculate minutes between dates")
  void shouldCalculateMinutesBetweenDates() {
    LocalDateTime start = LocalDateTime.of(2023, 10, 12, 10, 0, 0);
    LocalDateTime end = LocalDateTime.of(2023, 10, 12, 10, 30, 0);

    long result = DateTimeUtils.minutesBetween(start, end);

    assertThat(result).isEqualTo(30);
  }

  @Test
  @DisplayName("Should check if date is before another date")
  void shouldCheckIfDateIsBeforeAnotherDate() {
    LocalDateTime earlier = LocalDateTime.of(2023, 10, 12, 10, 0, 0);
    LocalDateTime later = LocalDateTime.of(2023, 10, 12, 15, 0, 0);

    assertThat(DateTimeUtils.isBefore(earlier, later)).isTrue();
    assertThat(DateTimeUtils.isBefore(later, earlier)).isFalse();
    assertThat(DateTimeUtils.isBefore(earlier, earlier)).isFalse();
    assertThat(DateTimeUtils.isBefore(null, later)).isFalse();
    assertThat(DateTimeUtils.isBefore(earlier, null)).isFalse();
  }

  @Test
  @DisplayName("Should check if date is after another date")
  void shouldCheckIfDateIsAfterAnotherDate() {
    LocalDateTime earlier = LocalDateTime.of(2023, 10, 12, 10, 0, 0);
    LocalDateTime later = LocalDateTime.of(2023, 10, 12, 15, 0, 0);

    assertThat(DateTimeUtils.isAfter(later, earlier)).isTrue();
    assertThat(DateTimeUtils.isAfter(earlier, later)).isFalse();
    assertThat(DateTimeUtils.isAfter(earlier, earlier)).isFalse();
    assertThat(DateTimeUtils.isAfter(null, earlier)).isFalse();
    assertThat(DateTimeUtils.isAfter(later, null)).isFalse();
  }

  @Test
  @DisplayName("Should check if date is within range")
  void shouldCheckIfDateIsWithinRange() {
    LocalDateTime start = LocalDateTime.of(2023, 10, 12, 10, 0, 0);
    LocalDateTime middle = LocalDateTime.of(2023, 10, 12, 12, 0, 0);
    LocalDateTime end = LocalDateTime.of(2023, 10, 12, 15, 0, 0);
    LocalDateTime outside = LocalDateTime.of(2023, 10, 12, 20, 0, 0);

    assertThat(DateTimeUtils.isWithinRange(middle, start, end)).isTrue();
    assertThat(DateTimeUtils.isWithinRange(start, start, end)).isTrue();
    assertThat(DateTimeUtils.isWithinRange(end, start, end)).isTrue();
    assertThat(DateTimeUtils.isWithinRange(outside, start, end)).isFalse();
    assertThat(DateTimeUtils.isWithinRange(null, start, end)).isFalse();
    assertThat(DateTimeUtils.isWithinRange(middle, null, end)).isFalse();
    assertThat(DateTimeUtils.isWithinRange(middle, start, null)).isFalse();
  }

  @Test
  @DisplayName("Should get start of day")
  void shouldGetStartOfDay() {
    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45);

    LocalDateTime result = DateTimeUtils.startOfDay(dateTime);

    assertThat(result).isEqualTo(LocalDateTime.of(2023, 10, 12, 0, 0, 0));
    assertThat(DateTimeUtils.startOfDay(null)).isNull();
  }

  @Test
  @DisplayName("Should get end of day")
  void shouldGetEndOfDay() {
    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45);

    LocalDateTime result = DateTimeUtils.endOfDay(dateTime);

    assertThat(result).isEqualTo(LocalDateTime.of(2023, 10, 12, 23, 59, 59, 999999999));
    assertThat(DateTimeUtils.endOfDay(null)).isNull();
  }

  @Test
  @DisplayName("Should get start of week")
  void shouldGetStartOfWeek() {
    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45); // Thursday

    LocalDateTime result = DateTimeUtils.startOfWeek(dateTime);

    assertThat(result).isEqualTo(LocalDateTime.of(2023, 10, 9, 0, 0, 0)); // Monday
    assertThat(DateTimeUtils.startOfWeek(null)).isNull();
  }

  @Test
  @DisplayName("Should get end of week")
  void shouldGetEndOfWeek() {
    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45); // Thursday

    LocalDateTime result = DateTimeUtils.endOfWeek(dateTime);

    assertThat(result).isEqualTo(LocalDateTime.of(2023, 10, 15, 23, 59, 59, 999999999)); // Sunday
    assertThat(DateTimeUtils.endOfWeek(null)).isNull();
  }

  @Test
  @DisplayName("Should get start of month")
  void shouldGetStartOfMonth() {
    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45);

    LocalDateTime result = DateTimeUtils.startOfMonth(dateTime);

    assertThat(result).isEqualTo(LocalDateTime.of(2023, 10, 1, 0, 0, 0));
    assertThat(DateTimeUtils.startOfMonth(null)).isNull();
  }

  @Test
  @DisplayName("Should get end of month")
  void shouldGetEndOfMonth() {
    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45);

    LocalDateTime result = DateTimeUtils.endOfMonth(dateTime);

    assertThat(result).isEqualTo(LocalDateTime.of(2023, 10, 31, 23, 59, 59, 999999999));
    assertThat(DateTimeUtils.endOfMonth(null)).isNull();
  }

  @Test
  @DisplayName("Should check if date is today")
  void shouldCheckIfDateIsToday() {
    LocalDateTime today = LocalDateTime.now();
    LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

    assertThat(DateTimeUtils.isToday(today)).isTrue();
    assertThat(DateTimeUtils.isToday(yesterday)).isFalse();
    assertThat(DateTimeUtils.isToday(null)).isFalse();
  }

  @Test
  @DisplayName("Should check if date is yesterday")
  void shouldCheckIfDateIsYesterday() {
    LocalDateTime today = LocalDateTime.now();
    LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
    LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);

    assertThat(DateTimeUtils.isYesterday(yesterday)).isTrue();
    assertThat(DateTimeUtils.isYesterday(today)).isFalse();
    assertThat(DateTimeUtils.isYesterday(twoDaysAgo)).isFalse();
    assertThat(DateTimeUtils.isYesterday(null)).isFalse();
  }

  @Test
  @DisplayName("Should check if date is tomorrow")
  void shouldCheckIfDateIsTomorrow() {
    LocalDateTime today = LocalDateTime.now();
    LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
    LocalDateTime twoDaysLater = LocalDateTime.now().plusDays(2);

    assertThat(DateTimeUtils.isTomorrow(tomorrow)).isTrue();
    assertThat(DateTimeUtils.isTomorrow(today)).isFalse();
    assertThat(DateTimeUtils.isTomorrow(twoDaysLater)).isFalse();
    assertThat(DateTimeUtils.isTomorrow(null)).isFalse();
  }

  @Test
  @DisplayName("Should have correct predefined formatters")
  void shouldHaveCorrectPredefinedFormatters() {
    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 12, 15, 30, 45, 123000000);

    assertThat(DateTimeUtils.ISO_LOCAL_DATE_TIME.format(dateTime)).isEqualTo("2023-10-12T15:30:45");
    assertThat(DateTimeUtils.SIMPLE_DATE.format(dateTime.toLocalDate())).isEqualTo("2023-10-12");
    assertThat(DateTimeUtils.SIMPLE_TIME.format(dateTime.toLocalTime())).isEqualTo("15:30:45");
    assertThat(DateTimeUtils.DISPLAY_DATE_TIME.format(dateTime)).isEqualTo("12/10/2023 15:30:45");
    assertThat(DateTimeUtils.DISPLAY_DATE.format(dateTime.toLocalDate())).isEqualTo("12/10/2023");
    assertThat(DateTimeUtils.API_DATE_TIME.format(dateTime)).isEqualTo("2023-10-12T15:30:45.123Z");
  }

  @Test
  @DisplayName("Should have correct predefined time zones")
  void shouldHaveCorrectPredefinedTimeZones() {
    assertThat(DateTimeUtils.UTC).isEqualTo(ZoneId.of("UTC"));
    assertThat(DateTimeUtils.SYSTEM_DEFAULT).isEqualTo(ZoneId.systemDefault());
  }
}
