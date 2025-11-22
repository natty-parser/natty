# natty-parser
[![maven central](https://img.shields.io/maven-central/v/io.github.natty-parser/natty.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.natty-parser/natty)
[![javadoc](https://javadoc.io/badge2/io.github.natty-parser/natty/javadoc.svg)](https://javadoc.io/doc/io.github.natty-parser/natty)

## What

Natty is a natural language date parser written in Java.  Given a date
expression, natty will apply standard language recognition and translation
techniques to produce a list of corresponding dates with optional parse and
syntax information.

## Documentation

Documentation (with a working demo) can be found at https://natty-parser.github.io/ (This used to live at http://natty.joestelmach.com/).

The original version at `joestelmach.com was`, I think, based on server-side JSP's. Our new version is more or less a 1-1 copy, but all server side (java) stuff is now running on the client (which is possible nowadays), making it possible to just host it for free via github-pages.

## History

Originally written by Joe Stelmach at https://github.com/joestelmach/natty and developed between 2010 and 2017. Abandoned since then. We keep it alive as fork here since 2022.

### Versions

Besides the package, maven coordinates, and bugfixes this project still behaves pretty much the same as the original thing.

 - 1.1.2: will only have a antlr3 dependency. So, even slf4j was dropped. If you need to have the debug-logging of natty (but why would you?), configure your logging framework to capture jul-logging.
 - 1.1.*: In these version the 'ics' support is dropped and replaced with algorithmic versions. The ics-files were heavily outdated and all 'holiday' and other recognized named date references where not working for recent dates.  E.g. the easter date (and dependents) will be correct for hundreds of years to come now.
 - 1.0.x: First versions release from this fork at new maven coordinates /package. Pus some bug fixes
 - 0.x: original versions at  https://github.com/joestelmach/natty

## Usage
Maven:
```xml
<dependency>
  <groupId>io.github.natty-parser</groupId>
  <artifactId>natty</artifactId>
  <version>1.1.1</version>
</dependency>
```

or [consult maven central](https://central.sonatype.com/artifact/io.github.natty-parser/natty/versions) for other build systems.

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
  <version>1.1.1-SNAPSHOT</version>
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
