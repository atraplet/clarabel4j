# Clarabel Solver for Java

[![Build](https://github.com/atraplet/clarabel4j/actions/workflows/build.yml/badge.svg)](https://github.com/atraplet/clarabel4j/actions/workflows/build.yml)
[![Codecov](https://codecov.io/github/atraplet/clarabel4j/graph/badge.svg?token=S8TXRQ4UAZ)](https://codecov.io/github/atraplet/clarabel4j)
[![Maven Central](https://img.shields.io/maven-central/v/com.ustermetrics/clarabel4j)](https://central.sonatype.com/artifact/com.ustermetrics/clarabel4j)
[![Apache License, Version 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://github.com/atraplet/clarabel4j/blob/master/LICENSE)

*This library requires JDK 22 as it depends on Java's
new [Foreign Function and Memory (FFM) API](https://docs.oracle.com/en/java/javase/22/core/foreign-function-and-memory-api.html).*

clarabel4j (Clarabel Solver for Java) is a Java library that provides an interface from the Java programming language to
the native open source mathematical programming solver [Clarabel](https://clarabel.org). It invokes the solver
through Java's
new [Foreign Function and Memory (FFM) API](https://docs.oracle.com/en/java/javase/22/core/foreign-function-and-memory-api.html).

## Usage

### Dependency

Add the latest version from [Maven Central](https://central.sonatype.com/artifact/com.ustermetrics/clarabel4j) to
your `pom.xml`

```
<dependency>
    <groupId>com.ustermetrics</groupId>
    <artifactId>clarabel4j</artifactId>
    <version>x.y.z</version>
</dependency>
```

### Native Library

Either add the latest version of [clarabel4j-native](https://github.com/atraplet/clarabel4j-native)
from [Maven Central](https://central.sonatype.com/artifact/com.ustermetrics/clarabel4j-native) to
your `pom.xml`

```
<dependency>
    <groupId>com.ustermetrics</groupId>
    <artifactId>clarabel4j-native</artifactId>
    <version>x.y.z</version>
    <scope>runtime</scope>
</dependency>
```

or install the native solver on the machine and add the location to the `java.library.path`. clarabel4j dynamically
loads the native solver.

### Run Code

Since clarabel4j invokes some restricted methods of the FFM API,
use `--enable-native-access=com.ustermetrics.clarabel4j` or `--enable-native-access=ALL-UNNAMED` (if you are not using
the Java Platform Module System) to avoid warnings from the Java runtime.

## Build

### Java bindings

The directory `./bindings` contains the files and scripts needed to generate the Java bindings. The actual bindings are
under `./src/main/java` in the package `com.ustermetrics.clarabel4j.bindings`.

The scripts depend on the [jextract](https://jdk.java.net/jextract/) tool, which mechanically generates Java bindings
from native library headers.

The bindings are generated in two steps: First, `./bindings/generate_includes.sh` generates the dumps of the included
symbols in the `includes.txt` file. Replace absolute platform dependent path with relative platform independent path in
the comments. Remove unused includes. Second, `./bindings/generate_bindings.sh` generates the actual Java bindings.
Add `NativeLoader.loadLibrary.` Remove platform dependent layout constants and make the code platform independent.

## Release

Update the version in the `pom.xml`, create a tag, and push it by running

```
export VERSION=X.Y.Z
git checkout --detach HEAD
sed -i -E "s/<version>[0-9]+\-SNAPSHOT<\/version>/<version>$VERSION<\/version>/g" pom.xml
git commit -m "v$VERSION" pom.xml
git tag v$VERSION
git push origin v$VERSION
```

This will trigger the upload of the package to Maven Central via GitHub Actions.

Then, go to the GitHub repository [releases page](https://github.com/atraplet/clarabel4j/releases) and update the
release.

## Credits

This project is based on the native open source mathematical programming
solver [Clarabel](https://clarabel.org), which is developed and maintained
by Paul Goulart and other members of the [Oxford Control Group](http://www.eng.ox.ac.uk/control).
