package org.natty.eventsearchers.ics;

import java.time.LocalDate;
import java.time.Year;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.natty.EventSearcher;
import org.natty.Range;

/**
 * NOT USED
 */

class IcsHolidaysSearcher implements EventSearcher<LocalDate> {


  private static final Map<Range<Year>, String> HOLIDAY_ICS_FILES;
  static {
    Map<Range<Year>, String> icsFileNames = new HashMap<>();
    icsFileNames.put(Range.ofYears(2000, 2020), "/US/holidays-2000-2020.ics");
   /* icsFileNames.put(new Range<>(Year.of(2025), Year.of(2028)), "/US/holidays-2005-2028.ics");

    for (int i = 2021; i <= 2045; i++) {
      icsFileNames.put(new Range<>(Year.of(i), Year.of(i)), "/US/holidays-" + i + ".ics");
    }*/
    HOLIDAY_ICS_FILES = Collections.unmodifiableMap(icsFileNames);
  }


  @Override
  public Stream<LocalDate> findEvents(Range<Year> range, String eventSummary) {
    return HOLIDAY_ICS_FILES.entrySet().stream()
      .filter(
        entry -> entry.getKey().isConnected(range))
      .map(Map.Entry::getValue)
      .flatMap(icsFileName -> {
          IcsSearcher searcher = new IcsSearcher(icsFileName);
          return searcher.findTemporals(range.getStart().getValue(), range.getEnd().getValue(), eventSummary).values().stream();
        }
      ).map(temporal -> {
        ;
        if (temporal instanceof LocalDate) {
          return (LocalDate) temporal;
        } else {
          return null;
        }
      }).filter(Objects::nonNull);
  }
}
