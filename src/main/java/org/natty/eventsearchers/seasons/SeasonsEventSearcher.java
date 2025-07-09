package org.natty.eventsearchers.seasons;

import java.time.Instant;
import java.time.Year;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.natty.EventSearcher;
import org.natty.Range;
import org.natty.Season;

/**
 * @since 1.1
 * @author Michiel Meeuwissen
 */
public class SeasonsEventSearcher implements EventSearcher<Instant> {

  public static SeasonsEventSearcher INSTANCE = new SeasonsEventSearcher();
  @Override
  public Stream<Instant> findEvents(Range<Year> range, TimeZone timeZone, String eventSummary) {

    Season season = Season.fromSummary(eventSummary);

    return IntStream.range(range.getStart().getValue(), range.getEnd().getValue() + 1)
      .mapToObj(Year::of)
      .map(season)
      .filter(Optional::isPresent)
      .map(Optional::get);
  }
}
