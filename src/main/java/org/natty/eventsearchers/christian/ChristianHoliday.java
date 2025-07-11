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
   /** December 25th, celebrating the birth of Jesus Christ. */
    CHRISTMAS("Christmas Day", (year) -> LocalDate.of(year.getValue(), 12, 25)),

    /** December 24th, the evening before Christmas. */
    CHRISTMAS_EVE("Christmas Eve", (year) -> LocalDate.of(year.getValue(), 12, 24)),

    /** Christian holiday celebrating the resurrection of Jesus. */
    EASTER("Easter Sunday", ChristianHolidaySearcher::easter),

    /** January 6th, commemorating the visit of the Magi to the infant Jesus. */
    EPIPHANY("Epiphany", (year) ->  LocalDate.of(year.getValue(), 1, 6)),

    /** November 1st, honoring all saints. */
    ALL_SAINTS("All Saints' Day", (year) -> LocalDate.of(year.getValue(), 11, 1)),

    /** Sunday before Easter, commemorating Jesus' entry into Jerusalem. */
    PALM_SUNDAY("Palm Sunday", (year) -> EASTER.dateFunction.apply(year).minusDays(7)),

    /** 46 days before Easter, marking the start of Lent. */
    ASH_WEDNESDAY("Ash Wednesday", (year) -> EASTER.dateFunction.apply(year).minusDays(46)),

    /** Friday before Easter, commemorating the crucifixion of Jesus. */
    GOOD_FRIDAY("Good Friday", (year) -> EASTER.dateFunction.apply(year).minusDays(2)),

    /** 39 days after Easter, commemorating the ascension of Jesus into heaven. */
    ASCENSION("Ascension Day", (year) -> EASTER.dateFunction.apply(year).plusDays(39)),

    /** 49 days after Easter, celebrating the descent of the Holy Spirit. */
    PENTECOST("Pentecost", (year) -> EASTER.dateFunction.apply(year).plusDays(49)),

    /** 56 days after Easter, celebrating the Trinity. */
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
