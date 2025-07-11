package org.natty;

import java.time.Instant;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public enum Season implements Function<Year, Instant> {
  WINTER("Winter Solstice"),
  SPRING("Vernal Equinox"),
  SUMMER("Summer Solstice"),
  FALL("Autumnal Equinox");

  private final String summary;
  private static final Map<String, Season> lookup;

  static {
    lookup = new HashMap<>();
    for (Season h : values()) {
      lookup.put(h.getSummary().toLowerCase(), h);
    }
  }



  Season(String summary) {
    this.summary = summary;
  }

  public String getSummary() {
    return summary;
  }

  public static Optional<Season> fromSummary(String summary) {
    if (summary == null || summary.trim().isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(lookup.get(summary.toLowerCase()));
  }

  @Override
  public Instant apply(Year year) {
    return equinoxSolsticeMeeusAlgorithm(year.getValue(), this);
  }

  // Meeus Table 27.B periodic terms for each event
  private static final Map<Season, double[][]> PERIODIC_TERMS;
  static {
    PERIODIC_TERMS = new HashMap<>();

    PERIODIC_TERMS.put(SPRING, new double[][]{
      {485, 324.96, 1934.136},
      {203, 337.23, 32964.467},
      {199, 342.08, 20.186},
      {182, 27.85, 445267.112},
      {156, 73.14, 45036.886},
      {136, 171.52, 22518.443},
      {77, 222.54, 65928.934},
      {74, 296.72, 3034.906},
      {70, 243.58, 9037.513},
      {58, 119.81, 33718.147},
      {52, 297.17, 150.678},
      {50, 21.02, 2281.226},
      {45, 247.54, 29929.562},
      {44, 325.15, 31555.956},
      {29, 60.93, 4443.417},
      {18, 155.12, 67555.328},
      {17, 288.79, 4562.452},
      {16, 198.04, 62894.029},
      {14, 199.76, 31436.921},
      {12, 95.39, 14577.848},
      {12, 287.11, 31931.756},
      {12, 320.81, 34777.259},
      {9, 227.73, 1222.114},
      {8, 15.45, 16859.074}
    });
    PERIODIC_TERMS.put(SUMMER, new double[][]{
      {171, 44.96, 1934.136},
      {50, 10.23, 32964.467},
      {32, 57.08, 20.186},
      {25, 95.85, 445267.112},
      {23, 287.14, 45036.886},
      {19, 171.52, 22518.443},
      {13, 222.54, 65928.934},
      {12, 296.72, 3034.906},
      {10, 243.58, 9037.513},
      {8, 119.81, 33718.147},
      {7, 297.17, 150.678},
      {7, 21.02, 2281.226},
      {6, 247.54, 29929.562},
      {6, 325.15, 31555.956},
      {4, 60.93, 4443.417},
      {3, 155.12, 67555.328},
      {3, 288.79, 4562.452},
      {3, 198.04, 62894.029},
      {2, 199.76, 31436.921},
      {2, 95.39, 14577.848},
      {2, 287.11, 31931.756},
      {2, 320.81, 34777.259},
      {1, 227.73, 1222.114},
      {1, 15.45, 16859.074}
    });
    PERIODIC_TERMS.put(FALL, new double[][]{
      {62, 90.96, 1934.136},
      {29, 45.23, 32964.467},
      {21, 57.08, 20.186},
      {17, 95.85, 445267.112},
      {16, 287.14, 45036.886},
      {13, 171.52, 22518.443},
      {9, 222.54, 65928.934},
      {8, 296.72, 3034.906},
      {7, 243.58, 9037.513},
      {6, 119.81, 33718.147},
      {6, 297.17, 150.678},
      {5, 21.02, 2281.226},
      {5, 247.54, 29929.562},
      {5, 325.15, 31555.956},
      {3, 60.93, 4443.417},
      {3, 155.12, 67555.328},
      {3, 288.79, 4562.452},
      {2, 198.04, 62894.029},
      {2, 199.76, 31436.921},
      {2, 95.39, 14577.848},
      {2, 287.11, 31931.756},
      {2, 320.81, 34777.259},
      {1, 227.73, 1222.114},
      {1, 15.45, 16859.074}
    });
    PERIODIC_TERMS.put(WINTER, new double[][]{
      {65, 45.96, 1934.136},
      {31, 45.23, 32964.467},
      {23, 57.08, 20.186},
      {19, 95.85, 445267.112},
      {17, 287.14, 45036.886},
      {14, 171.52, 22518.443},
      {10, 222.54, 65928.934},
      {9, 296.72, 3034.906},
      {8, 243.58, 9037.513},
      {7, 119.81, 33718.147},
      {7, 297.17, 150.678},
      {6, 21.02, 2281.226},
      {6, 247.54, 29929.562},
      {6, 325.15, 31555.956},
      {4, 60.93, 4443.417},
      {4, 155.12, 67555.328},
      {4, 288.79, 4562.452},
      {3, 198.04, 62894.029},
      {3, 199.76, 31436.921},
      {2, 95.39, 14577.848},
      {2, 287.11, 31931.756},
      {2, 320.81, 34777.259},
      {1, 227.73, 1222.114},
      {1, 15.45, 16859.074}
    });
  };


  private static final Map<Season, double[]> COEFFICIENTS;

  static {
    ;

    COEFFICIENTS = new HashMap<>();
    COEFFICIENTS.put(SPRING, new double[]{2451623.80984, 365242.37404, 0.05169, -0.00411, -0.00057});
    COEFFICIENTS.put(SUMMER, new double[]{2451716.56767, 365241.62603, 0.00325, 0.00888, -0.00030});
    COEFFICIENTS.put(FALL, new double[]{2451810.21715, 365242.01767, -0.11575, 0.00337, 0.00078});
    COEFFICIENTS.put(WINTER, new double[]{2451900.05952, 365242.74049, -0.06223, -0.00823, 0.00032});
  }
  public static Instant equinoxSolsticeMeeusAlgorithm(int year, Season event) {
    Map<Season, double[]> coefficients =  COEFFICIENTS;

    if (year < 1000 || year > 3000) {
      // Meeus algorithm does not support years before 1000 or after 3000
      throw new IllegalArgumentException("Year must be between 1000 and 3000");
    }


    double y = (year - 2000) / 1000.0;
    double[] c = coefficients.get(event);
    double jd = c[0] + c[1] * y + c[2] * y * y + c[3] * y * y * y + c[4] * y * y * y * y;

    // Add periodic terms correction (Meeus Table 27.B)
    double t = (jd - 2451545.0) / 36525.0;
    double s = 0.0;
    for (double[] term : PERIODIC_TERMS.get(event)) {
      s += term[0] * Math.cos(Math.toRadians(term[1] + term[2] * t));
    }
    double correction = s * 0.00001; // days
    jd += correction;

    // ΔT in seconds (Meeus, Chap. 10, for 2000–2100)
    double deltaT = deltaTSeconds(year);

    // Convert ΔT to days and subtract from JD to get UT
    jd -= deltaT / 86400.0;
    return julianDayToInstant(jd);
  }

  private static Instant julianDayToInstant(double jd) {
    double epochDay = jd - 2440587.5;
    long epochSecond = (long) (epochDay * 86400);
    double fractional = (epochDay * 86400) - epochSecond;
    return Instant.ofEpochSecond(epochSecond, (long) (fractional * 1_000_000_000))
      .plusMillis(500).truncatedTo(ChronoUnit.SECONDS); // round to nearest second
  }
  // Simple ΔT approximation for 2000–2100 (Meeus, Eq. 10.6)
  private static double deltaTSeconds(int year) {
    double t = (year - 2000) / 100.0;
    return 102.0 + 102.0 * t + 25.3 * t * t;
  }

}
