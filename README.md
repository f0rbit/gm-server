# <img src="logo.png" width="60" /> GM Server

[![CircleCI](https://img.shields.io/circleci/build/gh/f0rbit/gm-server/main)](https://jitpack.io/#f0rbit/gm-server/v1.0.2) [![Javadocs](https://img.shields.io/badge/javadocs-live-blue)](https://f0rbit.github.io/gm-server/)

A barebone server for talking with **GameMaker: Studio** games written in **Java**

# Usage

### Gradle
Add the following to your `build.gradle`
```gradle
  repositories {
      maven { url 'https://jitpack.io' }
  }
  
  dependencies {
      implementation 'com.github.f0rbit:gm-server:v1.0.2'
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
    <groupId>com.github.f0rbit</groupId>
    <artifactId>gm-server</artifactId>
    <version>v1.0.2</version>
  </dependency>
</dependencies>
```
