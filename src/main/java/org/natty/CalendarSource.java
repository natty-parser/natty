package org.natty;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Responsible for generating new Calendars that represent
 * the current point in time.  This is necessary so we can
 * manipulate what the software thinks is the 'current'
 * time, which may be different from the system time
 *
 * @author Joe Stelmach
 */
public class CalendarSource {
  private final Date referenceDate;

  public CalendarSource() {
    this.referenceDate = new Date();
  }

  public CalendarSource(Date referenceDate) {
    this.referenceDate = referenceDate;
  }

  public GregorianCalendar getCurrentCalendar() {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(referenceDate);
    return calendar;
  }
}
