# Project Overview

## Introduction

This Spring Boot template provides a standardized foundation for developing Java applications using Spring Boot. It follows Maven's standard single-project layout and includes a
comprehensive set of tools and configurations to streamline development.

## Key Features

- **Standard Maven Structure**: Follows the conventional Maven project layout
- **Spring Boot Integration**: Pre-configured for Spring Boot application development
- **Quality Assurance**: Integrated code quality tools (Checkstyle, JaCoCo)
- **Development Workflow**: Git hooks, conventional commits, and automated workflows
- **Documentation**: documentation and community guidelines
- **Testing Framework**: Unit and integration testing setup with testcontainers support
- **Docker Support**: Containerization with Docker and Docker Compose

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/         # Application source code
│   │   ├── resources/    # Application resources
│   │   └── docker/       # Docker-related files
│   └── test/
│       ├── java/         # Test source code
│       └── resources/    # Test resources
├── .github/              # GitHub-specific files and workflows
├── .mvn/                 # Maven wrapper configuration
├── docs/                 # Project documentation
└── [Configuration files] # Various configuration files for tools
```

## Technology Stack

- **Java**: OpenJDK 21 LTS
- **Build Tool**: Maven with Spring Boot parent POM
- **Framework**: Spring Boot
- **Containerization**: Docker and Docker Compose
- **Package Management**: pnpm for Node.js dependencies
- **Code Quality**: Checkstyle, JaCoCo, Prettier, Stylelint
- **Git Workflow**: Husky, Commitlint, Conventional Commits

## Purpose and Use Cases

This template is designed for:

1. **New Spring Boot Projects**: Quickly bootstrap new applications with best practices
2. **Standardized Development**: Ensure consistent development practices across teams
3. **CI/CD Integration**: Ready-to-use GitHub workflows for continuous integration
4. **Quality-Focused Development**: Built-in tools to maintain code quality

## Next Steps

- See the [Getting Started Guide](02-getting-started.md) for setup instructions
- Review the [Architecture Overview](03-architecture-overview.md) for design details
- Check the [Development Workflow](04-development-workflow.md) for day-to-day development practices
