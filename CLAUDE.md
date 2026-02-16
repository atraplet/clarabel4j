# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

clarabel4j is a Java wrapper around the native [Clarabel](https://clarabel.org) mathematical programming solver, using Java's Foreign Function and Memory (FFM) API. It requires JDK 25+.

## Build Commands

```bash
# Build and run all tests
mvn clean verify

# Build with code coverage (excludes bindings package)
mvn clean verify -P coverage

# Build without tests
mvn clean verify -DskipTests

# Run a single test class
mvn test -Dtest=ModelTest

# Run a single test method
mvn test -Dtest=ModelTest#solveLinearProgramReturnsExpectedSolution
```

There is no separate lint command. The project uses Lombok for annotation processing (configured in `lombok.config`).

## Architecture

### Layered Design

```
High-Level API (Model, Parameters, Cone, Matrix, Status, Output)
        ↓
FFM Bindings (com.ustermetrics.clarabel4j.bindings — auto-generated via jextract)
        ↓
Native Clarabel C Library (clarabel_c — loaded at runtime via NativeLoader)
```

### Key Classes (all in `com.ustermetrics.clarabel4j`)

- **Model** — Main orchestrator. Implements `AutoCloseable`. Has three lifecycle stages: `NEW → SETUP → OPTIMIZED`. Manages native memory via `Arena` (confined by default, or caller-provided). Methods: `setParameters()`, `setup()`, `optimize()`, `x()/z()/s()` for results.
- **Parameters** — Lombok `@Builder` record with ~35 nullable solver settings. Only non-null values override Clarabel defaults.
- **Matrix** — Record for CSC (Compressed Sparse Column) format sparse matrices. Validates column pointer monotonicity and row index ordering.
- **Cone** — Abstract sealed class. Subtypes: `ZeroCone`, `NonnegativeCone`, `SecondOrderCone`, `ExponentialCone`, `PowerCone`, `GenPowerCone`. Each has a `getTag()` (native enum value) and `getDimension()`.
- **Status** — Enum mapping native solver status codes (SOLVED, PRIMAL_INFEASIBLE, etc.).
- **Output** — Sealed interface with `StdOutOutput`, `StringOutput`, `FileOutput` implementations.

### Bindings Package

The `bindings/` directory contains `generate.sh` which uses [jextract](https://jdk.java.net/jextract/) to generate FFM bindings from Clarabel C headers. After generation, `NativeLoader.loadLibrary("clarabel_c")` must be manually added to `Clarabel_h`'s static initializer, and platform-specific binding code must be removed. The bindings are checked into source control and should not be manually edited.

### Native Library

The native library (`clarabel4j-native`) is a test-scoped dependency with platform-specific classifiers (`linux_64`, `windows_64`, `osx_arm64`) activated via Maven OS profiles.

### Conventions

- Guava `Preconditions` for input validation (`checkArgument`, `checkState`)
- Lombok `@NonNull`, `@Getter`, `@Builder`, `val` throughout
- Java sealed types for Cone and Output hierarchies
- Java records for immutable data (Matrix, Parameters)
- Tests verify against known Clarabel C++ example results with 1e-8 tolerance
