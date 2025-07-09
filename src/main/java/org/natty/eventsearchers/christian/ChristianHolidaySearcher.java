package org.natty.eventsearchers.christian;

import java.time.LocalDate;
import java.time.Year;
import org.natty.YearlyHoliday;
import org.natty.eventsearchers.AbstractYearlyHolidayEventSearcher;

/**
 * (Western) Christian holidays, such as Easter, Christmas, and Pentecost.
 * @since 1.1
 * @author Michiel Meeuwissen
 */
public class ChristianHolidaySearcher extends AbstractYearlyHolidayEventSearcher {

  public static final ChristianHolidaySearcher INSTANCE = new ChristianHolidaySearcher();

  private ChristianHolidaySearcher() {
    // Private constructor to enforce singleton pattern
  }



  public static LocalDate easter(Year year) {
    return computeEasterDateWithButcherMeeusAlgorithm(year.getValue());
  }

  @Override
  public YearlyHoliday fromSummary(String eventSummary) {
    return ChristianHoliday.fromSummary(eventSummary);
  }


  static LocalDate computeEasterDateWithButcherMeeusAlgorithm(int year) {
    int a = year % 19;
    int b = year / 100;
    int c = year % 100;
    int d = b / 4;
    int e = b % 4;
    int f = (b + 8) / 25;
    int g = (b - f + 1) / 3;
    int h = (19*a + b - d - g + 15) % 30;
    int i = c / 4;
    int k = c % 4;
    int l = (2*e + 2*i - h - k + 32) % 7;
    int m = (a + 11*h + 22*l) / 451;
    int t = h + l - 7*m + 114;
    int n = t / 31;
    int o = t % 31;
    return LocalDate.of(year, n, o+1);
  }
}
