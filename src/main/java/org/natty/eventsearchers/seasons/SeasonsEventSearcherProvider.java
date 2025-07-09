package org.natty.eventsearchers.seasons;

import org.natty.EventSearcherProvider;

/**
  * @since 1.1
  * @author Michiel Meeuwissen
 */
public class SeasonsEventSearcherProvider implements EventSearcherProvider {
  @Override
  public Class<? extends SeasonsEventSearcher> type() {
    return SeasonsEventSearcher.class;
  }

  @Override
  public SeasonsEventSearcher get() {
    return SeasonsEventSearcher.INSTANCE;
  }
}
