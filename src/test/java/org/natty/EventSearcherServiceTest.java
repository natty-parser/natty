package org.natty;

import org.junit.Test;

import java.util.logging.Logger;
import java.time.temporal.Temporal;
import java.util.Optional;

public class EventSearcherServiceTest {
  private static final Logger log  = Logger.getLogger(EventSearcherServiceTest.class.getName());


  @Test
  public void testService() {


    Optional<Temporal> fathers = EventSearcherService.INSTANCE.findEvents(Range.ofYear(2025), Holiday.FATHERS_DAY.getSummary()).findFirst();
    log.info("Fathers day: " + fathers.orElse(null));

    Optional<Temporal> spring = EventSearcherService.INSTANCE.findEvents(Range.ofYear(2025), Season.SPRING.getSummary()).findFirst();
    log.info("Spring day: " + spring.orElse(null));
  }

}
