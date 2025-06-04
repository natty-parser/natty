package org.natty.grammar;


import org.junit.jupiter.api.Test;

class TimeGrammarTest extends AbstractGrammarTest {

    @Test
    void time_zone_abbreviation() throws Exception {
        ruleName = "time_zone_abbreviation";

        assertAST("est", "America/New_York");
        assertAST("edt", "America/New_York");
        assertAST("et", "America/New_York");
        assertAST("pst", "America/Los_Angeles");
        assertAST("pdt", "America/Los_Angeles");
        assertAST("pt", "America/Los_Angeles");
        assertAST("cst", "America/Chicago");
        assertAST("cdt", "America/Chicago");
        assertAST("ct", "America/Chicago");
        assertAST("mst", "America/Denver");
        assertAST("mdt", "America/Denver");
        assertAST("mt", "America/Denver");
        assertAST("akst", "America/Anchorage");
        assertAST("akdt", "America/Anchorage");
        assertAST("akt", "America/Anchorage");
        assertAST("hast", "Pacific/Honolulu");
        assertAST("hadt", "Pacific/Honolulu");
        assertAST("hat", "Pacific/Honolulu");
        assertAST("hst", "Pacific/Honolulu");
    }

    @Test
    void meridian_indicator() throws Exception {
        ruleName = "meridian_indicator";

        assertAST("am", "am");
        assertAST("a.m.", "am");
        assertAST("a", "am");
        assertAST("pm", "pm");
        assertAST("p.m.", "pm");
        assertAST("p", "pm");
    }

    @Test
    void minutes() throws Exception {
        ruleName = "minutes";
        for (int i = 1; i <= 59; i++) {
            String input = String.format("%02d", i);
            assertAST(input, String.format("(MINUTES_OF_HOUR %02d)", i));
        }

        // TODO failures are not actually failing the parsing
        assertAST("0", "(MINUTES_OF_HOUR <unexpected: [@0,0:0='0',<69>,1:0], resync=0>)");
        //assertAST("1", "FAIL");
        //assertAST("2", "FAIL");
        //assertAST("3", "FAIL ");
        //assertAST("4", "FAIL");
        //assertAST("5", "FAIL");
        //assertAST("6", "FAIL");
        //assertAST("7", "FAIL");
        //assertAST("8", "FAIL");
        //assertAST("9", "FAIL");
        //"60" FAIL
    }

    @Test
    void hours() throws Exception {
        ruleName = "hours";

        // without prefix
        for (int i = 1; i <= 23; i++) {
            String input = String.format("%02d", i);
            assertAST(input, String.format("(HOURS_OF_DAY %02d)", i));
        }

        // with prefix
        for (int i = 1; i <= 23; i++) {
            String input = String.format("%d", i);
            assertAST(input, String.format("(HOURS_OF_DAY %d)", i));
        }

        //"-1" FAIL
        //"24" FAIL
    }

    @Test
    void explicit_time() throws Exception {
        ruleName = "explicit_time";

        assertAST("0600h", "(EXPLICIT_TIME (HOURS_OF_DAY 06) (MINUTES_OF_HOUR 00))");
        assertAST("06:00h", "(EXPLICIT_TIME (HOURS_OF_DAY 06) (MINUTES_OF_HOUR 00))");
        assertAST("06:00 hours", "(EXPLICIT_TIME (HOURS_OF_DAY 06) (MINUTES_OF_HOUR 00))");
        assertAST("0000", "(EXPLICIT_TIME (HOURS_OF_DAY 00) (MINUTES_OF_HOUR 00))");
        assertAST("0700h", "(EXPLICIT_TIME (HOURS_OF_DAY 07) (MINUTES_OF_HOUR 00))");
        assertAST("6pm", "(EXPLICIT_TIME (HOURS_OF_DAY 6) (MINUTES_OF_HOUR 0) pm)");
        assertAST("5:30 a.m.", "(EXPLICIT_TIME (HOURS_OF_DAY 5) (MINUTES_OF_HOUR 30) am)");
        assertAST("5", "(EXPLICIT_TIME (HOURS_OF_DAY 5) (MINUTES_OF_HOUR 0))");
        assertAST("12:59", "(EXPLICIT_TIME (HOURS_OF_DAY 12) (MINUTES_OF_HOUR 59))");
        assertAST("23:59", "(EXPLICIT_TIME (HOURS_OF_DAY 23) (MINUTES_OF_HOUR 59))");
        assertAST("00:00", "(EXPLICIT_TIME (HOURS_OF_DAY 00) (MINUTES_OF_HOUR 00))");
        assertAST("10:00am", "(EXPLICIT_TIME (HOURS_OF_DAY 10) (MINUTES_OF_HOUR 00) am)");
        assertAST("10a", "(EXPLICIT_TIME (HOURS_OF_DAY 10) (MINUTES_OF_HOUR 0) am)");
        assertAST("10am", "(EXPLICIT_TIME (HOURS_OF_DAY 10) (MINUTES_OF_HOUR 0) am)");
        assertAST("10", "(EXPLICIT_TIME (HOURS_OF_DAY 10) (MINUTES_OF_HOUR 0))");
        assertAST("8p", "(EXPLICIT_TIME (HOURS_OF_DAY 8) (MINUTES_OF_HOUR 0) pm)");
        assertAST("8pm", "(EXPLICIT_TIME (HOURS_OF_DAY 8) (MINUTES_OF_HOUR 0) pm)");
        assertAST("8 pm", "(EXPLICIT_TIME (HOURS_OF_DAY 8) (MINUTES_OF_HOUR 0) pm)");
        assertAST("noon", "(EXPLICIT_TIME (HOURS_OF_DAY 12) (MINUTES_OF_HOUR 0) (SECONDS_OF_MINUTE 0) pm)");
        assertAST("afternoon", "(EXPLICIT_TIME (HOURS_OF_DAY 12) (MINUTES_OF_HOUR 0) (SECONDS_OF_MINUTE 0) pm)");
        assertAST("midnight", "(EXPLICIT_TIME (HOURS_OF_DAY 12) (MINUTES_OF_HOUR 0) (SECONDS_OF_MINUTE 0) am)");
        assertAST("mid-night", "(EXPLICIT_TIME (HOURS_OF_DAY 12) (MINUTES_OF_HOUR 0) (SECONDS_OF_MINUTE 0) am)");
    }
}
