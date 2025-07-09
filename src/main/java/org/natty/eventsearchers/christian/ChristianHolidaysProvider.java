package org.natty.eventsearchers.christian;

import org.natty.EventSearcherProvider;

/**
  * @since 1.1
  * @author Michiel Meeuwissen
 */
public class ChristianHolidaysProvider implements EventSearcherProvider {
  @Override
  public Class<? extends ChristianHolidaySearcher> type() {
    return ChristianHolidaySearcher.class;
  }

  @Override
  public ChristianHolidaySearcher get() {
    return ChristianHolidaySearcher.INSTANCE;
  }
}
