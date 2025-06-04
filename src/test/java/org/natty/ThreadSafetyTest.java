package org.natty;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class ThreadSafetyTest extends AbstractTest {

    private final static int NUM_OF_THREADS = 10;
    private final static int JOIN_TIMEOUT = 2000; // 2 seconds
    private static DateFormat dateFormat;

    private final AtomicInteger numOfCorrectResults = new AtomicInteger();

    @BeforeAll
    public static void oneTime() {
        Locale.setDefault(Locale.US);
        TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
        initCalendarAndParser();
    }

    @Test
    void testManyThreads() throws Exception {
        Thread[] threads = new Thread[NUM_OF_THREADS];
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            threads[i] = new Thread(new Tester(i));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join(JOIN_TIMEOUT);
        }
        assertThat(numOfCorrectResults.get()).isEqualTo(NUM_OF_THREADS);
    }

    private class Tester implements Runnable {

        private final Date referenceDate;
        private final int baseMinute;

        public Tester(int baseMinute) throws Exception {
            final LocalDateTime refDateTime = LocalDateTime.of(2011, 3, 3, 1, baseMinute, 0);
            this.referenceDate = Timestamp.valueOf(refDateTime);
            this.baseMinute = baseMinute;
        }

        public void run() {
            try {
                // Imitate some long running task.
                Thread.sleep(100);
            } catch (Exception e) {
            }
            String newDate = "4/4/2012";
            Date parsed = parser.parse(newDate, referenceDate).get(0).getDates().get(0);
            validateThread(parsed, baseMinute);
            numOfCorrectResults.incrementAndGet();
        }
    }

    // We need this method because validateDate and validateTime are not thread safe.
    private synchronized void validateThread(Date date, int baseMinute) {
        validateDate(date, 4, 4, 2012);
        validateTime(date, 1, baseMinute, 0);
    }
}
