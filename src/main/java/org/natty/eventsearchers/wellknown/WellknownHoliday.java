package org.natty.eventsearchers.wellknown;

import java.time.LocalDate;
import java.time.Year;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
    int daysToAdd = (11 - firstDayOfNovember.getDayOfWeek().getValue() + 4) % 7 + 21; // 21 days to get to the fourth Thursday
    return firstDayOfNovember.plusDays(daysToAdd);
  }),
  BLACK_FRIDAY("Black Friday", (year) -> THANKSGIVING.dateFunction.apply(year).plusDays(1)),
  COLUMBUS_DAY("Columbus Day (US-OPM)", (year) -> {
    // Columbus Day is the second Monday in October
    LocalDate firstDayOfOctober = LocalDate.of(year.getValue(), 10, 1);
    int daysToAdd = (8 - firstDayOfOctober.getDayOfWeek().getValue() + 1) % 7 + 7; // 7 days to get to the second Monday
    return firstDayOfOctober.plusDays(daysToAdd);
  }),
  GROUNDHOG_DAY("Groundhog's Day", (year) -> LocalDate.of(year.getValue(), 2, 2)),
  NEW_YEARS_DAY("New Year's Day", (year) -> LocalDate.of(year.getValue(), 1, 1)),
  NEW_YEARS_EVE("New Year's Eve", (year) -> LocalDate.of(year.getValue(), 12, 31)),

  FATHERS_DAY("Father's Day", (year) -> {
    // Father's Day is the third Sunday in June
    LocalDate firstDayOfJune = LocalDate.of(year.getValue(), 6, 1);
    int daysToAdd = (7 - firstDayOfJune.getDayOfWeek().getValue() + 7) % 7 + 14; // 14 days to get to the third Sunday
    return firstDayOfJune.plusDays(daysToAdd);
  }),
  MOTHERS_DAY("Mother's Day",  (year) -> {
    // Mother's Day is the second Sunday in May
    LocalDate firstDayOfMay = LocalDate.of(year.getValue(), 5, 1);
    int daysToAdd = (7 - firstDayOfMay.getDayOfWeek().getValue() + 7) % 7 + 7; // 7 days to get to the second Sunday
    return firstDayOfMay.plusDays(daysToAdd);
  }),

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

  public static WellknownHoliday fromSummary(String summary) {
    if (summary == null || summary.trim().isEmpty()) {
      return null;
    }
    return lookup.get(summary.toLowerCase());
  }


  @Override
  public LocalDate apply(Year year) {
    return dateFunction.apply(year);
  }
}
