package org.natty;

import java.time.Year;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple range implementation for Comparable types.
 * It always as a start. The end value can be null, which means it is unbounded.
 *
 * The end value may also be less than the start value, which means it is a backward range.
 *
 * @author Michiel Meeuwissen
 * @since 1.1
 * @param <C>
 */
public class Range<C extends Comparable<C>> implements Predicate<C> {

  private final C start;
  private final C end;
  private final boolean forward;

  public static Range<Year> ofYears(int startYear, int endYear) {
    return new Range<>(Year.of(startYear), Year.of(endYear));
  }

  public static Range<Year> ofYear(int year) {
    Year y = Year.of(year);
    return new Range<>(y, y);
  }

  public static Range<Year> fromYear(int year) {
    Year y = Year.of(year);
    return new Range<>(y, Year.of(9999));
  }

  public static Range<Year> fromYear(int year, boolean forward) {
    Year y = Year.of(year);
    return new Range<>(y, forward);
  }


  public static Stream<Year> stream(Range<Year> range) {
    return range.stream(Year::plusYears);
  }

  public Range(C start, C end) {
    if (start == null) {
      throw new IllegalArgumentException("Start cannot be null");
    }
    this.start = start;
    this.end = end;
    this.forward = end == null || start.compareTo(end) <= 0;
  }

  public Range(C start, boolean forward) {
    if (start == null) {
      throw new IllegalArgumentException("Start cannot be null");
    }
    this.start = start;
    this.end = null;
    this.forward = forward;
  }

  public C getStart() {
    return start;
  }

  public C getEnd() {
    return end;
  }

  public boolean forward() {
    return forward;
  }

  public Stream<C> stream(BiFunction<C, Integer, C> nextFunction) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<C>() {
      C current = null; ;
      final int step = forward ? 1 : -1;
      @Override
      public boolean hasNext() {
        return current == null || (forward ? current.compareTo(end) < 0 : current.compareTo(end) > 0);
      }

      @Override
      public C next() {
        if (!hasNext()) {
          throw new NoSuchElementException("No more elements in the range");
        }

        current = current == null ? start : nextFunction.apply(current, step);
        return current;
      }
    }, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL), false);

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
