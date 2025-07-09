package org.natty;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;


public class ParserTest {

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ParserTest.class);
  @Test
  public void testParser() throws IOException, ClassNotFoundException {
    // Test serializing of a basic parser which does not have a
    // user-supplied TimeZone.
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
  public void testParserTimeZone() throws IOException, ClassNotFoundException {
    // TODO this could be improved by mocking TimeZone since it is not part
    // of the system under test.
    TimeZone defaultTimeZone = TimeZone.getDefault();

    // Test serializing of an alternate parser with a
    // user-supplied TimeZone.
    Parser parser = new Parser(defaultTimeZone);

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
    final List<DateGroup> groups = new Parser().parse("last easter", new Date(121, 1, 1));


  }

  /**
   * Still fails after 2020, because easter is not an holiday in the US.
   */

  @Test
  public void lastEaster() {
    for (int i = 2001; i < 2030; i++) {
      Date reference = new Date(i - 1900, 1, 1);
      final List<DateGroup> groups = new Parser().parse("last easter", reference);
      assertEquals(1, groups.size());
      Date easter = groups.get(0).getDates().get(0);
      log.info("Last easter for year {}: {}", i, easter);
    }
  }

  @Test
  public void lastElectionDay() {
    for (int i = 2001; i < 2030; i++) {
      Date reference = new Date(i - 1900, 1, 1);
      final List<DateGroup> groups = new Parser().parse("last election day", reference);
      assertEquals(1, groups.size());
      Date easter = groups.get(0).getDates().get(0);
      log.info("Last election day for year {}: {}", i, easter);
    }
  }

  @Test
  public void issue279() {
    Parser parser = new Parser();

    List<DateGroup> parse1 = parser.parse("Fri Mar 04 00:00:00 UTC 2016", new Date(100, 1, 1));
    log.info("Parsed date: {}", parse1.get(0).getDates().get(0));



    List<DateGroup> parse2 = parser.parse("Tue Jan 12 00:00:00 UTC 2016");

    log.info("Parsed date: {}", parse2.get(0).getDates().get(0));
  }
}
