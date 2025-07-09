package org.natty.eventsearchers;

import org.junit.Test;

import java.time.Year;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import org.apache.commons.lang3.Range;
import org.natty.EventSearcher;
import org.natty.Season;

public class SeasonsTest {

  @Test
  public void seasons() {

    Stream.of(Season.values()).forEach(season -> {
      System.out.println(season.getSummary() + " on " + season.apply(Year.of(2025)));
    });
  }

  @Test
  public void service() {
    ServiceLoader<EventSearcher> loader = ServiceLoader.load(EventSearcher.class);
    loader.forEach(eventSearcher -> {
      System.out.println("Found event searcher: " + eventSearcher.getClass().getName());
      eventSearcher.findEvents(new Range<>(Year.of(2020), Year.of(2025)), null, "Spring")
          .forEach(event -> System.out.println("Event found: " + event));
    });
  }
}
