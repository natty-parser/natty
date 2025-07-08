package org.natty;

import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.lang3.SerializationUtils;

import static org.junit.Assert.assertEquals;

public class ParserTest {

  @Test
  public void testParser() {
    // Test serializing of a basic parser which does not have a
    // user-supplied TimeZone.
    Parser parser = new Parser();

    byte[] serialized = SerializationUtils.serialize(parser);
    Parser deserialized = SerializationUtils.deserialize(serialized);

    // Check the parser
    // Note: if a.equals(b), then a.hashcode() MUST equal b.hashcode()
    // see http://stackoverflow.com/a/27609 for more.
    assertEquals(parser, deserialized);
    assertEquals(parser.hashCode(), deserialized.hashCode());
  }

  @Test
  public void testParserTimeZone() {
    // TODO this could be improved by mocking TimeZone since it is not part
    // of the system under test.
    TimeZone defaultTimeZone = TimeZone.getDefault();

    // Test serializing of an alternate parser with a
    // user-supplied TimeZone.
    Parser parser = new Parser(defaultTimeZone);

    byte[] serialized = SerializationUtils.serialize(parser);
    Parser deserialized = (Parser) SerializationUtils.deserialize(serialized);

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
}
