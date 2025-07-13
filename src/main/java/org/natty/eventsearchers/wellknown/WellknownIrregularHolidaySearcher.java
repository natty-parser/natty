package org.natty.eventsearchers.wellknown;

import java.time.LocalDate;
import java.time.Year;
import java.util.stream.Stream;
import org.natty.EventSearcher;
import org.natty.Range;

/**

 * @since 1.1
 * @author Michiel Meeuwissen
 */
public class WellknownIrregularHolidaySearcher implements EventSearcher<LocalDate> {


  public WellknownIrregularHolidaySearcher() {

  }



  @Override
  public Stream<LocalDate> findEvents(Range<Year> range, String eventSummary) {
    WellknownIrregularHoliday holiday = WellknownIrregularHoliday.fromSummary(eventSummary)
        .orElse(null);
    if (holiday == null) {
      return Stream.empty();
    }
     return Range.stream(range).flatMap(y -> holiday.apply(y).stream());
  }
}
