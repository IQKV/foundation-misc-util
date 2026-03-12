package com.iqkv.foundation.misc.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ValidationUtils Tests")
class ValidationUtilsTest {

  @Test
  @DisplayName("Should not allow instantiation")
  void shouldNotAllowInstantiation() {
    assertThatThrownBy(() -> {
      var constructor = ValidationUtils.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      constructor.newInstance();
    }).hasCauseInstanceOf(UnsupportedOperationException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "  ", "\t", "\n", "\r\n"})
  @DisplayName("Should identify blank strings")
  void shouldIdentifyBlankStrings(String input) {
    assertThat(ValidationUtils.isBlank(input)).isTrue();
    assertThat(ValidationUtils.isNotBlank(input)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"hello", "a", "test with spaces", " test "})
  @DisplayName("Should identify non-blank strings")
  void shouldIdentifyNonBlankStrings(String input) {
    assertThat(ValidationUtils.isBlank(input)).isFalse();
    assertThat(ValidationUtils.isNotBlank(input)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"test@example.com", "user.name@domain.co.uk", "user+tag@example.org", "user123@test-domain.com", "a@b.co"})
  @DisplayName("Should validate correct email formats")
  void shouldValidateCorrectEmailFormats(String email) {
    assertThat(ValidationUtils.isValidEmail(email)).isTrue();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"invalid-email", "@example.com", "test@", "test.example.com", "test@.com", "test@com"})
  @DisplayName("Should reject invalid email formats")
  void shouldRejectInvalidEmailFormats(String email) {
    assertThat(ValidationUtils.isValidEmail(email)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"+1234567890", "1234567890", "+44123456789", "447123456789", "+33123456789"})
  @DisplayName("Should validate correct phone number formats")
  void shouldValidateCorrectPhoneNumberFormats(String phoneNumber) {
    assertThat(ValidationUtils.isValidPhoneNumber(phoneNumber)).isTrue();
  }

  @Test
  @DisplayName("Should reject null and empty phone numbers")
  void shouldRejectNullAndEmptyPhoneNumbers() {
    assertThat(ValidationUtils.isValidPhoneNumber(null)).isFalse();
    assertThat(ValidationUtils.isValidPhoneNumber("")).isFalse();
    assertThat(ValidationUtils.isValidPhoneNumber("   ")).isFalse();
  }

  @Test
  @DisplayName("Should clean phone numbers before validation")
  void shouldCleanPhoneNumbersBeforeValidation() {
    assertThat(ValidationUtils.isValidPhoneNumber("+1 (234) 567-8900")).isTrue();
    assertThat(ValidationUtils.isValidPhoneNumber("1-234-567-8900")).isTrue();
    assertThat(ValidationUtils.isValidPhoneNumber("1 234 567 8900")).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"550e8400-e29b-41d4-a716-446655440000", "6ba7b810-9dad-11d1-80b4-00c04fd430c8", "6ba7b811-9dad-11d1-80b4-00c04fd430c8", "00000000-0000-0000-0000-000000000000"})
  @DisplayName("Should validate correct UUID formats")
  void shouldValidateCorrectUuidFormats(String uuid) {
    assertThat(ValidationUtils.isValidUUID(uuid)).isTrue();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(
      strings = {
          "550e8400-e29b-41d4-a716-44665544000", // Too short
          "550e8400-e29b-41d4-a716-4466554400000", // Too long
          "550e8400e29b41d4a716446655440000", // No hyphens
          "550e8400-e29b-41d4-a716-44665544000g", // Invalid character
          "550e8400-e29b-41d4-a716", // Incomplete
          "not-a-uuid-at-all",
      }
  )
  @DisplayName("Should reject invalid UUID formats")
  void shouldRejectInvalidUuidFormats(String uuid) {
    assertThat(ValidationUtils.isValidUUID(uuid)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
          "https://example.com",
          "http://example.com",
          "https://www.example.com",
          "https://subdomain.example.com",
          "https://example.com/path",
          "https://example.com/path?query=value",
          "https://example.com:8080/path",
      }
  )
  @DisplayName("Should validate correct URL formats")
  void shouldValidateCorrectUrlFormats(String url) {
    assertThat(ValidationUtils.isValidURL(url)).isTrue();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(
      strings = {
          "ftp://example.com", // Wrong protocol
          "example.com", // No protocol
          "https://", // Incomplete
          "https://.com", // Invalid domain
          "https://example", // No TLD
          "not-a-url",
      }
  )
  @DisplayName("Should reject invalid URL formats")
  void shouldRejectInvalidUrlFormats(String url) {
    assertThat(ValidationUtils.isValidURL(url)).isFalse();
  }

  @Test
  @DisplayName("Should validate collection emptiness correctly")
  void shouldValidateCollectionEmptinessCorrectly() {
    assertThat(ValidationUtils.isEmpty((List<String>) null)).isTrue();
    assertThat(ValidationUtils.isEmpty(Collections.emptyList())).isTrue();
    assertThat(ValidationUtils.isEmpty(List.of("item"))).isFalse();

    assertThat(ValidationUtils.isNotEmpty((List<String>) null)).isFalse();
    assertThat(ValidationUtils.isNotEmpty(Collections.emptyList())).isFalse();
    assertThat(ValidationUtils.isNotEmpty(List.of("item"))).isTrue();
  }

  @Test
  @DisplayName("Should validate string length correctly")
  void shouldValidateStringLengthCorrectly() {
    assertThat(ValidationUtils.isValidLength(null, 0, 10)).isTrue();
    assertThat(ValidationUtils.isValidLength(null, 1, 10)).isFalse();
    assertThat(ValidationUtils.isValidLength("", 0, 10)).isTrue();
    assertThat(ValidationUtils.isValidLength("", 1, 10)).isFalse();
    assertThat(ValidationUtils.isValidLength("hello", 3, 10)).isTrue();
    assertThat(ValidationUtils.isValidLength("hello", 6, 10)).isFalse();
    assertThat(ValidationUtils.isValidLength("hello", 1, 4)).isFalse();
    assertThat(ValidationUtils.isValidLength("hello", 5, 5)).isTrue();
  }

  @Test
  @DisplayName("Should validate number ranges correctly")
  void shouldValidateNumberRangesCorrectly() {
    assertThat(ValidationUtils.isInRange(null, 1, 10)).isFalse();
    assertThat(ValidationUtils.isInRange(5, 1, 10)).isTrue();
    assertThat(ValidationUtils.isInRange(1, 1, 10)).isTrue();
    assertThat(ValidationUtils.isInRange(10, 1, 10)).isTrue();
    assertThat(ValidationUtils.isInRange(0, 1, 10)).isFalse();
    assertThat(ValidationUtils.isInRange(11, 1, 10)).isFalse();
    assertThat(ValidationUtils.isInRange(5.5, 1, 10)).isTrue();
    assertThat(ValidationUtils.isInRange(5.5f, 1, 10)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"abc123", "ABC123", "test123TEST", "123abc"})
  @DisplayName("Should validate alphanumeric strings")
  void shouldValidateAlphanumericStrings(String input) {
    assertThat(ValidationUtils.isAlphanumeric(input)).isTrue();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "abc 123", "abc-123", "abc_123", "abc@123", "!"})
  @DisplayName("Should reject non-alphanumeric strings")
  void shouldRejectNonAlphanumericStrings(String input) {
    assertThat(ValidationUtils.isAlphanumeric(input)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"abc", "ABC", "abcDEF", "test"})
  @DisplayName("Should validate alphabetic strings")
  void shouldValidateAlphabeticStrings(String input) {
    assertThat(ValidationUtils.isAlphabetic(input)).isTrue();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "abc123", "abc ", "abc-def", "123"})
  @DisplayName("Should reject non-alphabetic strings")
  void shouldRejectNonAlphabeticStrings(String input) {
    assertThat(ValidationUtils.isAlphabetic(input)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"123", "0", "999", "1234567890"})
  @DisplayName("Should validate numeric strings")
  void shouldValidateNumericStrings(String input) {
    assertThat(ValidationUtils.isNumeric(input)).isTrue();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "123abc", "12.34", "123 ", "-123", "+123"})
  @DisplayName("Should reject non-numeric strings")
  void shouldRejectNonNumericStrings(String input) {
    assertThat(ValidationUtils.isNumeric(input)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"Password123!", "MyStr0ng@Pass", "C0mplex#Password", "Secure$Pass1"})
  @DisplayName("Should validate strong passwords")
  void shouldValidateStrongPasswords(String password) {
    assertThat(ValidationUtils.isStrongPassword(password)).isTrue();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(
      strings = {
          "short", // Too short
          "password", // No uppercase, no digit, no special char
          "PASSWORD", // No lowercase, no digit, no special char
          "Password", // No digit, no special char
          "Password123", // No special char
          "password123!", // No uppercase
          "PASSWORD123!", // No lowercase
          "Password!", // No digit
      }
  )
  @DisplayName("Should reject weak passwords")
  void shouldRejectWeakPasswords(String password) {
    assertThat(ValidationUtils.isStrongPassword(password)).isFalse();
  }

  @Test
  @DisplayName("Should sanitize input correctly")
  void shouldSanitizeInputCorrectly() {
    assertThat(ValidationUtils.sanitizeInput(null)).isNull();
    assertThat(ValidationUtils.sanitizeInput("")).isEqualTo("");
    assertThat(ValidationUtils.sanitizeInput("  ")).isEqualTo("  ");
    assertThat(ValidationUtils.sanitizeInput("normal text")).isEqualTo("normal text");
    assertThat(ValidationUtils.sanitizeInput("<script>alert('xss')</script>")).isEqualTo("&lt;script&gt;alert(&#x27;xss&#x27;)&lt;&#x2F;script&gt;");
    assertThat(ValidationUtils.sanitizeInput("\"quoted\" text")).isEqualTo("&quot;quoted&quot; text");
    assertThat(ValidationUtils.sanitizeInput("path/to/file")).isEqualTo("path&#x2F;to&#x2F;file");
    assertThat(ValidationUtils.sanitizeInput("back\\slash")).isEqualTo("back&#x5C;slash");
  }

  @Test
  @DisplayName("Should validate all objects are not null")
  void shouldValidateAllObjectsAreNotNull() {
    assertThat(ValidationUtils.areNotNull()).isTrue(); // No arguments
    assertThat(ValidationUtils.areNotNull("test")).isTrue();
    assertThat(ValidationUtils.areNotNull("test", 123, true)).isTrue();
    assertThat(ValidationUtils.areNotNull("test", null)).isFalse();
    assertThat(ValidationUtils.areNotNull(null)).isFalse();
    assertThat(ValidationUtils.areNotNull((Object) null)).isFalse();
  }

  @Test
  @DisplayName("Should detect if any object is null")
  void shouldDetectIfAnyObjectIsNull() {
    assertThat(ValidationUtils.hasNull()).isFalse(); // No arguments
    assertThat(ValidationUtils.hasNull("test")).isFalse();
    assertThat(ValidationUtils.hasNull("test", 123, true)).isFalse();
    assertThat(ValidationUtils.hasNull("test", null)).isTrue();
    assertThat(ValidationUtils.hasNull(null)).isTrue();
    assertThat(ValidationUtils.hasNull((Object) null)).isTrue();
  }
}
