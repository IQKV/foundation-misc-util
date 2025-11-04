# Testing Guide

## Testing Strategy

This project follows the [Test Pyramid](https://martinfowler.com/bliki/TestPyramid.html) approach:

1. **Unit Tests**: Most numerous, testing individual components in isolation
2. **Integration Tests**: Testing component interactions
3. **End-to-End Tests**: Testing complete workflows

## Running Tests

### Unit Tests

```bash
mvn clean test
```

Unit tests should:

- Be fast and independent
- Mock external dependencies
- Focus on a single component

### Integration Tests

```bash
mvn clean verify -P use-testcontainers
```

Integration tests use:

- Testcontainers for database and service dependencies
- Spring Boot Test for application context
- REST Assured for API testing

## Test Coverage

- Minimum required coverage: 80%
- Coverage report location: `target/site/jacoco/index.html`
- Generate coverage report: `mvn jacoco:report`

## Best Practices

1. **Test Naming**: Use descriptive names following `methodName_scenario_expectedBehavior` pattern
2. **Arrange-Act-Assert**: Structure tests with clear setup, action, and verification
3. **Test Independence**: Tests should not depend on each other
4. **Clean Setup/Teardown**: Properly initialize and clean up test resources

## Mocking

- Use Mockito for mocking dependencies
- Prefer constructor injection for easier testing
- Use `@MockBean` for Spring context tests

## Test Data

- Use test factories for complex objects
- Keep test data minimal and focused
- Use meaningful values that relate to the test case

## Continuous Integration

Tests automatically run on:

- Pull requests
- Pushes to main branch
- Release preparation
