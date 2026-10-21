package org.natty;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;


public class ParserTest {

  private static final Logger log  = Logger.getLogger(ParserTest.class.getName());



  @Test
  public void serializationOfParser() throws IOException, ClassNotFoundException {
    // Test serializing of a basic parser which does not have a
    // user-supplied zone.
    Parser parser = new Parser();

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
      out.writeObject(parser);
    }
    byte[] serialized = bytes.toByteArray();

    Parser deserialized;
    try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(serialized))) {
      deserialized = (Parser) in.readObject();
    }

    // Check the parser
    // Note: if a.equals(b), then a.hashcode() MUST equal b.hashcode()
    // see http://stackoverflow.com/a/27609 for more.
    assertEquals(parser, deserialized);
    assertEquals(parser.hashCode(), deserialized.hashCode());
  }

  @Test
  public void serializationOfParserTimeZone() throws IOException, ClassNotFoundException {
    // Test serializing of an alternate parser with a
    // user-supplied zone.
    Parser parser = new Parser(ZoneId.systemDefault());

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
      out.writeObject(parser);
    }
    byte[] serialized = bytes.toByteArray();

    Parser deserialized;
    try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(serialized))) {
      deserialized = (Parser) in.readObject();
    }

    // Check the parser
    // Note: if a.equals(b), then a.hashcode() MUST equal b.hashcode()
    // see http://stackoverflow.com/a/27609 for more.
    assertEquals(parser, deserialized);
    assertEquals(parser.hashCode(), deserialized.hashCode());
  }

  @Test
  public void issue135() {
    Parser parser1 = new Parser();
    List<DateGroup> groups1 = parser1.parse("Diagnosis 1: AMS");

  }

  @Test
  public void issue278() {
    final List<DateGroup> groups = new Parser().parse("last easter", date(2021, 2, 1));
  }

  /**
   * Test whether we have easter dates for a lot of years.
   */

  @Test
  public void lastEaster() {
    for (int i = 1900; i < 2100; i++) {
      Date reference = date(i, 2, 1);
      final List<DateGroup> groups = new Parser().parse("last easter", reference);
      assertEquals(1, groups.size());
      Instant easter = groups.get(0).getInstants().get(0);
      log.info("Last easter for year " + i + ": " +easter);
    }
  }

  @Test
  public void lastElectionDay() {
    for (int i = 2001; i < 2030; i++) {
      Date reference = date(i, 2, 1);
      final List<DateGroup> groups = new Parser().parse("last election day", reference);
      assertEquals(1, groups.size());
      Instant easter = groups.get(0).getInstants().get(0);
      log.info("Last election day for year " +  i + ": " + easter);
    }
  }


  /**
   * Just showing #279 now. Indeed, it doesn't seem to make much sense what is happening now.
   */

  @Test
  public void issue279() {
    Parser parser = new Parser();
    Date d2000  = date(2000, 2, 1);
    Date d2010  = date(2010, 2, 1);
    Date d2020  = date(2020, 2, 1);

    log.info("Parsed date: " +
      parser.parse("Fri Mar 04 00:00:00 UTC 2016", d2000).get(0).getTemporals().get(0) +
      " (" + d2000 + ")");

    log.info("Parsed date: " +
      parser.parse("Fri Mar 04 00:00:00 UTC 2016", d2010).get(0).getTemporals().get(0) +
      " (" + d2010 + ")");

    log.info("Parsed date: " +
      parser.parse("Fri Mar 04 00:00:00 UTC 2016", d2020).get(0).getTemporals().get(0) +
      " (" + d2020 + ")");



    List<DateGroup> parse2 = parser.parse("Tue Jan 12 00:00:00 UTC 2016");

    log.info("Parsed date: " +  parse2.get(0).getTemporals().get(0));
  }

  /**
   * This used to give exceptions (because summer not found any more)
   */
  @Test
  public void issue277() {
    Parser parser = new Parser();
    List<DateGroup> parse1 = parser.parse("MIGUEL JESSIE REYEZ - QUEEN NAIJA - J.I.D - MASEGO - TIERRA WHACK SUMMER WALKER KIANA LEDE - SNOH AALEGRA - RAVEENA TOBI LOU - JESS CONNELLY UMI - DAVEB IVY SOLE - PARISALEXA");
    log.info("Parsed date: " + parse1.get(0).getTemporals().get(0));
  }

  @Test
  public void issue246() {
    Parser parser = new Parser("2026-10-19T22:00:00Z", "Europe/Amsterdam");
    List<DateGroup> parsed = parser.parse("the day before next 10/11/2016");
    log.info("Parsed date: " + parsed.get(0).getTemporals().get(0));
    //assertEquals(Instant.parse("2026-10-19T22:00:30Z"), _30secs.get(0).getDates().get(0).toInstant());

  }

  @Test
  public void issue149() {
    Parser parser = new Parser("2026-10-19T22:00:00Z", "Europe/Amsterdam");
    List<DateGroup> _30secs = parser.parse("30 seconds");
    assertEquals(Instant.parse("2026-10-19T22:00:30Z"), Instant.from(_30secs.get(0).getTemporals().get(0)));
    assertEquals(ZonedDateTime.parse("2026-10-20T00:00:30+02:00[Europe/Amsterdam]"), _30secs.get(0).getTemporals().get(0));

    List<DateGroup> _20secs = parser.parse("20 seconds");
    assertEquals(Instant.parse("2026-10-19T22:00:20Z"), _20secs.get(0).getInstants().get(0));
    List<DateGroup> twentySecs = parser.parse("twenty seconds");
    assertEquals(Instant.parse("2026-10-19T22:00:20Z"), twentySecs.get(0).getInstants().get(0));
    List<DateGroup> _5secs = parser.parse("5 seconds");
    assertEquals(Instant.parse("2026-10-19T22:00:05Z"), _5secs.get(0).getInstants().get(0));
  }

  @Test
  public void parsesWithSqlDateReferences() {
    Parser parser = new Parser(ZoneId.of("UTC"));
    long referenceMillis = Instant.parse("2026-10-20T12:00:00Z").toEpochMilli();

    assertEquals(
      Instant.ofEpochMilli(referenceMillis + 1000),
      parser.parse("1 second", new java.sql.Date(referenceMillis)).get(0).getInstants().get(0));
    assertEquals(
      Instant.ofEpochMilli(referenceMillis + 1000),
      parser.parse("1 second", new java.sql.Time(referenceMillis)).get(0).getInstants().get(0));
  }

  @Test
  public void defaultNow() {
    Parser parser = new Parser("", "Europe/Amsterdam");
    assertEquals(ZoneId.of("Europe/Amsterdam"), parser.getClock().getZone());
    assertEquals(Instant.now().toEpochMilli(), parser.getClock().instant().toEpochMilli(), 100);
    assertEquals(Instant.now().toEpochMilli(), parser.parse("now").get(0).getInstants().get(0).toEpochMilli(), 100);
  }

  @Test
  public void empty() {
    Parser parser = new Parser("", "Europe/Amsterdam");
    assertEquals(0, parser.parse("").size());
  }


  private static Date date(int year, int month, int day) {
    return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
  }


}
