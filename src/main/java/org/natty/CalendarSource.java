package org.natty;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Responsible for generating new Calendars that represent
 * the current point in time.  This is necessary, so we can
 * manipulate what the software thinks is the 'current'
 * time, which may be different from the system time
 *
 * @author Joe Stelmach
 */
public class CalendarSource {
  private ZonedDateTime referenceDate;
  private TimeZone timeZone;

  CalendarSource() {
    this(ZonedDateTime.now());
  }
  CalendarSource(ZonedDateTime referenceDate) {
    this.referenceDate = referenceDate;
    this.timeZone = TimeZone.getTimeZone(referenceDate.getZone());
  }
  CalendarSource(Instant referenceDate) {
    this(referenceDate.atZone(ZoneId.systemDefault()));
  }

  TimeZone getTimeZone() {
    return timeZone;
  }

  void  setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }


  public GregorianCalendar getCurrentCalendar() {
    if (referenceDate == null) {
      return null;
    }
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(Date.from(referenceDate.toInstant()));
    calendar.setTimeZone(getTimeZone());
    return calendar;
  }


  static ZonedDateTime toZonedDateTime(GregorianCalendar calendar) {
    try {
      return calendar.toZonedDateTime();
    } catch (DateTimeException e) {
      return calendar.toInstant().atZone(getZoneId(calendar));
    }
  }

  static ZoneId getZoneId(Calendar calendar) {
    return getZoneId(calendar.getTimeZone(), calendar.toInstant());
  }
  static ZoneId getZoneId(TimeZone timeZone, Instant referenceDate) {
    try {
      return timeZone.toZoneId();
    } catch (DateTimeException e) {
      int offset = timeZone.getOffset(referenceDate.toEpochMilli());
      return ZoneOffset.ofTotalSeconds(offset / 1000);
    }
  }
}
