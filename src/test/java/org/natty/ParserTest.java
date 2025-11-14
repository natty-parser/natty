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
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;


public class ParserTest {

  private static final Logger log  = Logger.getLogger(ParserTest.class.getName());

  @Test
  public void serializationOfParser() throws IOException, ClassNotFoundException {
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
  public void serializationOfParserTimeZone() throws IOException, ClassNotFoundException {
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
   * Test whether we have easter dates for a lot of years.
   */

  @Test
  public void lastEaster() {
    for (int i = 1900; i < 2100; i++) {
      Date reference = new Date(i - 1900, 1, 1);
      final List<DateGroup> groups = new Parser().parse("last easter", reference);
      assertEquals(1, groups.size());
      Date easter = groups.get(0).getDates().get(0);
      log.info("Last easter for year " + i + ": " +easter);
    }
  }

  @Test
  public void lastElectionDay() {
    for (int i = 2001; i < 2030; i++) {
      Date reference = new Date(i - 1900, 1, 1);
      final List<DateGroup> groups = new Parser().parse("last election day", reference);
      assertEquals(1, groups.size());
      Date easter = groups.get(0).getDates().get(0);
      log.info("Last election day for year " +  i + ": " + easter);
    }
  }


  /**
   * Just showing #279 now. Indeed, it doesn't seem to make much sense what is happening now.
   */

  @Test
  public void issue279() {
    Parser parser = new Parser();
    Date d2000  = new Date(100, 1, 1);
    Date d2010  = new Date(110, 1, 1);
    Date d2020  = new Date(120, 1, 1);

    log.info("Parsed date: " +
      parser.parse("Fri Mar 04 00:00:00 UTC 2016", d2000).get(0).getDates().get(0) +
      " (" + d2000 + ")");

    log.info("Parsed date: " +
      parser.parse("Fri Mar 04 00:00:00 UTC 2016", d2010).get(0).getDates().get(0) +
      " (" + d2010 + ")");

    log.info("Parsed date: " +
      parser.parse("Fri Mar 04 00:00:00 UTC 2016", d2020).get(0).getDates().get(0) +
      " (" + d2020 + ")");



    List<DateGroup> parse2 = parser.parse("Tue Jan 12 00:00:00 UTC 2016");

    log.info("Parsed date: " +  parse2.get(0).getDates().get(0));
  }

  /**
   * This used to give exceptions (because summer not found any more)
   */
  @Test
  public void issue277() {
    Parser parser = new Parser();
    List<DateGroup> parse1 = parser.parse("MIGUEL JESSIE REYEZ - QUEEN NAIJA - J.I.D - MASEGO - TIERRA WHACK SUMMER WALKER KIANA LEDE - SNOH AALEGRA - RAVEENA TOBI LOU - JESS CONNELLY UMI - DAVEB IVY SOLE - PARISALEXA");
    log.info("Parsed date: " + parse1.get(0).getDates().get(0));
  }

  @Test
  public void issue246() {
    Parser parser = new Parser();
    List<DateGroup> parsed = parser.parse("the day before next 10/11/2016");
    log.info("Parsed date: " + parsed.get(0).getDates().get(0));
  }


}
