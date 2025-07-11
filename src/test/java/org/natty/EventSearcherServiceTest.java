package org.natty;

import org.junit.Test;

import java.time.temporal.Temporal;
import java.util.Optional;

public class EventSearcherServiceTest {

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EventSearcherServiceTest.class);

  @Test
  public void testService() {


    Optional<Temporal> fathers = EventSearcherService.INSTANCE.findEvents(Range.ofYear(2025), Holiday.FATHERS_DAY.getSummary()).findFirst();
    log.info("Fathers day: {}", fathers.orElse(null));

    Optional<Temporal> spring = EventSearcherService.INSTANCE.findEvents(Range.ofYear(2025), Season.SPRING.getSummary()).findFirst();
    log.info("Spring day: {}", spring.orElse(null));
  }

}
