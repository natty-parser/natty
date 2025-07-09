package org.natty.eventsearchers;

import org.junit.Test;

import java.time.Year;
import java.util.stream.Stream;
import org.natty.eventsearchers.wellknown.WellknownHoliday;

public class WellknownHolidaysTest {

  @Test
  public void testWellknownHolidays() {

    Stream.of(WellknownHoliday.values()).forEach(holiday -> {
      System.out.println(holiday.getSummary() + " on " + holiday.apply(Year.of(2025)));
    });
  }

}
