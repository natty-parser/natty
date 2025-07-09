package org.natty.eventsearchers;

import org.junit.Test;

import java.time.Year;
import java.util.stream.Stream;
import org.natty.EventSearcherService;
import org.natty.Range;
import org.natty.Season;
import org.natty.eventsearchers.seasons.SeasonsEventSearcher;

public class SeasonsTest {

  @Test
  public void seasons() {

    Stream.of(Season.values()).forEach(season -> {
      System.out.println(season.getSummary() + " on " + season.apply(Year.of(2025)));
    });
  }

  @Test
  public void service() {
    SeasonsEventSearcher seasonsEventSearcher = EventSearcherService.INSTANCE.getEventSearcher(SeasonsEventSearcher.class);

    seasonsEventSearcher.findEvents(Range.ofYears(1900, 2100), Season.SPRING.getSummary())
      .forEach(event -> System.out.println(Season.SPRING + ": " +event));

  }
}
