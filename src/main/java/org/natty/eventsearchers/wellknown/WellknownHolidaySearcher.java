package org.natty.eventsearchers.wellknown;

import org.natty.YearlyHoliday;
import org.natty.eventsearchers.AbstractYearlyHolidayEventSearcher;

/**
 * (Western) Christian holidays, such as Easter, Christmas, and Pentecost.
 * @since 1.1
 * @author Michiel Meeuwissen
 */
public class WellknownHolidaySearcher extends AbstractYearlyHolidayEventSearcher {

  public static final WellknownHolidaySearcher INSTANCE = new WellknownHolidaySearcher();

  private WellknownHolidaySearcher() {
    // Private constructor to enforce singleton pattern
  }

  @Override
  public YearlyHoliday fromSummary(String eventSummary) {
    return WellknownHoliday.fromSummary(eventSummary);
  }


}
