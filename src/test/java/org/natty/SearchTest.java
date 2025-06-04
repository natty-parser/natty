package org.natty;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Joe Stelmach
 */
class SearchTest extends AbstractTest {
    @BeforeAll
    public static void oneTime() {
        Locale.setDefault(Locale.US);
        TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));
        initCalendarAndParser();
    }

    @Test
    void test() throws Exception {
        Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("2/20/2011");
        calendarSource = new CalendarSource(reference);

        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse("golf tomorrow at 9 AM at pebble beach", reference);
        assertThat(groups).hasSize(1);
        DateGroup group = groups.get(0);
        assertThat(group.getLine()).isEqualTo(1);
        assertThat(group.getPosition()).isEqualTo(6);
        assertThat(group.getDates()).hasSize(1);
        validateDate(group.getDates().get(0), 2, 21, 2011);
        validateTime(group.getDates().get(0), 9, 0, 0);

        groups = parser.parse("golf with friends tomorrow at 10 ", reference);
        assertThat(groups).hasSize(1);
        group = groups.get(0);
        assertThat(group.getLine()).isEqualTo(1);
        assertThat(group.getPosition()).isEqualTo(19);
        assertThat(group.getDates().size()).isEqualTo(1);
        validateDate(group.getDates().get(0), 2, 21, 2011);
        validateTime(group.getDates().get(0), 10, 0, 0);

        parser = new Parser();
        groups = parser.parse("golf with freinds tomorrow at 9 or Thursday at 10 am", reference);
        assertThat(groups).hasSize(1);
        List<Date> dates = groups.get(0).getDates();
        assertThat(dates).hasSize(2);
        validateDate(dates.get(0), 2, 21, 2011);
        validateTime(dates.get(0), 9, 0, 0);
        validateDate(dates.get(1), 2, 24, 2011);
        validateTime(dates.get(1), 10, 0, 0);

        groups = parser.parse("golf with friends tomorrow at 9 or Thursday at 10", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(2);
        validateDate(dates.get(0), 2, 21, 2011);
        validateTime(dates.get(0), 9, 0, 0);
        validateDate(dates.get(1), 2, 24, 2011);
        validateTime(dates.get(1), 10, 0, 0);

        groups = parser.parse("I want to go to park tomorrow and then email john@aol.com", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 2, 21, 2011);

        groups = parser.parse("I want to pay off all my debt in the next two years.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(2);
        validateDate(dates.get(0), 2, 20, 2011);
        validateDate(dates.get(1), 2, 20, 2013);

        groups = parser.parse("I want to purchase a car in the next month.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 3, 20, 2011);

        groups = parser.parse("I want to plan a get-together with my friends for this Friday.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 2, 25, 2011);

        groups = parser.parse("I want to lose five pounds in the next two months.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(2);
        validateDate(dates.get(0), 2, 20, 2011);
        validateDate(dates.get(1), 4, 20, 2011);

        groups = parser.parse("I want to finalize my college schedule by next week.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 2, 27, 2011);

        groups = parser.parse("I want to read this weekend.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 2, 26, 2011);

        groups = parser.parse("I want to travel a big chunk of world next year.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 2, 20, 2012);

        groups = parser.parse("last 2 weeks", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(2);
        validateDate(dates.get(0), 2, 20, 2011);
        validateDate(dates.get(1), 2, 6, 2011);

        groups = parser.parse("last 5 years", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(2);
        validateDate(dates.get(0), 2, 20, 2011);
        validateDate(dates.get(1), 2, 20, 2006);

        groups = parser.parse("next 5 years", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(2);
        validateDate(dates.get(0), 2, 20, 2011);
        validateDate(dates.get(1), 2, 20, 2016);

        groups = parser.parse("I want to go to my doctors appointment on May 15, 2011.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 5, 15, 2011);

        groups = parser.parse("I intend to become a zombie on December, 21st 2012.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 12, 21, 2012);

        groups = parser.parse("I want to hire a virtual assistant to do research for me on March 15, 2011", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 3, 15, 2011);

        groups = parser.parse("I want to see my mother on sunday.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 2, 27, 2011);

        groups = parser.parse("I want to be able to jog 3 miles non-stop by September.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 9, 1, 2011);

        groups = parser.parse("I want to lose 10 lbs in 10 days", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 3, 2, 2011);

        groups = parser.parse("I want to visit my grandfathers grave on December 30 2011", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 12, 30, 2011);

        groups = parser.parse("i want to have 1 kid this year", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 2, 20, 2011);

        groups = parser.parse("save $1000 by September", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 9, 1, 2011);

        groups = parser.parse("have my son play at muse music in provo UT at the 3 band cause they always have fog on the third band at 7:30", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDateTime(dates.get(0), 2, 20, 2011, 7, 30, 0);

        groups = parser.parse("i want to eat chinese tonight", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDateTime(dates.get(0), 2, 20, 2011, 20, 0, 0);

        groups = parser.parse("Watch School Spirits on June 20 on syfy channel", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 6, 20, 2011);

        groups = parser.parse("Watch School Spirits on June 20 on", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 6, 20, 2011);

        groups = parser.parse("Watch School Spirits on June 20", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 6, 20, 2011);

        groups = parser.parse("hillary clinton sep 13, 2013", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 9, 13, 2013);
        assertThat(groups.get(0).getPosition()).isEqualTo(17);
        assertThat(groups.get(0).getText()).isEqualTo("sep 13, 2013");

        groups = parser.parse("hillary clinton 9/13/2013", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 9, 13, 2013);
        assertThat(groups.get(0).getPosition()).isEqualTo(17);
        assertThat(groups.get(0).getText()).isEqualTo("9/13/2013");

        groups = parser.parse("hillary clintoo sep 13, 2013", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 9, 13, 2013);
        assertThat(groups.get(0).getPosition()).isEqualTo(17);
        assertThat(groups.get(0).getText()).isEqualTo("sep 13, 2013");

        groups = parser.parse("clinton sep 13 2013", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 9, 13, 2013);
        assertThat(groups.get(0).getPosition()).isEqualTo(9);
        assertThat(groups.get(0).getText()).isEqualTo("sep 13 2013");

        groups = parser.parse("wedding dinner with Pam", reference);
        assertThat(groups.size()).isEqualTo(0);

        groups = parser.parse("yummy fried chicken", reference);
        assertThat(groups.size()).isEqualTo(0);

        groups = parser.parse("I am friend with Pam", reference);
        assertThat(groups.size()).isEqualTo(0);

        groups = parser.parse("bfriday blah blah", reference);
        assertThat(groups.size()).isEqualTo(0);

        groups = parser.parse("dinner bmong friends", reference);
        assertThat(groups.size()).isEqualTo(0);

        groups = parser.parse("I know we should meet tomorrow", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 2, 21, 2011);

        groups = parser.parse("**SHOT 01/31/15**", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 1, 31, 2015);

        groups = parser.parse("KOSTROMA REGION, RUSSIA. SEPTEMBER 24, 2014. A woman cleaning up fallen leaves on the grounds of the Shchelykovo museum reserve of Russian playwright Alexander Ostrovsky.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 9, 24, 2014);

        groups = parser.parse("21 November 2014-NYC-USA **** STRICTLY NOT AVAILABLE FOR USA ***", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 11, 21, 2014);

        groups = parser.parse("...all the backstory I needed in the first two minutes. From there, I....", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDateTime(dates.get(0), 2, 20, 2011, 0, 2, 0);

        groups = parser.parse("earthquake occured 5km NNW of Vincent, California at 07:34 UTC! #earthquake #Vincent http://t.co/6e4fAC6hTU", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDateTime(dates.get(0), 2, 20, 2011, 2, 34, 0);

        groups = parser.parse("Caricature: Person with anti-German foreign press. From: Le Rire, Paris, Spring 1933.", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 3, 20, 1933);

        groups = parser.parse("Person with Generaloberst von Seeckt in Bad Nauheim. Photograph. Autumn 1936", reference);
        assertThat(groups).hasSize(1);
        dates = groups.get(0).getDates();
        assertThat(dates).hasSize(1);
        validateDate(dates.get(0), 9, 23, 1936);
    }

    @Test
    public void testLocations() throws Exception {
        Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("2/20/2011");
        calendarSource = new CalendarSource(reference);

        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse("I want to go to the movies on september 1st.  Or maybe we should go on October 3rd.", reference);
        assertThat(groups.size()).isEqualTo(2);
        DateGroup group = groups.get(0);
        assertThat(group.getLine()).isEqualTo(1);
        assertThat(group.getPosition()).isEqualTo(31);
        group = groups.get(1);
        assertThat(group.getLine()).isEqualTo(1);
        assertThat(group.getPosition()).isEqualTo(72);

        groups = parser.parse("I want to go to \nthe movies on september 1st to see The Alan Turing Movie.", reference);
        assertThat(groups).hasSize(1);
        group = groups.get(0);
        assertThat(group.getLine()).isEqualTo(2);
        assertThat(group.getPosition()).isEqualTo(15);
    }

    @Test
    public void testPrefixSuffix() throws Exception {

        Date reference = DateFormat.getDateInstance(DateFormat.SHORT).parse("2/20/2011");
        calendarSource = new CalendarSource(reference);

        // no prefix or suffix
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse("Sept. 1st");
        assertThat(groups).hasSize(1);
        DateGroup group = groups.get(0);
        assertThat(group.getPrefix(3)).hasSize(0);
        assertThat(group.getSuffix(3)).hasSize(0);

        // no prefix
        groups = parser.parse("Sept. 1st is the date");
        assertThat(groups).hasSize(1);
        group = groups.get(0);
        assertThat(group.getPrefix(3)).hasSize(0);
        String suffix = group.getSuffix(3);
        assertThat(suffix).isEqualTo(" is");

        // no suffix
        groups = parser.parse("The date is Sept. 1st");
        assertThat(groups).hasSize(1);
        group = groups.get(0);
        String prefix = group.getPrefix(3);
        assertThat(prefix).hasSize(3).isEqualTo("is ");
        assertThat(group.getSuffix(3)).hasSize(0);

        // ask for a larger prefix than available
        groups = parser.parse("a Sept. 1st");
        assertThat(groups).hasSize(1);
        group = groups.get(0);
        prefix = group.getPrefix(5);
        assertThat(prefix).isEqualTo("a ");
        assertThat(group.getSuffix(3)).hasSize(0);

        // ask for a larger suffix than available
        groups = parser.parse("Sept. 1st a");
        assertThat(groups).hasSize(1);
        group = groups.get(0);
        suffix = group.getSuffix(5);
        assertThat(suffix).isEqualTo(" a");
        assertThat(group.getPrefix(3)).hasSize(0);

        // ask for a larger prefix and suffix than available
        groups = parser.parse("a Sept. 1st a");
        assertThat(groups).hasSize(1);
        group = groups.get(0);
        prefix = group.getPrefix(5);
        suffix = group.getSuffix(5);
        assertThat(prefix).isEqualTo("a ");
        assertThat(suffix).isEqualTo(" a");
    }

    @Test
    public void testNoDates() {
        List<Date> dates = parseCollection(new Date(), "Fried Chicken, Wedding Dinner");
        assertThat(dates).hasSize(0);

        parseCollection(new Date(), "Cleveland");
        assertThat(dates).hasSize(0);
    }

}
