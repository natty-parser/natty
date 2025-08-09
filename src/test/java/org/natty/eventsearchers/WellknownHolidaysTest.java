package org.natty.eventsearchers;

import org.junit.Assert;
import org.junit.Test;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.natty.eventsearchers.wellknown.WellknownHoliday;

public class WellknownHolidaysTest {

  @Test
  public void testWellknownHolidays() {

    List<String> strings = new ArrayList<>();
    Stream.of(WellknownHoliday.values()).forEach(holiday -> {
      strings.add(holiday.getSummary() + " on " + holiday.apply(Year.of(2025)));
    });

    Stream.of(WellknownHoliday.values()).forEach(holiday -> {
      strings.add(holiday.getSummary() + " on " + holiday.apply(Year.of(2012)));
    });
    Assert.assertEquals(Arrays.asList(
     "Earth Day on 2025-04-22",
    "April Fool's Day on 2025-04-01",
    "Thanksgiving Day on 2025-11-27",
    "Black Friday on 2025-11-28",
    "Columbus Day (US-OPM) on 2025-10-13",
    "Groundhog's Day on 2025-02-02",
    "New Year's Day on 2025-01-01",
    "New Year's Eve on 2024-12-31",
    "Father's Day on 2025-06-15",
    "Mother's Day on 2025-05-11",
    "Veteran's Day on 2025-11-11",
    "Memorial Day on 2025-05-26",
    "Flag Day on 2025-06-14",
    "Halloween on 2025-10-31",
    "Independence Day on 2025-07-04",
    "Kwanzaa on 2025-12-26",
    "Labor Day on 2025-09-01",
    "Martin Luther King Jr.'s Day on 2025-01-20",
    "Patriot Day on 2025-09-11",
    "President's Day on 2025-02-17",
    "St. Patrick's Day on 2025-03-17",
    "Tax Day on 2025-04-15",
    "Valentine's Day on 2025-02-14",
    "Earth Day on 2012-04-22",
    "April Fool's Day on 2012-04-01",
    "Thanksgiving Day on 2012-11-22",
    "Black Friday on 2012-11-23",
    "Columbus Day (US-OPM) on 2012-10-08",
    "Groundhog's Day on 2012-02-02",
    "New Year's Day on 2012-01-01",
    "New Year's Eve on 2011-12-31",
    "Father's Day on 2012-06-17",
    "Mother's Day on 2012-05-13",
    "Veteran's Day on 2012-11-11",
    "Memorial Day on 2012-05-28",
    "Flag Day on 2012-06-14",
    "Halloween on 2012-10-31",
    "Independence Day on 2012-07-04",
    "Kwanzaa on 2012-12-26",
    "Labor Day on 2012-09-03",
    "Martin Luther King Jr.'s Day on 2012-01-16",
    "Patriot Day on 2012-09-11",
    "President's Day on 2012-02-20",
    "St. Patrick's Day on 2012-03-17",
    "Tax Day on 2012-04-15",
    "Valentine's Day on 2012-02-14"
    ), strings);
  }

}
