# Getting Started Guide

## Prerequisites

- Java 21 (OpenJDK)
- Maven
- Node.js & pnpm
- Docker & Docker Compose
- Git

## Quick Start

1. **Create a new repository from this template**
   - Click the "Use this template" button on GitHub
   - Or clone the repository directly:
     ```bash
     git clone https://github.com/IQKV/standard-maven-single-project-layout.git my-project
     cd my-project
     ```

2. **Install Git hooks**

   ```bash
   pnpm install
   ```

3. **Start local development services**

   ```bash
   docker compose up -d
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=local -P dev
   ```

## Project Configuration

1. **Update project information in pom.xml**
   - Group ID
   - Artifact ID
   - Version
   - Description

2. **Configure application properties**
   - Edit `src/main/resources/application.properties` or `application.yml`

3. **Customize Docker Compose services**
   - Edit `compose.yaml` to match your development needs

## Development Profiles

- **default**: Standard development
- **dev**: Includes Spring Boot DevTools
- **production**: Optimized for deployment
- **use-testcontainers**: Integration testing with testcontainers

## Next Steps

- Review the [Development Workflow](04-development-workflow.md)
- Check the [Testing Guide](05-testing-guide.md)
- Explore the [Architecture Overview](03-architecture-overview.md)
