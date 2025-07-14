package org.natty;

import java.util.HashMap;
import java.util.Map;

/**
 * The recognized holidays.
 * TODO: This is quite US-centric, but it is also tightly coupled to the recognized tokens in DateParser.
 * <p>
 * May be we can change the parser that it will recognize any summary when using quotes or so.
 * <p>
 * Like: "King's day" 2025
 */

public enum Holiday {
  /** April 1st, known for pranks and jokes. */
  APRIL_FOOLS_DAY("April Fool's Day"),
  /** The Friday following Thanksgiving, known for major retail sales. */
  BLACK_FRIDAY("Black Friday"),
  /** December 25th, celebrating the birth of Jesus Christ. */
  CHRISTMAS("Christmas Day"),
  /** December 24th, the evening before Christmas. */
  CHRISTMAS_EVE("Christmas Eve"),
  /** Second Monday in October, commemorating Christopher Columbus's landing. */
  COLUMBUS_DAY("Columbus Day (US-OPM)"),
  /** April 22nd, promoting environmental protection. */
  EARTH_DAY("Earth Day"),
  /** Christian holiday celebrating the resurrection of Jesus. */
  EASTER("Easter Sunday"),
  /** Third Sunday in June, honoring fathers. */
  FATHERS_DAY("Father's Day"),
  /** June 14th, commemorating the adoption of the US flag. */
  FLAG_DAY("Flag Day"),
  /** Friday before Easter Sunday, commemorating the crucifixion of Jesus. */
  GOOD_FRIDAY("Good Friday"),
  /** February 2nd, folklore about weather prediction. */
  GROUNDHOG_DAY("Groundhog's Day"),
  /** October 31st, known for costumes and trick-or-treating. */
  HALLOWEEN("Halloween"),
  /** July 4th, celebrating US independence. */
  INDEPENDENCE_DAY("Independence Day"),
  /** December 26th to January 1st, celebrating African heritage. */
  KWANZAA("Kwanzaa"),
  /** First Monday in September, honoring workers. */
  LABOR_DAY("Labor Day"),
  /** Third Monday in January, honoring Martin Luther King Jr. */
  MLK_DAY("Martin Luther King Jr.'s Day"),
  /** Last Monday in May, honoring fallen military personnel. */
  MEMORIAL_DAY("Memorial Day"),
  /** Second Sunday in May, honoring mothers. */
  MOTHERS_DAY("Mother's Day"),
  /** January 1st, celebrating the start of the new year. */
  NEW_YEARS_DAY("New Year's Day"),
  /** December 31st, the last day of the year. */
  NEW_YEARS_EVE("New Year's Eve"),
  /** September 11th, commemorating the 2001 terrorist attacks. */
  PATRIOT_DAY("Patriot Day"),
  /** Third Monday in February, honoring US presidents. */
  PRESIDENTS_DAY("President's Day"),
  /** March 17th, celebrating Irish culture and St. Patrick. */
  ST_PATRICKS_DAY("St. Patrick's Day"),
  /** April 15th, US federal income tax filing deadline. */
  TAX_DAY("Tax Day"),
  /** Fourth Thursday in November, giving thanks and feasting. */
  THANKSGIVING("Thanksgiving Day"),
  /** First Tuesday after the first Monday in November, US general elections. */
  ELECTION_DAY("US General Election"),
  /** February 14th, celebrating love and affection. */
  VALENTINES_DAY("Valentine's Day"),
  /** November 11th, honoring military veterans. */
  VETERANS_DAY("Veteran's Day"),

  INAUGURATION_DAY("Inauguration day")
  ;

  private final String summary;
  private static final Map<String, Holiday> lookup;
  static {
    lookup = new HashMap<String, Holiday>();
    for(Holiday h:values()) {
      lookup.put(h.getSummary().toLowerCase(), h);
    }
  }

  Holiday(String summary) {
    this.summary = summary;
  }

  public String getSummary() {
    return summary;
  }

  public static Holiday fromSummary(String summary) {
    if(summary == null)  {
      return null;
    }
    return lookup.get(summary.toLowerCase());
  }
}
