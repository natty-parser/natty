package org.natty;

import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public enum Season implements Function<Year, Optional<Instant>>{
  WINTER("Winter Solstice", 3, Month.DECEMBER),
  SPRING("Vernal Equinox", 0, Month.MARCH),
  SUMMER("Summer Solstice", 1, Month.JUNE),
  FALL("Autumnal Equinox", 2, Month.SEPTEMBER);

  private final String summary;
  private static final Map<String, Season> lookup;
  static {
    lookup = new HashMap<>();
    for(Season h:values()) {
      lookup.put(h.getSummary().toLowerCase(), h);
    }
  }
  private Map<Year, Instant> dates;
  private final int column;
  private final Month month;



  Season(String summary, int column, Month month) {
    this.summary = summary;
    this.column = column;
    this.month = month;
  }

  public String getSummary() {
    return summary;
  }

  public static Season fromSummary(String summary) {
    if (summary == null || summary.trim().isEmpty()) {
      return null;
    }
    return lookup.get(summary.toLowerCase());
  }

  @Override
  public Optional<Instant> apply(Year year) {
    setDatesIfNecessary();
    return Optional.ofNullable(dates.get(year));
  }

  private void setDatesIfNecessary() {
    if (dates == null) {
      dates = new HashMap<>();
      // Calculate the date for each season based on the year
      try (InputStream in = getClass().getResourceAsStream("/seasons.txt");
           java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(in))
      ) {
        while(true) {
          String line = reader.readLine();
          if (line == null) {
            break;
          }

          String[] parts = line.trim().split("\\s+");
          if (parts.length == 13) {
            int y = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[column * 3 + 2]);
            LocalTime time = LocalTime.parse(parts[column * 3 + 3]);
            dates.put(
              Year.of(y),
              LocalDate.of(y, month, day).atTime(time).toInstant(
                ZoneOffset.UTC));
          }
        }
      } catch (Exception e) {
        throw new RuntimeException("Error reading seasons data", e);
      }
    }

  }
}
