package org.natty;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Joe Stelmach
 */
class RecurrenceTest extends AbstractTest {
    @BeforeAll
    public static void oneTime() {
        Locale.setDefault(Locale.US);
        TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));
        initCalendarAndParser();
    }

    @Test
    void testRelative() throws Exception {

        final LocalDateTime refDateTime = LocalDateTime.of(2011, 3, 3, 12, 00, 00);
        Date reference = Timestamp.valueOf(refDateTime);
        calendarSource = new CalendarSource(reference);

        DateGroup group = parser.parse("every friday until two tuesdays from now", reference).get(0);
        assertThat(group.getDates()).hasSize(1);
        validateDate(group.getDates().get(0), 3, 4, 2011);
        assertThat(group.isRecurring()).isTrue();
        validateDate(group.getRecursUntil(), 3, 15, 2011);

        group = parser.parse("every saturday or sunday", reference).get(0);
        assertThat(group.getDates()).hasSize(2);

        validateDate(group.getDates().get(0), 3, 5, 2011);
        validateDate(group.getDates().get(1), 3, 6, 2011);
        assertThat(group.isRecurring()).isTrue();
        assertThat(group.getRecursUntil()).isNull();
    }
}
