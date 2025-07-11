package org.natty.eventsearchers;

import java.time.LocalDate;
import java.time.Year;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.natty.EventSearcher;
import org.natty.Range;
import org.natty.YearlyHoliday;

public abstract class AbstractYearlyHolidayEventSearcher implements EventSearcher<LocalDate> {


  public abstract Optional<? extends YearlyHoliday> fromSummary(String eventSummary);

  @Override
  public Stream<LocalDate> findEvents(Range<Year> range, String eventSummary) {
    YearlyHoliday holiday = fromSummary(eventSummary).orElse(null);
    if (holiday == null) {
      return Stream.empty();
    }

    return IntStream.range(range.getStart().getValue(), range.getEnd().getValue() + 1)
      .mapToObj(Year::of)
      .map(holiday);
  }
}
