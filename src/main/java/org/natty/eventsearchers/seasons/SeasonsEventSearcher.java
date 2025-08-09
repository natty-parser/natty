package org.natty.eventsearchers.seasons;

import java.time.Instant;
import java.time.Year;
import java.util.stream.Stream;
import org.natty.EventSearcher;
import org.natty.Range;
import org.natty.Season;

/**
 * @since 1.1
 * @author Michiel Meeuwissen
 */
public class SeasonsEventSearcher implements EventSearcher<Instant> {
  @Override
  public Stream<Instant> findEvents(Range<Year> range, String eventSummary) {

    Season season = Season.fromSummary(eventSummary).orElse(null);
    if (season == null) {
      return Stream.empty();
    }
    return Range.stream(range).map(season);
  }
}
