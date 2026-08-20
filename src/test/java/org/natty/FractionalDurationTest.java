package org.natty;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.Locale.US;

/**
 * Tests that fractional duration expressions like "1.5 days" are parsed correctly.
 */
public class FractionalDurationTest extends AbstractTest {

  @BeforeClass
  public static void oneTime() {
    Locale.setDefault(US);
    TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));
    initCalendarAndParser();
  }

  // ---- normalizeFractionalDurations unit tests ----

  @Test
  public void testNormalize_days() {
    Assert.assertEquals("36 hours", Parser.normalizeFractionalDurations("1.5 days"));
    Assert.assertEquals("12 hours", Parser.normalizeFractionalDurations("0.5 days"));
    Assert.assertEquals("48 hours", Parser.normalizeFractionalDurations("2.0 days"));
  }

  @Test
  public void testNormalize_hours() {
    Assert.assertEquals("150 minutes", Parser.normalizeFractionalDurations("2.5 hours"));
    Assert.assertEquals("30 minutes",  Parser.normalizeFractionalDurations("0.5 hours"));
  }

  @Test
  public void testNormalize_weeks() {
    Assert.assertEquals("4 days",  Parser.normalizeFractionalDurations("0.5 weeks"));
    Assert.assertEquals("11 days", Parser.normalizeFractionalDurations("1.5 weeks"));
  }

  @Test
  public void testNormalize_minutes() {
    Assert.assertEquals("90 seconds", Parser.normalizeFractionalDurations("1.5 minutes"));
    Assert.assertEquals("30 seconds", Parser.normalizeFractionalDurations("0.5 minutes"));
  }

  @Test
  public void testNormalize_months() {
    Assert.assertEquals("45 days", Parser.normalizeFractionalDurations("1.5 months"));
  }

  @Test
  public void testNormalize_years() {
    Assert.assertEquals("6 months", Parser.normalizeFractionalDurations("0.5 years"));
    Assert.assertEquals("18 months", Parser.normalizeFractionalDurations("1.5 years"));
  }

  @Test
  public void testNormalize_noChange_integers() {
    Assert.assertEquals("3 days", Parser.normalizeFractionalDurations("3 days"));
    Assert.assertEquals("2 hours", Parser.normalizeFractionalDurations("2 hours"));
  }

  @Test
  public void testNormalize_preservesSurroundingText() {
    Assert.assertEquals("in 36 hours from now",
        Parser.normalizeFractionalDurations("in 1.5 days from now"));
    Assert.assertEquals("36 hours ago",
        Parser.normalizeFractionalDurations("1.5 days ago"));
  }

  // ---- end-to-end parse tests ----

  @Test
  public void testParse_fractionalDays() throws Exception {
    // Reference: Feb 28, 2011 midnight (US/Eastern)
    Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("2/28/2011");
    calendarSource = new CalendarSource(reference);

    // "1.5 days from now" → "36 hours from now" → Feb 28 00:00 + 36h = March 1 12:00 → March 1
    validateDate(reference, "1.5 days from now", 3, 1, 2011);

    // "1.5 days ago" → "36 hours ago" → Feb 28 00:00 - 36h = Feb 26 12:00 → Feb 26
    validateDate(reference, "1.5 days ago", 2, 26, 2011);
  }

  @Test
  public void testParse_fractionalWeeks() throws Exception {
    Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("2/28/2011");
    calendarSource = new CalendarSource(reference);

    // "1.5 weeks from now" → "11 days from now" → Feb 28 + 11 = March 11
    validateDate(reference, "1.5 weeks from now", 3, 11, 2011);
  }

  @Test
  public void testParse_inFractionalDays() throws Exception {
    Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("2/28/2011");
    calendarSource = new CalendarSource(reference);

    // "in 1.5 days" → "in 36 hours" → Feb 28 00:00 + 36h = March 1 12:00 → March 1
    validateDate(reference, "in 1.5 days", 3, 1, 2011);
  }
}
