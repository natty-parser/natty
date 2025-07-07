package org.natty;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Content;
import net.fortuna.ical4j.model.Period;

public class IcsSearcher {
  private static final String GMT = "GMT";
  private static final String VEVENT = "VEVENT";
  private static final String SUMMARY = "SUMMARY";
  private static final Logger _logger = LoggerFactory.getLogger(IcsSearcher.class);
  private net.fortuna.ical4j.model.Calendar _holidayCalendar;
  private final String _calendarFileName;
  private final TimeZone _timeZone;
  private final CalendarSource calendarSource;

  public IcsSearcher(String calendarFileName, TimeZone timeZone, Date referenceDate) {
    calendarSource = new CalendarSource(referenceDate);
    _calendarFileName = calendarFileName;
    _timeZone = timeZone;
  }

  public IcsSearcher(String calendarFileName, TimeZone timeZone) {
    this(calendarFileName, timeZone, new Date());
  }

  public Map<Integer, Date> findDates(int startYear, int endYear, String eventSummary) {
    Map<Integer, Date> holidays = new HashMap<Integer, Date>();

    if(_holidayCalendar == null) {
      InputStream fin = WalkerState.class.getResourceAsStream(_calendarFileName);
      try {
        _holidayCalendar = new CalendarBuilder().build(fin);

      } catch (IOException e) {
        _logger.error("Couln't open {}",  _calendarFileName);
        return holidays;

      } catch (ParserException e) {
        _logger.error("Couln't parse {}", _calendarFileName);
        return holidays;
      }
    }

    final Period<LocalDateTime> period;
    try {

      LocalDateTime from = LocalDate.of(startYear, 1, 1).atStartOfDay();
      LocalDateTime to =  LocalDate.of(endYear, 12, 31).atStartOfDay();
      period = new Period<>(from, to);

    } catch (DateTimeParseException e) {
      _logger.error("Invalid start or end year: {}, {}", startYear, endYear, e);
      return holidays;
    }

    for (Component  vevent : _holidayCalendar.getComponents(VEVENT)) {
      String summary = vevent.getProperty(SUMMARY).map(Content::getValue).orElse(null);
      if(summary.equals(eventSummary)) {
        Set<Period<Temporal>> list = vevent.calculateRecurrenceSet(period);
        for(Period<Temporal> p : list) {
          Temporal date = p.getStart();

          // this date is at the date of the holiday at 12 AM UTC
          Calendar utcCal = calendarSource.getCurrentCalendar();
          utcCal.setTimeZone(TimeZone.getTimeZone(GMT));
          if (date instanceof LocalDate) {
            utcCal.setTime(Date.from(((LocalDate) date).atStartOfDay(ZoneOffset.UTC).toInstant()));
          } else if (date instanceof OffsetDateTime) {
            utcCal.setTime(Date.from(((OffsetDateTime) date).toInstant()));
          } else {
            _logger.warn("Unsupported date type: " + date.getClass().getName());
            continue;
          }

          // use the year, month and day components of our UTC date to form a new local date
          Calendar localCal = calendarSource.getCurrentCalendar();
          localCal.setTimeZone(_timeZone);
          localCal.set(Calendar.YEAR, utcCal.get(Calendar.YEAR));
          localCal.set(Calendar.MONTH, utcCal.get(Calendar.MONTH));
          localCal.set(Calendar.DAY_OF_MONTH, utcCal.get(Calendar.DAY_OF_MONTH));

          holidays.put(localCal.get(Calendar.YEAR), localCal.getTime());
        }
      }
    }

    return holidays;
  }

}
