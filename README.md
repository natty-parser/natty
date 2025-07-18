# natty-parser
[![maven central](https://img.shields.io/maven-central/v/io.github.natty-parser/natty.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.natty-parser/natty)
[![javadoc](https://javadoc.io/badge2/io.github.natty-parser/natty/javadoc.svg)](https://javadoc.io/doc/io.github.natty-parser/natty)

## What

Natty is a natural language date parser written in Java.  Given a date
expression, natty will apply standard language recognition and translation
techniques to produce a list of corresponding dates with optional parse and
syntax information.

## Documentation

Used to live at http://natty.joestelmach.com/, we'll try to get something up at https://natty-parser.github.io/

## History

Originally written by Joe Stelmach at https://github.com/joestelmach/natty and developed between 2010 and 2017. Abandoned since then. We keep it alive as fork here since 2022.

## Usage
Maven:
```xml
<dependency>
  <groupId>io.github.natty-parser</groupId>
  <artifactId>natty</artifactId>
  <version>1.0.2</version>
</dependency>
```

or [consult maven.org](https://search.maven.org/artifact/io.github.natty-parser/natty) for other build systems.

for snapshot dependencies, add maven-snapshots repository to your `pom.xml`:
```xml
<repositories>
  <repository>
  <name>Central Portal Snapshots</name>
  <id>central-portal-snapshots</id>
  <url>https://central.sonatype.com/repository/maven-snapshots/</url>
  <releases>
    <enabled>false</enabled>
  </releases>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
  </repository>
</repositories>
```
and
```xml
<dependency>
  <groupId>io.github.natty-parser</groupId>
  <artifactId>natty</artifactId>
  <version>1.1.0-SNAPSHOT</version>
</dependency>
```

## Idea behind the fork

Started with [a thread in the original project](https://github.com/joestelmach/natty/issues/274). The aim is to try providing maintenance for the library.

Plan / Priorities (as of November 2022):

1. ~~Release version `1.0.0` with (possibly) the same code as the fork root, but under new name, consider renaming packages and artifacts to use `natty-parser` as the moniker, etc.~~
   DONE
2. ~~Fix the tests (some are failing?).~~ DONE
3. ~~Set up basic technicalities of the fork - Maven release process, CI (Continuous Integration), etc.~~ DONE
4. Start accepting contributions (PRs), encourage the community to solve [issues reported in the original repo](https://github.com/joestelmach/natty/issues)
5. Consider switching to Gradle (as this is a build tool which is modern and I am familiar with)
6. Decide on the overall vision of the project - at this point it's hard to tell, but I think a conscious decision on what is gonna be supported would be
   great - i.e. focus on i18n, customization, stability, or what?

## Known issues from the parent project

See [issues](issues.md)

## Commitment

I hope for open collaboration and contributions from others. To me it's totally a side-project, not the core activity. Yet, I can commit to being (fairly) responsive and inclusive.

## Contributions

Are more than welcome. Feel free to reach out (e.g. by creating an issue in this repo) to offer your support and ideas. I am happy to include more
maintainers. Tag @mccartney or @mihxil if no response for 2-3 days (I might have missed that).

I see this project potentially being welcoming to many, incl. quite junior and inexperienced developers, who would like to learn and contribute.
