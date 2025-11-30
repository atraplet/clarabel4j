# Clarabel Solver for Java

[![Build](https://github.com/atraplet/clarabel4j/actions/workflows/build.yml/badge.svg)](https://github.com/atraplet/clarabel4j/actions/workflows/build.yml)
[![Codecov](https://codecov.io/github/atraplet/clarabel4j/graph/badge.svg?token=S8TXRQ4UAZ)](https://codecov.io/github/atraplet/clarabel4j)
[![Maven Central](https://img.shields.io/maven-central/v/com.ustermetrics/clarabel4j)](https://central.sonatype.com/artifact/com.ustermetrics/clarabel4j)
[![Apache License, Version 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://github.com/atraplet/clarabel4j/blob/master/LICENSE)

*This library requires JDK 25 or later.*

clarabel4j (Clarabel Solver for Java) is a Java library that provides an interface from the Java programming language to
the native open source mathematical programming solver [Clarabel](https://clarabel.org). It invokes the solver through
Java's [Foreign Function and Memory (FFM) API](https://docs.oracle.com/en/java/javase/25/core/foreign-function-and-memory-api.html).

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
from [Maven Central](https://central.sonatype.com/artifact/com.ustermetrics/clarabel4j-native) to your `pom.xml`

```
<dependency>
    <groupId>com.ustermetrics</groupId>
    <artifactId>clarabel4j-native</artifactId>
    <version>x.y.z</version>
    <classifier>platform</classifier>
    <scope>runtime</scope>
</dependency>
```

where `x.y.z` is the version of the library and `platform` is one of `linux_64`, `windows_64`, or `osx_arm64`. If no
`classifier` is specified, binaries for all platforms are included.

Or alternatively install the native solver on the machine and add the location to the `java.library.path`. clarabel4j
dynamically loads the native solver.

### Run Code

Since clarabel4j invokes some restricted methods of the FFM API,
use `--enable-native-access=com.ustermetrics.clarabel4j --enable-native-access=org.scijava.nativelib` or
`--enable-native-access=ALL-UNNAMED` (if you are not using the Java Platform Module System) to avoid warnings from the
Java runtime.

## Build

### Java bindings

The directory `./bindings` contains the Bash script `generate.sh` needed to generate the Java bindings. The actual
bindings are under `./src/main/java` in the package `com.ustermetrics.clarabel4j.bindings`.

The script depends on the [jextract](https://jdk.java.net/jextract/) tool, which mechanically generates the bindings
from native library headers.

The bindings are generated in two steps: First, `./bindings/generate.sh --dump-includes` generates the dump of the
included symbols in the `includes.txt` file. Second, `./bindings/generate.sh` generates the actual bindings. After
generating the bindings, update the code to load the native library using `NativeLoader.loadLibrary` and remove any
platform-specific binding code.

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
solver [Clarabel](https://clarabel.org), which is developed and maintained by Paul Goulart (main development, maths and
algorithms) and Yuwen Chen (maths and algorithms) from the [Oxford Control Group](http://www.eng.ox.ac.uk/control) of
the Department of Engineering Science at the University of Oxford, and other contributors. For details
see https://clarabel.org, https://github.com/oxfordcontrol/Clarabel.rs,
and https://github.com/oxfordcontrol/Clarabel.cpp.
