package org.natty.eventsearchers.wellknown;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.natty.YearlyHoliday;

/**
  * @since 1.1
  * @author Michiel Meeuwissen
 */
public enum WellknownHoliday implements YearlyHoliday {
  /** April 22nd, promoting environmental protection. */
  EARTH_DAY("Earth Day", (year) -> LocalDate.of(year.getValue(), 4, 22)),

  /** April 1st, known for pranks and jokes. */
  APRIL_FOOLS_DAY("April Fool's Day", (year) -> LocalDate.of(year.getValue(), 4, 1)),

  /** Fourth Thursday in November, giving thanks and feasting. */
  THANKSGIVING("Thanksgiving Day", (year) -> {
    LocalDate firstDayOfNovember = LocalDate.of(year.getValue(), 11, 1);
    int daysToAdd = (DayOfWeek.THURSDAY.getValue() - firstDayOfNovember.getDayOfWeek().getValue() + 7) % 7 + 21;
    return firstDayOfNovember.plusDays(daysToAdd);
  }),

  /** The Friday following Thanksgiving, known for major retail sales. */
  BLACK_FRIDAY("Black Friday", (year) -> THANKSGIVING.dateFunction.apply(year).plusDays(1)),

  /** Second Monday in October, commemorating Christopher Columbus's landing. */
  COLUMBUS_DAY("Columbus Day (US-OPM)", (year) -> {
    LocalDate firstDayOfOctober = LocalDate.of(year.getValue(), 10, 1);
    int daysToAdd = (DayOfWeek.MONDAY.getValue() - firstDayOfOctober.getDayOfWeek().getValue() + 7) % 7 + 7;
    return firstDayOfOctober.plusDays(daysToAdd);
  }),

  /** February 2nd, folklore about weather prediction. */
  GROUNDHOG_DAY("Groundhog's Day", (year) -> LocalDate.of(year.getValue(), 2, 2)),

  /** January 1st, celebrating the start of the new year. */
  NEW_YEARS_DAY("New Year's Day", (year) -> LocalDate.of(year.getValue(), 1, 1)),

  /** December 31st, the last day of the year. */
  NEW_YEARS_EVE("New Year's Eve", (year) -> LocalDate.of(year.getValue(), 1, 1).minusDays(1)),

  /** Third Sunday in June, honoring fathers. */
  FATHERS_DAY("Father's Day", (year) -> {
    LocalDate firstDayOfJune = LocalDate.of(year.getValue(), 6, 1);
    int daysToAdd = (DayOfWeek.SUNDAY.getValue() - firstDayOfJune.getDayOfWeek().getValue() + 7) % 7 + 14;
    return firstDayOfJune.plusDays(daysToAdd);
  }),

  /** Second Sunday in May, honoring mothers. */
  MOTHERS_DAY("Mother's Day", (year) -> {
    LocalDate firstDayOfMay = LocalDate.of(year.getValue(), 5, 1);
    int daysToAdd = (DayOfWeek.SUNDAY.getValue() - firstDayOfMay.getDayOfWeek().getValue() + 7) % 7 + 7;
    return firstDayOfMay.plusDays(daysToAdd);
  }),

  /** November 11th, honoring military veterans. */
  VETERANS_DAY("Veteran's Day", (year) -> LocalDate.of(year.getValue(), 11, 11)),

  /** Last Monday in May, honoring fallen military personnel. */
  MEMORIAL_DAY("Memorial Day", (year) -> {
    LocalDate lastDayOfMay = LocalDate.of(year.getValue(), 5, 31);
    int daysToSubtract = (lastDayOfMay.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue() + 7) % 7;
    return lastDayOfMay.minusDays(daysToSubtract);
  }),

  /** June 14th, commemorating the adoption of the US flag. */
  FLAG_DAY("Flag Day", (year) -> LocalDate.of(year.getValue(), 6, 14)),

  /** October 31st, known for costumes and trick-or-treating. */
  HALLOWEEN("Halloween", (year) -> LocalDate.of(year.getValue(), 10, 31)),

  /** July 4th, celebrating US independence. */
  INDEPENDENCE_DAY("Independence Day", (year) -> LocalDate.of(year.getValue(), 7, 4)),

  /** December 26th, celebrating African heritage. */
  KWANZAA("Kwanzaa", (year) -> LocalDate.of(year.getValue(), 12, 26)),

  /** First Monday in September, honoring workers. */
  LABOR_DAY("Labor Day", (year) -> {
    LocalDate firstDayOfSeptember = LocalDate.of(year.getValue(), 9, 1);
    int daysToAdd = (DayOfWeek.MONDAY.getValue() - firstDayOfSeptember.getDayOfWeek().getValue() + 7) % 7;
    return firstDayOfSeptember.plusDays(daysToAdd);
  }),

  /** Third Monday in January, honoring Martin Luther King Jr. */
  MARTIN_LUTHER_KING_JR_DAY("Martin Luther King Jr.'s Day", (year) -> {
    LocalDate firstDayOfJanuary = LocalDate.of(year.getValue(), 1, 1);
    int daysToAdd = (DayOfWeek.MONDAY.getValue() - firstDayOfJanuary.getDayOfWeek().getValue() + 7) % 7 + 14;
    return firstDayOfJanuary.plusDays(daysToAdd);
  }),

  /** September 11th, commemorating the 2001 terrorist attacks. */
  PATRIOT_DAY("Patriot Day", (year) -> LocalDate.of(year.getValue(), 9, 11)),

  /** Third Monday in February, honoring US presidents. */
  PRESIDENTS_DAY("President's Day", (year) -> {
    LocalDate firstDayOfFebruary = LocalDate.of(year.getValue(), 2, 1);
    int daysToAdd = (DayOfWeek.MONDAY.getValue() - firstDayOfFebruary.getDayOfWeek().getValue() + 7) % 7 + 14;
    return firstDayOfFebruary.plusDays(daysToAdd);
  }),

  /** March 17th, celebrating Irish culture and St. Patrick. */
  ST_PATRICKS_DAY("St. Patrick's Day", (year) -> LocalDate.of(year.getValue(), 3, 17)),

  /** April 15th, US federal income tax filing deadline. */
  TAX_DAY("Tax Day", (year) -> LocalDate.of(year.getValue(), 4, 15)),


  /** February 14th, celebrating love and affection. */
  VALENTINES_DAY("Valentine's Day", (year) -> LocalDate.of(year.getValue(), 2, 14))


  ;

  private final String summary;
  final Function<Year, LocalDate> dateFunction;

  private static final Map<String, WellknownHoliday> lookup;

  static {
    Map<String, WellknownHoliday> map = new HashMap<>();
    for (WellknownHoliday h : values()) {
      map.put(h.getSummary().toLowerCase(), h);
    }
    lookup = Collections.unmodifiableMap(map);
  }

  WellknownHoliday(String summary, Function<Year, LocalDate> dateFunction) {
    this.summary = summary;
    this.dateFunction = dateFunction;
  }

  public String getSummary() {
    return summary;
  }

  public static Optional<WellknownHoliday> fromSummary(String summary) {
    if (summary == null || summary.trim().isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(lookup.get(summary.toLowerCase()));
  }


  @Override
  public LocalDate apply(Year year) {
    return dateFunction.apply(year);
  }

}
