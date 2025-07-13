package org.natty.eventsearchers.wellknown;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
  * @since 1.1
  * @author Michiel Meeuwissen
 */
public enum WellknownIrregularHoliday implements Function<Year, List<LocalDate>>{

  /** First Tuesday after the first Monday in November, US general elections. */
  ELECTION_DAY("US General Election", (year) -> {
    LocalDate firstDayOfNovember = LocalDate.of(year.getValue(), 11, 1);
    int daysToAdd = (DayOfWeek.MONDAY.getValue() - firstDayOfNovember.getDayOfWeek().getValue() + 7) % 7 + 1;
    return Collections.singletonList(firstDayOfNovember.plusDays(daysToAdd));
  }),

  INAUGURATION_DAY("Inauguration Day", (year) ->  {
    int y = year.getValue();
    if (y < 1789) return Collections.emptyList();

    // Special/irregular inaugurations (including Washington and Sunday exceptions)
    List<LocalDate> specialInaugurations = Arrays.asList(
      LocalDate.of(1789, 4, 30), // Washington
      LocalDate.of(1793, 3, 4),
      LocalDate.of(1797, 3, 4),
      LocalDate.of(1801, 3, 4),
      LocalDate.of(1805, 3, 4),
      LocalDate.of(1809, 3, 4),
      LocalDate.of(1813, 3, 4),
      LocalDate.of(1817, 3, 4),
      LocalDate.of(1821, 3, 5),
      LocalDate.of(1923, 8, 3), // Harding died, Coolidge inaugurated// March 4 was Sunday
      LocalDate.of(1825, 3, 4),
      LocalDate.of(1829, 3, 4),
      LocalDate.of(1833, 3, 4),
      LocalDate.of(1837, 3, 4),
      LocalDate.of(1841, 3, 4),
      LocalDate.of(1845, 3, 4),
      LocalDate.of(1849, 3, 5),  // March 4 was Sunday
      LocalDate.of(1850, 7, 9), // Taylor died, Fillmore inaugurated
      LocalDate.of(1853, 3, 4),
      LocalDate.of(1857, 3, 4),
      LocalDate.of(1861, 3, 4),
      LocalDate.of(1865, 3, 4),
      LocalDate.of(1865, 4, 15), // Lincoln assassinated, Johnson inaugurated
      LocalDate.of(1869, 3, 4),
      LocalDate.of(1873, 3, 4),
      LocalDate.of(1877, 3, 5),  // March 4 was Sunday
      LocalDate.of(1881, 3, 4),
      LocalDate.of(1885, 3, 4),
      LocalDate.of(1889, 3, 4),
      LocalDate.of(1893, 3, 4),
      LocalDate.of(1897, 3, 4),
      LocalDate.of(1901, 3, 4),
      LocalDate.of(1905, 3, 4),
      LocalDate.of(1909, 3, 4),
      LocalDate.of(1913, 3, 4),
      LocalDate.of(1917, 3, 5),  // March 4 was Sunday
      LocalDate.of(1921, 3, 4),
      LocalDate.of(1925, 3, 4),
      LocalDate.of(1929, 3, 4),
      LocalDate.of(1933, 3, 4),
      LocalDate.of(1963, 11, 22), // Kennedy assassinated, Johnson inaugurated
      LocalDate.of(1974, 9, 8) // Nixon resigned, Ford inaugurated

    );
    List<LocalDate> result = new ArrayList<>();
    for (LocalDate date : specialInaugurations) {
      if (date.getYear() == y) result.add(date);
    }

    // Regular inaugurations from 1937 onward (every 4 years)
    if (y >= 1937 && (y - 1937) % 4 == 0) {
      LocalDate jan20 = LocalDate.of(y, 1, 20);
      if (jan20.getDayOfWeek() == DayOfWeek.SUNDAY) {
        result.add(jan20.plusDays(1));
      } else {
        result.add(jan20);
      }
    }
    return result;
  })
  ;

  private final String summary;
  final Function<Year, List<LocalDate>> dateFunction;

  private static final Map<String, WellknownIrregularHoliday> lookup;

  static {
    Map<String, WellknownIrregularHoliday> map = new HashMap<>();
    for (WellknownIrregularHoliday h : values()) {
      map.put(h.getSummary().toLowerCase(), h);
    }
    lookup = Collections.unmodifiableMap(map);
  }

  WellknownIrregularHoliday(String summary, Function<Year, List<LocalDate>> dateFunction) {
    this.summary = summary;
    this.dateFunction = dateFunction;
  }

  public String getSummary() {
    return summary;
  }

  public static Optional<WellknownIrregularHoliday> fromSummary(String summary) {
    if (summary == null || summary.trim().isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(lookup.get(summary.toLowerCase()));
  }


  @Override
  public List<LocalDate> apply(Year year) {
    return dateFunction.apply(year);
  }


}
