package org.natty.eventsearchers;

import org.junit.Test;

import java.time.Year;
import java.util.stream.Stream;
import org.natty.eventsearchers.christian.ChristianHoliday;

public class ChristianHolidaysTest  {

  @Test
  public void testChristianHolidays() {

    Stream.of(ChristianHoliday.values()).forEach(holiday -> {
      System.out.println(holiday.getSummary() + " on " + holiday.apply(Year.of(2025)));
    });
  }

}
