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
  EARTH_DAY("Earth Day", (year) -> LocalDate.of(year.getValue(), 4, 22)),
  APRIL_FOOLS_DAY("April Fool's Day", (year) -> LocalDate.of(year.getValue(), 4, 1)),
  THANKSGIVING("Thanksgiving Day", (year) -> {
    // Thanksgiving is the fourth Thursday in November
    LocalDate firstDayOfNovember = LocalDate.of(year.getValue(), 11, 1);
    int daysToAdd = (DayOfWeek.THURSDAY.getValue()  - firstDayOfNovember.getDayOfWeek().getValue() + 7) % 7 + 21; // three week after first
    return firstDayOfNovember.plusDays(daysToAdd);
  }),
  BLACK_FRIDAY("Black Friday", (year) -> THANKSGIVING.dateFunction.apply(year).plusDays(1)),
  COLUMBUS_DAY("Columbus Day (US-OPM)", (year) -> {
    // Columbus Day is the second Monday in October
    LocalDate firstDayOfOctober = LocalDate.of(year.getValue(), 10, 1);
    int daysToAdd = (DayOfWeek.MONDAY.getValue() - firstDayOfOctober.getDayOfWeek().getValue() + 7) % 7 + 7; // 7 days to get to the second Monday
    return firstDayOfOctober.plusDays(daysToAdd);
  }),
  GROUNDHOG_DAY("Groundhog's Day", (year) -> LocalDate.of(year.getValue(), 2, 2)),
  NEW_YEARS_DAY("New Year's Day", (year) -> LocalDate.of(year.getValue(), 1, 1)),
  NEW_YEARS_EVE("New Year's Eve", (year) -> LocalDate.of(year.getValue(), 1, 1).minusDays(1)),

  FATHERS_DAY("Father's Day", (year) -> {
    // Father's Day is the third Sunday in June
    LocalDate firstDayOfJune = LocalDate.of(year.getValue(), 6, 1);
    int daysToAdd = (DayOfWeek.SUNDAY.getValue() - firstDayOfJune.getDayOfWeek().getValue() + 7) % 7 + 14; // 14 days to get to the third Sunday
    return firstDayOfJune.plusDays(daysToAdd);
  }),
  MOTHERS_DAY("Mother's Day",  (year) -> {
    // Mother's Day is the second Sunday in May
    LocalDate firstDayOfMay = LocalDate.of(year.getValue(), 5, 1);
    int daysToAdd = (DayOfWeek.SUNDAY.getValue() - firstDayOfMay.getDayOfWeek().getValue() + 7) % 7 + 7; // 7 days to get to the second Sunday
    return firstDayOfMay.plusDays(daysToAdd);
  }),

  VETERANS_DAY("Veteran's Day", (year) -> {
    // Veteran's Day is November 11th
    return LocalDate.of(year.getValue(), 11, 11);
  }),
  MEMORIAL_DAY("Memorial Day", (year) -> {
    // Memorial Day is the last Monday in May
    LocalDate lastDayOfMay = LocalDate.of(year.getValue(), 5, 31);
    int daysToSubtract = (lastDayOfMay.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue() + 7) % 7; // days to subtract to get to the last Monday
    return lastDayOfMay.minusDays(daysToSubtract);
  }),
  FLAG_DAY("Flag Day", (year) -> LocalDate.of(year.getValue(), 6, 14)),
  HALLOWEEN("Halloween", (year) -> LocalDate.of(year.getValue(), 10, 31)),
  INDEPENDENCE_DAY("Independence Day", (year) -> LocalDate.of(year.getValue(), 7, 4)),
  KWANZAA("Kwanzaa", (year) -> LocalDate.of(year.getValue(), 12, 26)),
  LABOR_DAY("Labor Day", (year) -> {
    // Labor Day is the first Monday in September
    LocalDate firstDayOfSeptember = LocalDate.of(year.getValue(), 9, 1);
    int daysToAdd = (DayOfWeek.MONDAY.getValue() - firstDayOfSeptember.getDayOfWeek().getValue() + 7) % 7; // days to add to get to the first Monday
    return firstDayOfSeptember.plusDays(daysToAdd);
  }),
  MARTIN_LUTHER_KING_JR_DAY("Martin Luther King Jr.'s Day", (year) -> {
    // Martin Luther King Jr. Day is the third Monday in January
    LocalDate firstDayOfJanuary = LocalDate.of(year.getValue(), 1, 1);
    int daysToAdd = (DayOfWeek.MONDAY.getValue() - firstDayOfJanuary.getDayOfWeek().getValue() + 7) % 7 + 14; // 14 days to get to the third Monday
    return firstDayOfJanuary.plusDays(daysToAdd);
  }),
  PATRIOT_DAY("Patriot Day", (year) -> LocalDate.of(year.getValue(), 9, 11)),
  PRESIDENTS_DAY("President's Day", (year) -> {
    // President's Day is the third Monday in February
    LocalDate firstDayOfFebruary = LocalDate.of(year.getValue(), 2, 1);
    int daysToAdd = (DayOfWeek.MONDAY.getValue() - firstDayOfFebruary.getDayOfWeek().getValue() + 7) % 7 + 14; // 14 days to get to the third Monday
    return firstDayOfFebruary.plusDays(daysToAdd);
  }),
  ST_PATRICKS_DAY("St. Patrick's Day", (year) -> LocalDate.of(year.getValue(), 3, 17)),
  TAX_DAY("Tax Day", (year) -> LocalDate.of(year.getValue(), 4, 15)),
  ELECTION_DAY("US General Election", (year) -> {
    // US General Election is the first Tuesday after the first Monday in November
    LocalDate firstDayOfNovember = LocalDate.of(year.getValue(), 11, 1);
    int daysToAdd = (DayOfWeek.MONDAY.getValue() - firstDayOfNovember.getDayOfWeek().getValue() + 7) % 7 + 1; // days to add to get to the first Tuesday
    return firstDayOfNovember.plusDays(daysToAdd);
  }),

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
