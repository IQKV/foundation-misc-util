# AI Agent Development Guide

## Project Overview

**iQ Foundation Miscellaneous Utilities** — A plain Java utility library (`com.iqkv.foundation.misc.util`). No Spring Boot, no web layer, no DI container. Provides reusable static utility classes for string manipulation, date/time operations, and validation.

**Key characteristics:**

- Java 25, single Maven module, no Spring dependency
- Three utility classes: `StringUtils`, `DateTimeUtils`, `ValidationUtils`
- JUnit Jupiter 6 + AssertJ for testing; tests co-located with source in `src/test/`
- Checkstyle enforced at `validate` phase via `maven-project-common-checkstyle.xml` from `com.iqkv:checkstyle-config`
- JaCoCo coverage gate: ≥ 90% instruction coverage
- Node.js tooling (pnpm + Husky) for commit hooks and formatting only — not part of the Java build

## Project Structure

```
foundation-misc-util/
├── src/
│   ├── main/java/com/iqkv/foundation/misc/util/
│   │   ├── StringUtils.java       # String manipulation (blank checks, case conversion, masking, slugs)
│   │   ├── DateTimeUtils.java     # Date/time helpers
│   │   └── ValidationUtils.java  # Input validation helpers
│   └── test/java/com/iqkv/foundation/misc/util/
│       ├── StringUtilsTest.java
│       ├── DateTimeUtilsTest.java
│       └── ValidationUtilsTest.java
├── pom.xml                        # Single-module Maven build; Java 25
├── package.json                   # Node tooling only (Husky, OxFmt, commitlint)
└── AGENTS.md
```

## Code Standards

### Utility class pattern

All utility classes follow the same conventions:

- `final` class, private constructor that throws `UnsupportedOperationException`
- All methods are `static`; no instance state
- Every public method has a Javadoc with `@param`, `@return`, and null-handling documented
- Null inputs are handled gracefully — never throw `NullPointerException` without documenting it
- Use `org.slf4j.Logger` for any diagnostic logging; never `System.out`

```java
public final class MyUtils {

  private static final Logger log = LoggerFactory.getLogger(MyUtils.class);

  private MyUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * Does something useful.
   *
   * @param input the input value; may be null
   * @return result, or null if input is null
   */
  public static String doSomething(String input) {
    // ...
  }
}
```

### Testing standards

- Test class name: `{ClassName}Test`, same package as the class under test
- Use `@DisplayName` on every `@Test` — describe the expected behavior, not the method name
- Use `@ParameterizedTest` with `@NullAndEmptySource` / `@ValueSource` for boundary cases
- Follow **Arrange / Act / Assert** — no logic in assertions
- Use AssertJ fluent assertions (`assertThat(...).isEqualTo(...)`) — not JUnit `assertEquals`
- Test null inputs, empty inputs, and boundary conditions for every method
- The "should not allow instantiation" test is required for every utility class

```java
@Test
@DisplayName("Should return empty string when input is null")
void shouldReturnEmptyStringWhenInputIsNull() {
  // Arrange / Act
  var result = MyUtils.doSomething(null);

  // Assert
  assertThat(result).isEqualTo("");
}
```

### Checkstyle

The build runs Checkstyle at `validate` phase. Config is pulled from `com.iqkv:checkstyle-config` (`maven-project-common-checkstyle.xml`). Do not suppress violations with `@SuppressWarnings("checkstyle:...")` without a clear justification comment.

## Execution Discipline

- Root cause first. Fix the real entry point, not a bypass around it.
- Read the existing utility class and its tests before adding to or modifying either.
- After two identical build failures without new evidence, change approach — do not retry blindly.
- Run `./mvnw verify` locally before presenting a result; report failures rather than assuming green.
- No speculative additions. Add methods only when directly required by the request.

## Security

- No secrets, tokens, or credentials belong in this library or its tests.
- Use synthetic data in tests — no real email addresses, names, or identifiers.
- Flag unusual dependency names before adding them to `pom.xml`. Use exact versions.
- Never bypass `--no-verify` unless explicitly requested.

## AI Agent Development Guidelines

### Code generation principles

1. **Utility-first**: add to an existing class before creating a new one; only create a new class for a genuinely distinct responsibility
2. **Test alongside**: every new method needs a corresponding test in the matching `*Test.java` file
3. **Null-safe always**: document and handle null for every parameter
4. **No new dependencies** without explicit approval — the library intentionally has a minimal dependency footprint (`commons-lang3`, `slf4j-api`)
5. **Checkstyle-clean**: run `./mvnw checkstyle:check` to validate before presenting changes

### Approval workflow (MANDATORY)

Ask before applying. For any file creation, modification, or deletion:

```
1. ANALYZE  → understand the request
2. PRESENT  → describe changes, show method signature + Javadoc, list affected files
3. WAIT     → stop and wait for explicit approval
4. APPLY    → only after approval
5. VERIFY   → run ./mvnw verify, report results concisely
```

**Approval phrases:** "Yes", "Proceed", "Apply", "Do it", "Go ahead", "Looks good"

Operations NOT requiring approval: reading files, explaining concepts, running type checks.

### When presenting changes

```markdown
## Proposed Changes

**Goal**: one sentence

**Files**:
- `src/main/.../StringUtils.java` — add `foo(String)` method
- `src/test/.../StringUtilsTest.java` — add corresponding tests

**New method signature**:
```java
/**
 * Does X. Returns Y if input is null.
 */
public static String foo(String input) { ... }
```

Proceed?
```

## Commit Standards

Format: `type(scope): subject`

- Subject: imperative, lowercase, no trailing period, ≤ 72 chars
- Types: `feat`, `fix`, `improvement`, `refactor`, `docs`, `test`, `chore`, `perf`, `ci`, `build`, `revert`
- Scope: affected class or area (e.g., `string-utils`, `date-utils`, `validation-utils`, `checkstyle`, `deps`)
- For `fix`: describe the symptom and trigger, not the code change
  - ✅ `fix(string-utils): toSlug returns leading hyphen when input starts with special chars`
  - ❌ `fix(string-utils): add replaceAll call for leading hyphens`

Examples:
- `feat(string-utils): add maskEmail utility method`
- `test(validation-utils): add boundary tests for email validation`
- `chore(deps): update commons-lang3 to 3.21.0`
- `fix(date-utils): parse fails on UTC offset with colon separator`

## Development Commands

```bash
# Build and test (runs Checkstyle + JaCoCo + JUnit)
./mvnw verify

# Tests only
./mvnw test

# Checkstyle only
./mvnw checkstyle:check

# Skip Checkstyle for a quick local build
./mvnw verify -Dcheckstyle.skip=true

# Node tooling (formatting only — not part of Java build)
pnpm formatter:check   # check formatting
pnpm formatter:write   # auto-format
```

## Verification After Changes

Run before presenting a result:

```bash
./mvnw verify
```

This covers: Checkstyle → compile → test → JaCoCo coverage gate. Report results in 2–3 sentences. Generate a commit message for any multi-file change.
