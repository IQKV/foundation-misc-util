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

## Minimal Change Discipline

Solve the whole problem with the least new code. The best code is the code never written.

### Scope before you write

Read the task and the code it touches. List every place the change must reach: callers, tests, fixtures, config, exports. Check what it could break for users: data it would destroy or expose, callers that stop working. That is scope. Extra features are not.

### Pick the smallest complete change

Take the first option that fully works:

1. Does it need to exist? Drop features, options and flexibility nobody asked for — name them in one line so the user can decide.
2. Already in this codebase (helper, component, service, pattern)? Use it the way the surrounding code does.
3. Standard library or platform feature? Use it, unless the project has its own.
4. An installed dependency? Use it. Never add a dependency for a few lines.
5. Can it be one line a reader gets at a glance? One line.
6. Otherwise: the minimum code that works.

Be lazy about the solution, never about the change itself:

- Finish every part the task needs, including the callers, tests and fixtures the change breaks.
- No abstraction, wrapper, option, config, boilerplate or "for later" code nobody asked for. Keep values in the form the platform already gives you. Deletion beats addition. Keep the layers, interfaces and conventions the codebase already has.
- The shortest working diff wins, once you know everything it must touch. A one-liner that needs decoding is not short.
- Comment only the why the code cannot show, in one line.
- Bug fix: grep every caller of the function you touch, then fix the root cause once in the shared code.
- Code you move or merge keeps its error handling and validation.
- Between options of equal size, take the one that is correct on edge cases.
- New non-trivial logic (a branch, a loop, a parser, money or security, or a whole new script) leaves one small test or an assert-based self-check. Trivial changes need none.
- A shortcut with a known limit gets a one-line comment: `shortcut: <the limit>, <when to upgrade>`.
- Push back when the need does not justify a part: state the smaller option in one line instead of building the bigger one silently.

Never cut: validation at trust boundaries, error handling that prevents data loss, security, accessibility, the calibration real hardware needs, anything the user explicitly asked for.

### Token discipline

- Locate with Grep/Glob, then read only the ranges you need — not whole trees or whole files.
- Prefer editing existing files to creating new ones; reuse code that is already there before writing.
- Never paste file contents back in a reply. Cite the path and lines.
- Keep replies to the outcome: what changed, what passed, any risk (see AI Communication Standards).

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

````markdown
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
````

Proceed?

````

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
````

## Evaluation

How we judge whether an agent task in this repository is actually done. Evaluation is evidence-based — a claim without the command and its observed result is not a pass.

> This section starts minimal and grows with the repository. Add a gate only when a change type repeatedly slips through without it.

### Definition of done

A task is evaluated as done only when all of the following hold:

- Every gate required by the change type below has run and passed.
- The final report names the exact command(s) run and the observed result (pass/fail) — per AI Agent Development Guidelines.
- No required gate was skipped silently. A gate that could not run is reported as a gap, never as a pass.

### Required gates by change type

| Change type                           | Required gates                                                                  |
| ------------------------------------- | ------------------------------------------------------------------------------- |
| Any change                            | `./mvnw -q compile`, then the narrowest targeted `./mvnw test -Dtest=ClassName` |
| New or changed utility method         | Unit tests for the method, then full `./mvnw verify`                            |
| Parsing, date-time or edge-case logic | Unit tests covering the edges, then full `./mvnw verify`                        |
| Dependency or build config            | Full `./mvnw verify`                                                            |
| Formatting or docs (`AGENTS.md`) only | `pnpm formatter:check`; no Java code gate — state that none applies             |

### Ordering

Run the narrowest relevant gate first, then the full set for the change type. On the first failure, stop and report it — do not continue on an unproven base (see Execution Discipline).

## Verification After Changes

Run before presenting a result:

```bash
./mvnw verify
```

This covers: Checkstyle → compile → test → JaCoCo coverage gate. Report results in 2–3 sentences. Generate a commit message for any multi-file change.
