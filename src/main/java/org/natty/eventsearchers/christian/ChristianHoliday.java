package org.natty.eventsearchers.christian;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.function.Function;
import org.natty.YearlyHoliday;

/**
  * @since 1.1
  * @author Michiel Meeuwissen
 */
public enum ChristianHoliday implements YearlyHoliday {
  CHRISTMAS("Christmas", (year) -> LocalDate.of(year.getValue(), 12, 25)),
  CHRISTMAS_EVE("Christmas Eve", (year) -> LocalDate.of(year.getValue(), 12, 24)),
  EASTER("Easter", ChristianHolidaySearcher::easter),
  EPIPHANY("Epiphany", (year) ->  LocalDate.of(year.getValue(), 1, 6)),
  ALL_SAINTS("All Saints' Day", (year) -> LocalDate.of(year.getValue(), 11, 1)),
  PALM_SUNDAY("Palm Sunday", (year) -> EASTER.dateFunction.apply(year).minusDays(7)),
  ASH_WEDNESDAY("Ash Wednesday", (year) -> EASTER.dateFunction.apply(year).minusDays(46)),
  GOOD_FRIDAY("Good Friday", (year) -> EASTER.dateFunction.apply(year).minusDays(2)),
  ASCENSION("Ascension Day", (year) -> EASTER.dateFunction.apply(year).plusDays(39)),
  PENTECOST("Pentecost", (year) -> EASTER.dateFunction.apply(year).plusDays(49)),
  TRINITY_SUNDAY("Trinity Sunday", year -> EASTER.dateFunction.apply(year).plusDays(56))
  ;

  private final String summary;
  final Function<Year, LocalDate> dateFunction;

  private static final Map<String, ChristianHoliday> lookup;

  static {
    Map<String, ChristianHoliday> map = new HashMap<>();
    for (ChristianHoliday h : values()) {
      map.put(h.getSummary().toLowerCase(), h);
    }
    lookup = Collections.unmodifiableMap(map);
  }

  ChristianHoliday(String summary, Function<Year, LocalDate> dateFunction) {
    this.summary = summary;
    this.dateFunction = dateFunction;
  }

  public String getSummary() {
    return summary;
  }

  public static Optional<ChristianHoliday> fromSummary(String summary) {
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
