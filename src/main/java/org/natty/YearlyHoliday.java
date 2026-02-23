package org.natty;

import java.time.LocalDate;
import java.time.Year;
import java.util.function.Function;

/**
 * Represents a hollyday that basically occurs once a year. There are man of those.
 * <p>
 * Some of them are fixed, such as Christmas, but many are not, such as Easter. This interface is meant to represent both,
 * and is modeled like a function that takes a Year and returns the date of the holiday in that year.
 * @author Michiel Meeuwissen
 * @since 1.1
 */
public interface YearlyHoliday extends Function<Year, LocalDate> {

  String getSummary();


}
