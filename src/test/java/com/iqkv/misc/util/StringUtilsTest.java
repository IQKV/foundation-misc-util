package com.iqkv.misc.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("StringUtils Tests")
class StringUtilsTest {

  @Test
  @DisplayName("Should not allow instantiation")
  void shouldNotAllowInstantiation() {
    assertThatThrownBy(() -> {
      var constructor = StringUtils.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      constructor.newInstance();
    }).hasCauseInstanceOf(UnsupportedOperationException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @DisplayName("Should identify empty strings")
  void shouldIdentifyEmptyStrings(String input) {
    assertThat(StringUtils.isEmpty(input)).isTrue();
    assertThat(StringUtils.isNotEmpty(input)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = { "hello", " ", "test" })
  @DisplayName("Should identify non-empty strings")
  void shouldIdentifyNonEmptyStrings(String input) {
    assertThat(StringUtils.isEmpty(input)).isFalse();
    assertThat(StringUtils.isNotEmpty(input)).isTrue();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = { " ", "  ", "\t", "\n", "\r\n" })
  @DisplayName("Should identify blank strings")
  void shouldIdentifyBlankStrings(String input) {
    assertThat(StringUtils.isBlank(input)).isTrue();
    assertThat(StringUtils.isNotBlank(input)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = { "hello", "a", "test with spaces" })
  @DisplayName("Should identify non-blank strings")
  void shouldIdentifyNonBlankStrings(String input) {
    assertThat(StringUtils.isBlank(input)).isFalse();
    assertThat(StringUtils.isNotBlank(input)).isTrue();
  }

  @Test
  @DisplayName("Should return default value when string is null")
  void shouldReturnDefaultValueWhenStringIsNull() {
    String defaultValue = "default";
    assertThat(StringUtils.defaultIfNull(null, defaultValue)).isEqualTo(defaultValue);
    assertThat(StringUtils.defaultIfNull("test", defaultValue)).isEqualTo("test");
  }

  @Test
  @DisplayName("Should return default value when string is blank")
  void shouldReturnDefaultValueWhenStringIsBlank() {
    String defaultValue = "default";
    assertThat(StringUtils.defaultIfBlank(null, defaultValue)).isEqualTo(defaultValue);
    assertThat(StringUtils.defaultIfBlank("", defaultValue)).isEqualTo(defaultValue);
    assertThat(StringUtils.defaultIfBlank("  ", defaultValue)).isEqualTo(defaultValue);
    assertThat(StringUtils.defaultIfBlank("test", defaultValue)).isEqualTo("test");
  }

  @Test
  @DisplayName("Should trim strings correctly")
  void shouldTrimStringsCorrectly() {
    assertThat(StringUtils.trimToNull(null)).isNull();
    assertThat(StringUtils.trimToNull("")).isNull();
    assertThat(StringUtils.trimToNull("  ")).isNull();
    assertThat(StringUtils.trimToNull("  test  ")).isEqualTo("test");

    assertThat(StringUtils.trimToEmpty(null)).isEqualTo("");
    assertThat(StringUtils.trimToEmpty("")).isEqualTo("");
    assertThat(StringUtils.trimToEmpty("  ")).isEqualTo("");
    assertThat(StringUtils.trimToEmpty("  test  ")).isEqualTo("test");
  }

  @Test
  @DisplayName("Should capitalize strings correctly")
  void shouldCapitalizeStringsCorrectly() {
    assertThat(StringUtils.capitalize(null)).isNull();
    assertThat(StringUtils.capitalize("")).isEqualTo("");
    assertThat(StringUtils.capitalize("hello")).isEqualTo("Hello");
    assertThat(StringUtils.capitalize("HELLO")).isEqualTo("Hello");
    assertThat(StringUtils.capitalize("hELLO")).isEqualTo("Hello");
    assertThat(StringUtils.capitalize("a")).isEqualTo("A");
  }

  @Test
  @DisplayName("Should convert to camelCase correctly")
  void shouldConvertToCamelCaseCorrectly() {
    assertThat(StringUtils.toCamelCase(null)).isNull();
    assertThat(StringUtils.toCamelCase("")).isEqualTo("");
    assertThat(StringUtils.toCamelCase("  ")).isEqualTo("  ");
    assertThat(StringUtils.toCamelCase("hello world")).isEqualTo("helloWorld");
    assertThat(StringUtils.toCamelCase("hello_world")).isEqualTo("helloWorld");
    assertThat(StringUtils.toCamelCase("hello-world")).isEqualTo("helloWorld");
    assertThat(StringUtils.toCamelCase("HELLO WORLD")).isEqualTo("helloWorld");
    assertThat(StringUtils.toCamelCase("hello world test")).isEqualTo("helloWorldTest");
  }

  @Test
  @DisplayName("Should convert to PascalCase correctly")
  void shouldConvertToPascalCaseCorrectly() {
    assertThat(StringUtils.toPascalCase(null)).isNull();
    assertThat(StringUtils.toPascalCase("")).isEqualTo("");
    assertThat(StringUtils.toPascalCase("  ")).isEqualTo("  ");
    assertThat(StringUtils.toPascalCase("hello world")).isEqualTo("HelloWorld");
    assertThat(StringUtils.toPascalCase("hello_world")).isEqualTo("HelloWorld");
    assertThat(StringUtils.toPascalCase("hello-world")).isEqualTo("HelloWorld");
    assertThat(StringUtils.toPascalCase("hello world test")).isEqualTo("HelloWorldTest");
  }

  @Test
  @DisplayName("Should convert to snake_case correctly")
  void shouldConvertToSnakeCaseCorrectly() {
    assertThat(StringUtils.toSnakeCase(null)).isNull();
    assertThat(StringUtils.toSnakeCase("")).isEqualTo("");
    assertThat(StringUtils.toSnakeCase("  ")).isEqualTo("  ");
    assertThat(StringUtils.toSnakeCase("helloWorld")).isEqualTo("hello_world");
    assertThat(StringUtils.toSnakeCase("HelloWorld")).isEqualTo("hello_world");
    assertThat(StringUtils.toSnakeCase("hello world")).isEqualTo("hello_world");
    assertThat(StringUtils.toSnakeCase("hello-world")).isEqualTo("hello_world");
    assertThat(StringUtils.toSnakeCase("XMLHttpRequest")).isEqualTo("xmlhttp_request");
  }

  @Test
  @DisplayName("Should convert to kebab-case correctly")
  void shouldConvertToKebabCaseCorrectly() {
    assertThat(StringUtils.toKebabCase(null)).isNull();
    assertThat(StringUtils.toKebabCase("")).isEqualTo("");
    assertThat(StringUtils.toKebabCase("  ")).isEqualTo("  ");
    assertThat(StringUtils.toKebabCase("helloWorld")).isEqualTo("hello-world");
    assertThat(StringUtils.toKebabCase("HelloWorld")).isEqualTo("hello-world");
    assertThat(StringUtils.toKebabCase("hello world")).isEqualTo("hello-world");
    assertThat(StringUtils.toKebabCase("hello_world")).isEqualTo("hello-world");
    assertThat(StringUtils.toKebabCase("XMLHttpRequest")).isEqualTo("xmlhttp-request");
  }

  @Test
  @DisplayName("Should truncate strings correctly")
  void shouldTruncateStringsCorrectly() {
    assertThat(StringUtils.truncate(null, 10)).isNull();
    assertThat(StringUtils.truncate("hello", -1)).isEqualTo("hello");
    assertThat(StringUtils.truncate("hello", 10)).isEqualTo("hello");
    assertThat(StringUtils.truncate("hello", 5)).isEqualTo("hello");
    assertThat(StringUtils.truncate("hello world", 8)).isEqualTo("hello...");
    assertThat(StringUtils.truncate("hello", 3)).isEqualTo("hel");
    assertThat(StringUtils.truncate("hello", 2)).isEqualTo("he");
    assertThat(StringUtils.truncate("hello", 1)).isEqualTo("h");
    assertThat(StringUtils.truncate("hello", 0)).isEqualTo("");
  }

  @Test
  @DisplayName("Should mask strings correctly")
  void shouldMaskStringsCorrectly() {
    assertThat(StringUtils.mask(null, 2, 2, '*')).isNull();
    assertThat(StringUtils.mask("", 2, 2, '*')).isEqualTo("");
    assertThat(StringUtils.mask("abc", 2, 2, '*')).isEqualTo("abc"); // Too short to mask
    assertThat(StringUtils.mask("hello", 1, 1, '*')).isEqualTo("h***o");
    assertThat(StringUtils.mask("hello world", 2, 2, '*')).isEqualTo("he*******ld");
    assertThat(StringUtils.mask("1234567890", 3, 2, 'X')).isEqualTo("123XXXXX90");
  }

  @Test
  @DisplayName("Should mask email addresses correctly")
  void shouldMaskEmailAddressesCorrectly() {
    assertThat(StringUtils.maskEmail(null)).isNull();
    assertThat(StringUtils.maskEmail("")).isEqualTo("");
    assertThat(StringUtils.maskEmail("invalid-email")).isEqualTo("invalid-email");
    assertThat(StringUtils.maskEmail("a@example.com")).isEqualTo("a@example.com"); // Too short
    assertThat(StringUtils.maskEmail("test@example.com")).isEqualTo("t***@example.com");
    assertThat(StringUtils.maskEmail("john.doe@company.org")).isEqualTo("j*******@company.org");
  }

  @Test
  @DisplayName("Should generate random strings correctly")
  void shouldGenerateRandomStringsCorrectly() {
    assertThat(StringUtils.randomAlphanumeric(0)).isEqualTo("");
    assertThat(StringUtils.randomAlphanumeric(-1)).isEqualTo("");

    String result = StringUtils.randomAlphanumeric(10);
    assertThat(result).hasSize(10);
    assertThat(result).matches("[a-zA-Z0-9]+");

    String numeric = StringUtils.randomNumeric(5);
    assertThat(numeric).hasSize(5);
    assertThat(numeric).matches("[0-9]+");

    String alphabetic = StringUtils.randomAlphabetic(7);
    assertThat(alphabetic).hasSize(7);
    assertThat(alphabetic).matches("[a-zA-Z]+");
  }

  @Test
  @DisplayName("Should generate random strings from custom characters")
  void shouldGenerateRandomStringsFromCustomCharacters() {
    String customChars = "ABC123";
    String result = StringUtils.randomString(10, customChars);
    assertThat(result).hasSize(10);
    assertThat(result).matches("[ABC123]+");

    assertThat(StringUtils.randomString(5, "")).isEqualTo("");
    assertThat(StringUtils.randomString(0, "ABC")).isEqualTo("");
  }

  @Test
  @DisplayName("Should join strings correctly")
  void shouldJoinStringsCorrectly() {
    assertThat(StringUtils.join(",", (List<String>) null)).isEqualTo("");
    assertThat(StringUtils.join(",", List.of())).isEqualTo("");
    assertThat(StringUtils.join(",", List.of("a"))).isEqualTo("a");
    assertThat(StringUtils.join(",", List.of("a", "b", "c"))).isEqualTo("a,b,c");
    assertThat(StringUtils.join(" - ", List.of("hello", "world"))).isEqualTo("hello - world");

    assertThat(StringUtils.join(",", (String[]) null)).isEqualTo("");
    assertThat(StringUtils.join(",")).isEqualTo("");
    assertThat(StringUtils.join(",", "a")).isEqualTo("a");
    assertThat(StringUtils.join(",", "a", "b", "c")).isEqualTo("a,b,c");
  }

  @Test
  @DisplayName("Should remove diacritics correctly")
  void shouldRemoveDiacriticsCorrectly() {
    assertThat(StringUtils.removeDiacritics(null)).isNull();
    assertThat(StringUtils.removeDiacritics("")).isEqualTo("");
    assertThat(StringUtils.removeDiacritics("hello")).isEqualTo("hello");
    assertThat(StringUtils.removeDiacritics("café")).isEqualTo("cafe");
    assertThat(StringUtils.removeDiacritics("naïve")).isEqualTo("naive");
    assertThat(StringUtils.removeDiacritics("résumé")).isEqualTo("resume");
    assertThat(StringUtils.removeDiacritics("Zürich")).isEqualTo("Zurich");
  }

  @Test
  @DisplayName("Should create URL-friendly slugs correctly")
  void shouldCreateUrlFriendlySlugsCorrectly() {
    assertThat(StringUtils.toSlug(null)).isEqualTo("");
    assertThat(StringUtils.toSlug("")).isEqualTo("");
    assertThat(StringUtils.toSlug("  ")).isEqualTo("");
    assertThat(StringUtils.toSlug("Hello World")).isEqualTo("hello-world");
    assertThat(StringUtils.toSlug("Hello, World!")).isEqualTo("hello-world");
    assertThat(StringUtils.toSlug("café & naïve")).isEqualTo("cafe-naive");
    assertThat(StringUtils.toSlug("Multiple   Spaces")).isEqualTo("multiple-spaces");
    assertThat(StringUtils.toSlug("--leading-and-trailing--")).isEqualTo("leading-and-trailing");
    assertThat(StringUtils.toSlug("Special@#$%Characters")).isEqualTo("specialcharacters");
  }

  @Test
  @DisplayName("Should count occurrences correctly")
  void shouldCountOccurrencesCorrectly() {
    assertThat(StringUtils.countOccurrences(null, "test")).isZero();
    assertThat(StringUtils.countOccurrences("test", null)).isZero();
    assertThat(StringUtils.countOccurrences("", "test")).isZero();
    assertThat(StringUtils.countOccurrences("test", "")).isZero();
    assertThat(StringUtils.countOccurrences("hello world", "l")).isEqualTo(3);
    assertThat(StringUtils.countOccurrences("hello world", "ll")).isEqualTo(1);
    assertThat(StringUtils.countOccurrences("hello world", "o")).isEqualTo(2);
    assertThat(StringUtils.countOccurrences("hello world", "xyz")).isZero();
    assertThat(StringUtils.countOccurrences("aaaa", "aa")).isEqualTo(2);
  }

  @Test
  @DisplayName("Should reverse strings correctly")
  void shouldReverseStringsCorrectly() {
    assertThat(StringUtils.reverse(null)).isNull();
    assertThat(StringUtils.reverse("")).isEqualTo("");
    assertThat(StringUtils.reverse("a")).isEqualTo("a");
    assertThat(StringUtils.reverse("hello")).isEqualTo("olleh");
    assertThat(StringUtils.reverse("hello world")).isEqualTo("dlrow olleh");
    assertThat(StringUtils.reverse("12345")).isEqualTo("54321");
  }

  @Test
  @DisplayName("Should identify palindromes correctly")
  void shouldIdentifyPalindromesCorrectly() {
    assertThat(StringUtils.isPalindrome(null)).isTrue();
    assertThat(StringUtils.isPalindrome("")).isTrue();
    assertThat(StringUtils.isPalindrome("a")).isTrue();
    assertThat(StringUtils.isPalindrome("aa")).isTrue();
    assertThat(StringUtils.isPalindrome("aba")).isTrue();
    assertThat(StringUtils.isPalindrome("racecar")).isTrue();
    assertThat(StringUtils.isPalindrome("A man a plan a canal Panama")).isTrue();
    assertThat(StringUtils.isPalindrome("race a car")).isFalse();
    assertThat(StringUtils.isPalindrome("hello")).isFalse();
    assertThat(StringUtils.isPalindrome("Racecar")).isTrue(); // Case insensitive
  }
}
