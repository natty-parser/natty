package org.natty;

import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Joe Stelmach
 */
public abstract class AbstractTest {
    private static Calendar calendar;
    protected static CalendarSource calendarSource;
    protected static Parser parser;


    public static void initCalendarAndParser() {
        calendar = Calendar.getInstance();
        parser = new Parser();
    }

    /**
     * Resets the calendar source time before each test
     */
    @BeforeEach
    public void before() {
        calendarSource = new CalendarSource();
    }

    /**
     * Parses the given value into a collection of dates
     */
    protected List<Date> parseCollection(Date referenceDate, String value) {
        List<DateGroup> dateGroup = parser.parse(value, referenceDate);
        return dateGroup.isEmpty() ? new ArrayList<Date>() : dateGroup.get(0).getDates();
    }

    /**
     * Parses the given value, asserting that one and only one date is produced.
     */
    protected Date parseSingleDate(String value, Date referenceDate) {
        List<Date> dates = parseCollection(referenceDate, value);
        assertThat(dates.size()).isEqualTo(1);
        return dates.get(0);
    }

    /**
     * Asserts that the given string value parses down to the given
     * month, day, and year values.
     */
    protected void validateDate(Date referenceDate, String value, int month, int day, int year) {
        Date date = parseSingleDate(value, referenceDate);
        validateDate(date, month, day, year);
    }

    protected void validateDate(String value, int month, int day, int year) {
        validateDate(new Date(), value, month, day, year);
    }

    /**
     * Asserts that the given date contains the given attributes
     */
    protected void validateDate(Date date, int month, int day, int year) {
        calendar.setTime(date);
        assertThat(calendar.get(Calendar.MONTH)).isEqualTo(month - 1);
        assertThat(calendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(day);
        assertThat(calendar.get(Calendar.YEAR)).isEqualTo(year);
    }

    /**
     * Asserts that the given string value parses down to the given
     * hours, minutes, and seconds
     */
    protected void validateTime(Date referenceDate, String value, int hours, int minutes, int seconds) {
        Date date = parseSingleDate(value, referenceDate);
        validateTime(date, hours, minutes, seconds);
    }

    /**
     * Asserts that the given date contains the given time attributes
     */
    protected void validateTime(Date date, int hours, int minutes, int seconds) {
        calendar.setTime(date);
        assertThat(calendar.get(Calendar.HOUR_OF_DAY)).isEqualTo(hours);
        assertThat(calendar.get(Calendar.MINUTE)).isEqualTo(minutes);
        assertThat(calendar.get(Calendar.SECOND)).isEqualTo(seconds);
    }

    /**
     * Asserts that the given string value parses down to the given
     * month, day, year, hours, minutes, and seconds
     */
    protected void validateDateTime(Date referenceDate, String value, int month, int day, int year,
                                    int hours, int minutes, int seconds) {

        Date date = parseSingleDate(value, referenceDate);
        validateDateTime(date, month, day, year, hours, minutes, seconds);
    }

    /**
     * Asserts that the given date contains the given attributes
     */
    protected void validateDateTime(Date date, int month, int day, int year,
                                    int hours, int minutes, int seconds) {

        calendar.setTime(date);

        assertThat(calendar.get(Calendar.MONTH)).isEqualTo(month - 1);
        assertThat(calendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(day);
        assertThat(calendar.get(Calendar.YEAR)).isEqualTo(year);
        assertThat(calendar.get(Calendar.HOUR_OF_DAY)).isEqualTo(hours);
        assertThat(calendar.get(Calendar.MINUTE)).isEqualTo(minutes);
        assertThat(calendar.get(Calendar.SECOND)).isEqualTo(seconds);
    }
}
