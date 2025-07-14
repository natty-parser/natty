package org.natty.eventsearchers.wellknown;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import org.natty.EventSearcherService;
import org.natty.Range;


public class WellknownIrregularHolidaySearcherTest  {


  @Test
  public void inaugurations() {

    for (int year = 1750; year <= 2080; year++) {
      List<LocalDate> apply = WellknownIrregularHoliday.INAUGURATION_DAY.apply(Year.of(year));
      if (apply.isEmpty()) {
        continue;
      }
      System.out.println("inauguration: " + apply);
    }

  }


  @Test
  public void searcher() {
    WellknownIrregularHolidaySearcher searcher = new WellknownIrregularHolidaySearcher();

    searcher.findEvents(Range.ofYears(1750, 2080), "inauguration day")
      .forEach(System.out::println);


  }

  @Test
  public void service() {


    EventSearcherService.INSTANCE.findEvents(Range.ofYears(2025, 1750), "inauguration day")
      .forEach(System.out::println);


  }

}
