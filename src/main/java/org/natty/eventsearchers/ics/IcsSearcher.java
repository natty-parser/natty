package org.natty.eventsearchers.ics;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.natty.WalkerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Content;
import net.fortuna.ical4j.model.Period;

/**
 *
 */
class IcsSearcher {


  private static final String GMT = "GMT";
  private static final String VEVENT = "VEVENT";
  private static final String SUMMARY = "SUMMARY";
  private static final Logger _logger = LoggerFactory.getLogger(IcsSearcher.class);
  private net.fortuna.ical4j.model.Calendar _holidayCalendar;
  private final String _calendarFileName;


  public IcsSearcher(String calendarFileName) {
    _calendarFileName = calendarFileName;
  }



  public Map<Integer, Temporal> findTemporals(int startYear, int endYear, String eventSummary) {


    if(_holidayCalendar == null) {
      InputStream fin = WalkerState.class.getResourceAsStream(_calendarFileName);
      try {
        _holidayCalendar = new CalendarBuilder().build(fin);

      } catch (IOException e) {
        _logger.error("Couldn't open {}",  _calendarFileName);
        return Collections.emptyMap();

      } catch (ParserException e) {
        _logger.error("Couldn't parse {}", _calendarFileName);
        return Collections.emptyMap();
      }
    }
    Map<Integer, Temporal> holidays = new HashMap<>();
    final Period<LocalDateTime> period;
    try {

      LocalDateTime from = LocalDate.of(startYear, 1, 1).atStartOfDay();
      LocalDateTime to =  LocalDate.of(endYear, 12, 31).atStartOfDay();
      period = new Period<>(from, to);

    } catch (DateTimeParseException e) {
      _logger.error("Invalid start or end year: {}, {}", startYear, endYear, e);
      return holidays;
    }

    for (Component vevent : _holidayCalendar.getComponents(VEVENT)) {
      String summary = vevent.getProperty(SUMMARY).map(Content::getValue).orElse(null);
      if(summary.equals(eventSummary)) {
        Set<Period<Temporal>> list = vevent.calculateRecurrenceSet(period);
        for(Period<Temporal> p : list) {
          Temporal date = p.getStart();
          holidays.put(date.get(ChronoField.YEAR), date);
        }
      }
    }

    return holidays;
  }




}
