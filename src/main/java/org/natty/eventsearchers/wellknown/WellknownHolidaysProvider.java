package org.natty.eventsearchers.wellknown;

import org.natty.EventSearcherProvider;

/**
  * @since 1.1
  * @author Michiel Meeuwissen
 */
public class WellknownHolidaysProvider implements EventSearcherProvider {
  @Override
  public Class<? extends WellknownHolidaySearcher> type() {
    return WellknownHolidaySearcher.class;
  }

  @Override
  public WellknownHolidaySearcher get() {
    return WellknownHolidaySearcher.INSTANCE;
  }
}
