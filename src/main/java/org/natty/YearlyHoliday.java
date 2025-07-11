package org.natty;

import java.time.LocalDate;
import java.time.Year;
import java.util.function.Function;
/*
 * @author Michiel Meeuwissen
 * @since 1.1
 */
public interface YearlyHoliday extends Function<Year, LocalDate> {

  String getSummary();


}
