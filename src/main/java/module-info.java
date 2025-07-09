import org.natty.EventSearcher;
import org.natty.eventsearchers.christian.ChristianHolidaySearcher;
import org.natty.eventsearchers.seasons.SeasonsEventSearcher;
import org.natty.eventsearchers.wellknown.WellknownHolidaySearcher;

module org.natty {
  uses EventSearcher;

  exports org.natty;

  requires antlr.runtime;
  requires static ical4j.core;
  requires static org.slf4j;

  provides EventSearcher with
    ChristianHolidaySearcher,
    WellknownHolidaySearcher,
    SeasonsEventSearcher;

}
