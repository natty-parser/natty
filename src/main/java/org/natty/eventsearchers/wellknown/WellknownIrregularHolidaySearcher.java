package org.natty.eventsearchers.wellknown;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.natty.EventSearcher;
import org.natty.Range;

/**

 * @since 1.1
 * @author Michiel Meeuwissen
 */
public class WellknownIrregularHolidaySearcher implements EventSearcher<LocalDate> {


  public WellknownIrregularHolidaySearcher() {

  }



  @Override
  public Stream<LocalDate> findEvents(Range<Year> range, String eventSummary) {
    WellknownIrregularHoliday holiday = WellknownIrregularHoliday.fromSummary(eventSummary)
        .orElse(null);
    if (holiday == null) {
      return Stream.empty();
    }
     return Range.stream(range).flatMap(y -> holiday.apply(y).stream());
  }

  // Special/irregular inaugurations (including Washington and Sunday exceptions)
  static final Map<Integer, List<LocalDate>> SPECIAL_INAUGURATIONS = Stream.of(
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

  ).collect(Collectors.groupingBy(
    LocalDate::getYear,
    Collectors.toList()
  ));

}
