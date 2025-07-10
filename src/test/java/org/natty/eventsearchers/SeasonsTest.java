package org.natty.eventsearchers;

import org.junit.Assert;
import org.junit.Test;

import java.time.Year;
import java.time.ZoneId;
import java.util.Map;
import java.util.stream.Stream;
import org.natty.EventSearcherService;
import org.natty.Range;
import org.natty.Season;
import org.natty.eventsearchers.seasons.SeasonsEventSearcher;

import static org.natty.Season.*;

public class SeasonsTest {

  static final Map<Integer, Map<Season, String>> EXPECTED = Map.of(2025, Map.of(
    SPRING, "2025-03-20T09:00:42Z",
    SUMMER, "2025-06-21T02:31:08Z",
    FALL, "2025-09-22T18:22:23Z",
    WINTER, "2025-12-21T15:01:41Z"
  ));
  @Test
  public void seasons() {

    Stream.of(Season.values()).forEach(season -> {
      Assert.assertEquals( EXPECTED.get(2025).get(season), season.apply(Year.of(2025)).toString());
    });
  }

  @Test
  public void service() {
    SeasonsEventSearcher seasonsEventSearcher = EventSearcherService.INSTANCE.getEventSearcher(SeasonsEventSearcher.class);
    seasonsEventSearcher.findEvents(Range.ofYears(1900, 2100), SPRING.getSummary())
      .forEach(event -> {
        System.out.println(SPRING + ": " +event);
        int year = event.atZone(ZoneId.of("UTC")).getYear();
        Map<Season, String> expected = EXPECTED.get(year);
        if (expected != null) {
          Assert.assertEquals(expected.get(SPRING), event.toString());
        }
      });

  }
}
