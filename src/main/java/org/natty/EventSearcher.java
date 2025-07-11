package org.natty;

import java.time.Year;
import java.time.temporal.Temporal;
import java.util.stream.Stream;


/**
 * @author Michiel Meeuwissen
 * @since 1.1
 */
public interface EventSearcher<T extends Temporal> {

    /**
     * Finds dates within the specified range that match the given event summary.

     * @param eventSummary The summary of the event to search for.
     * @return A stream of LocalDate objects that match the criteria.
     */
  Stream<T> findEvents(Range<Year> range, String eventSummary);

}
