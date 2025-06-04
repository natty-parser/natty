package org.natty;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Runs the parser through the various time formats
 *
 * @author Joe Stelmach
 */
class TimeTest extends AbstractTest {
    @BeforeAll
    public static void oneTime() {
        Locale.setDefault(Locale.US);
        TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));
        initCalendarAndParser();
    }

    /**
     * Runs the parser through the various time formats
     */
    @Test
    void testFormal() throws Exception {
        Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("1/02/2011");
        calendarSource = new CalendarSource(reference);
        validateTime(reference, "0600h", 6, 0, 0);
        validateTime(reference, "06:00h", 6, 0, 0);
        validateTime(reference, "06:00 hours", 6, 0, 0);
        validateTime(reference, "0000", 0, 0, 0);
        validateTime(reference, "0700h", 7, 0, 0);
        validateTime(reference, "6pm", 18, 0, 0);
        validateTime(reference, "5:30 a.m.", 5, 30, 0);
        validateTime(reference, "5", 5, 0, 0);
        validateTime(reference, "12:59", 12, 59, 0);
        validateTime(reference, "23:59:28", 23, 59, 28);
        validateTime(reference, "00:00", 0, 0, 0);
        validateTime(reference, "10:00am", 10, 0, 0);
        validateTime(reference, "10a", 10, 0, 0);
        validateTime(reference, "10am", 10, 0, 0);
        validateTime(reference, "10a_m", 10, 0, 0);
        validateTime(reference, "10", 10, 0, 0);
        validateTime(reference, "8p", 20, 0, 0);
        validateTime(reference, "8pm", 20, 0, 0);
        validateTime(reference, "8 pm", 20, 0, 0);
        validateTime(reference, "8 p_m", 20, 0, 0);
    }

    @Test
    void testRelaxed() throws Exception {
        Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("1/02/2011");
        calendarSource = new CalendarSource(reference);
        validateTime(reference, "noon", 12, 0, 0);
        validateTime(reference, "at noon", 12, 0, 0);
        validateTime(reference, "afternoon", 12, 0, 0);
        validateTime(reference, "midnight", 0, 0, 0);
        validateTime(reference, "mid-night", 0, 0, 0);
        validateTime(reference, "6 in the morning", 6, 0, 0);
        validateTime(reference, "4 in the afternoon", 16, 0, 0);
        validateTime(reference, "evening", 19, 0, 0);
        validateTime(reference, "10 hours before noon", 2, 0, 0);
        validateTime(reference, "10 hours before midnight", 14, 0, 0);
        validateTime(reference, "5 hours after noon", 17, 0, 0);
        validateTime(reference, "5 hours after midnight", 5, 0, 0);
        validateTime(reference, "tonight", 20, 0, 0);
    }

    @Test
    void testRelative() throws Exception {
        Date reference = DateFormat.getTimeInstance(DateFormat.SHORT).parse("12:00 pm");
        calendarSource = new CalendarSource(reference);
        validateTime(reference, "in 5 seconds", 12, 0, 5);
        validateTime(reference, "in 5 minutes", 12, 5, 0);
        validateTime(reference, "in 5 hours", 17, 0, 0);
        validateTime(reference, "4 seconds from now", 12, 0, 4);
        validateTime(reference, "4 minutes from now", 12, 4, 0);
        validateTime(reference, "4 hours from now", 16, 0, 0);
        validateTime(reference, "next minute", 12, 1, 0);
        validateTime(reference, "last minute", 11, 59, 0);
        validateTime(reference, "next second", 12, 0, 1);
        validateTime(reference, "this second", 12, 0, 0);
        validateTime(reference, "this minute", 12, 0, 0);
        validateTime(reference, "this hour", 12, 0, 0);
    }

    @Test
    public void testAlternatives() throws Exception {
        Date reference = DateFormat.getTimeInstance(DateFormat.SHORT).parse("12:00 pm");
        calendarSource = new CalendarSource(reference);

        List<Date> dates = parseCollection(reference, "12 or 12:30");
        assertThat(dates).hasSize(2);
        validateTime(dates.get(0), 12, 0, 0);
        validateTime(dates.get(1), 12, 30, 0);

        dates = parseCollection(reference, "12pm or 12:30");
        assertThat(dates).hasSize(2);
        validateTime(dates.get(0), 12, 0, 0);
        validateTime(dates.get(1), 12, 30, 0);

        dates = parseCollection(reference, "noon or 12:30");
        assertThat(dates).hasSize(2);
        validateTime(dates.get(0), 12, 0, 0);
        validateTime(dates.get(1), 12, 30, 0);

        dates = parseCollection(reference, "12 or 12:30am");
        assertThat(dates).hasSize(2);
        validateTime(dates.get(0), 0, 0, 0);
        validateTime(dates.get(1), 0, 30, 0);
    }

    @Test
    void testRange() throws Exception {
        Date reference = DateFormat.getTimeInstance(DateFormat.SHORT).parse("12:00 pm");
        calendarSource = new CalendarSource(reference);

        List<Date> dates = parseCollection(reference, "for six hours");
        assertThat(dates).hasSize(2);
        validateTime(dates.get(0), 12, 0, 0);
        validateTime(dates.get(1), 18, 0, 0);

        dates = parseCollection(reference, "for 12 minutes");
        assertThat(dates).hasSize(2);
        validateTime(dates.get(0), 12, 0, 0);
        validateTime(dates.get(1), 12, 12, 0);

        dates = parseCollection(reference, "for 10 seconds");
        assertThat(dates).hasSize(2);
        validateTime(dates.get(0), 12, 0, 0);
        validateTime(dates.get(1), 12, 0, 10);
    }

    @Test
    void testText() throws Exception {
        Date reference = DateFormat.getTimeInstance(DateFormat.SHORT).parse("12:00 pm");

        List<DateGroup> groups = parser.parse("5.30pm", reference);
        assertThat(groups).hasSize(1);
        DateGroup group = groups.get(0);
        assertThat(group.getDates()).hasSize(1);
        validateTime(group.getDates().get(0), 17, 30, 0);
        assertThat(group.getText()).isEqualTo("5.30pm");

        groups = parser.parse("5:30pm", reference);
        assertThat(groups).hasSize(1);
        group = groups.get(0);
        assertThat(group.getDates()).hasSize(1);

        validateTime(group.getDates().get(0), 17, 30, 0);
        assertThat(group.getText()).isEqualTo("5:30pm");

    }
}
