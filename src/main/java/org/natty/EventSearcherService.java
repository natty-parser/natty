package org.natty;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")
public class EventSearcherService implements EventSearcher<Temporal> {

  public static final EventSearcherService INSTANCE = new EventSearcherService();

  private final Map<Class<? extends EventSearcher>, EventSearcher> eventSearchers = new HashMap<>();


  public ServiceLoader<? extends EventSearcher> loader;

  private EventSearcherService() {
    loader = ServiceLoader.load(EventSearcher.class);
  }


  @SuppressWarnings("unchecked")
  @Override
  public Stream<Temporal> findEvents(Range<Year> range, String eventSummary) {
    return loader.stream().map(ServiceLoader.Provider::get)
      .flatMap(searcher -> searcher.findEvents(range, eventSummary))
      .filter(Objects::nonNull);
  }


  public Stream<Instant> findEvents(Range<Year> range, TimeZone timeZone, String eventSummary) {
    return findEvents(range, eventSummary)
      .map(t -> {
        return toInstant(t, timeZone);
      }).filter(Optional::isPresent)
      .map(Optional::get);
  }

  protected Optional<Instant> toInstant(Temporal date, TimeZone timeZone) {
  // this date is at the date of the holiday at 12 AM UTC
    if (date instanceof LocalDate) {
      return Optional.of(((LocalDate) date).atStartOfDay().atZone(timeZone.toZoneId()).toInstant());
    } else if (date instanceof OffsetDateTime) {
      return Optional.of(((OffsetDateTime) date).toInstant());
    } else if (date instanceof Instant) {
      return Optional.of((Instant) date);
    } else if (date instanceof Date) {
      return Optional.of(((Date) date).toInstant());
    } else {
      return Optional.empty();
    }
  }


    @SuppressWarnings("unchecked")
  public <T extends Temporal, E extends EventSearcher<T>> Stream<E> getEventSearchers(Class<E> eventSearcherClass) {
    return  (Stream<E>) loader.stream()
      .filter(provider -> eventSearcherClass.isAssignableFrom(provider.type()))
      .map(ServiceLoader.Provider::get);
  }

  @SuppressWarnings("unchecked")
  public <T extends Temporal, E extends EventSearcher<T>> E getEventSearcher(Class<E> eventSearcherClass) {
    return (E) eventSearchers.computeIfAbsent(eventSearcherClass, cls -> {
      List<E> searchers = getEventSearchers(eventSearcherClass).collect(Collectors.toList());
      if (searchers.size() != 1) {
        throw new IllegalStateException("Expected exactly one event searcher of type " + cls.getName() + ", but found: " + searchers);
      }
      return searchers.get(0);
    });
  }

  public <T extends Temporal,  E extends EventSearcher<T>> Stream<T> findEvents(Range<Year> range, TimeZone timeZone, String eventSummary, Class<E> eventSearcherClass) {
    return getEventSearchers(eventSearcherClass)
      .flatMap(searcher -> searcher.findEvents(range, eventSummary))
      .filter(Objects::nonNull);
  }
}
