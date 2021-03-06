# <img src="logo.png" width="60" /> GM Server

[![CircleCI](https://img.shields.io/circleci/build/gh/f0rbit/gm-server/main)](https://jitpack.io/#dev.forbit/gm-server/1.1) [![Javadocs](https://img.shields.io/badge/javadocs-live-blue)](https://f0rbit.github.io/gm-server/)

A barebone server for talking with **GameMaker: Studio** games written in **Java**

# Usage

### Gradle

Add the following to your `build.gradle`

```gradle
  repositories {
      maven { url 'https://jitpack.io' }
  }
  
  dependencies {
      implementation 'dev.forbit:gm-server:1.1'
  }

```

### Maven

Add the following to your `pom.xml`

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
<dependency>
    <groupId>dev.forbit</groupId>
    <artifactId>gm-server</artifactId>
    <version>1.1</version>
</dependency>
</dependencies>
```

## Examples

Inside the `examples` directory, is the `basic-raw-server`, which implements a one-room multiplayer lobby, where people can join and move around.

If you'd like a guided breakdown of how to implement this, or where to start, check out
this [wiki article](https://github.com/f0rbit/gm-server/wiki/Basic-Raw-Server)

### License

The source code is licensed under the [MIT License](./LICENSE.md)
