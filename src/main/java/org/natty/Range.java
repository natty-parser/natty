package org.natty;

import java.time.Year;
import java.util.function.Predicate;

/**
 * Simple range implementation for Comparable types.
 * @author Michiel Meeuwissen
 * @since 1.1
 * @param <C>
 */
public class Range<C extends Comparable<C>> implements Predicate<C> {

  private final C start;
  private final C end;

  public static Range<Year> ofYears(int startYear, int endYear) {
    return new Range<>(Year.of(startYear), Year.of(endYear));
  }

  public static Range<Year> ofYear(int year) {
    Year y = Year.of(year);
    return new Range<>(y, y);
  }

  public Range(C start, C end) {
    if (start == null || end == null) {
      throw new IllegalArgumentException("Start and end cannot be null");
    }
    if (start.compareTo(end) > 0) {
      throw new IllegalArgumentException("Start cannot be greater than end");
    }
    this.start = start;
    this.end = end;
  }

  public C getStart() {
    return start;
  }

  public C getEnd() {
    return end;
  }

  @Override
  public boolean test(C value) {
    if (value == null) {
      return false;
    }
    return start.compareTo(value) <= 0 && end.compareTo(value) >= 0;
  }
  public boolean encloses(Range<C> value) {
    if (value == null) {
      return false;
    }
    return test(value.start) && test(value.end);
  }

  public boolean isConnected(Range<C> value) {
    if (value == null) {
      return false;
    }
    return !(end.compareTo(value.start) < 0 || start.compareTo(value.end) > 0);
  }

  @Override
  public String toString() {
    return "Range{" +
      "start=" + start +
      ", end=" + end +
      '}';
    }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Range)) return false;

    Range<?> range = (Range<?>) o;
    return start.equals(range.start) && end.equals(range.end);
  }

  @Override
  public int hashCode() {
    int result = start.hashCode();
    result = 31 * result + end.hashCode();
    return result;
  }
}
