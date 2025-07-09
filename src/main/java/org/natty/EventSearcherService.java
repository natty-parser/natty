package org.natty;

import java.time.Year;
import java.time.temporal.Temporal;
import java.util.TimeZone;
import java.util.stream.Stream;

public class EventSearcherService implements EventSearcher<Temporal> {


  @Override
  public Stream<Temporal> findEvents(Range<Year> range, TimeZone timeZone, String eventSummary) {
    return Stream.empty();
  }
}
