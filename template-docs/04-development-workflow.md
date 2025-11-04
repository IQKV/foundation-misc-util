# Development Workflow

## Git Workflow

This project uses a conventional Git workflow with automated quality checks:

1. **Branching Strategy**
   - `main`: Production-ready code
   - `feature/*`: New features
   - `bugfix/*`: Bug fixes
   - `release/*`: Release preparation

2. **Commit Convention**
   - Uses [Conventional Commits](https://www.conventionalcommits.org/)
   - Format: `type(scope): message`
   - Examples: `feat(api): add user endpoint`, `fix(auth): correct token validation`

3. **Pre-commit Hooks**
   - Code formatting with Prettier
   - Linting with Stylelint
   - Commit message validation

## Development Cycle

1. **Setup Environment**

   ```bash
   pnpm install
   docker compose up -d
   ```

2. **Implement Changes**
   - Write code following project conventions
   - Add tests for new functionality

3. **Local Testing**

   ```bash
   mvn clean test
   mvn clean verify -P use-testcontainers  # For integration tests
   ```

4. **Code Quality**
   - Ensure code passes Checkstyle rules
   - Maintain test coverage (minimum 80%)

5. **Submit Changes**
   - Create a pull request with a clear description
   - Ensure CI checks pass

## Continuous Integration

GitHub Actions workflows automatically:

- Build the project
- Run tests
- Check code quality
- Validate commit messages and PR titles

## Release Process

1. Update version in `pom.xml`
2. Update `CHANGELOG.md`
3. Create a release tag
4. GitHub Actions will build and publish artifacts

## Dependency Management

- Use Maven for Java dependencies
- Use pnpm for Node.js dependencies
- Dependabot automatically suggests updates
