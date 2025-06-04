package org.natty;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Joe Stelmach
 */
class CPANTest {

    @Test
    void sanityCheck() throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    CPANTest.class.getResourceAsStream("/cpan.txt")));

            String value = null;
            while ((value = reader.readLine()) != null) {
                if (!value.trim().startsWith("#") && !value.trim().isEmpty()) {
                    Parser parser = new Parser();
                    List<DateGroup> groups = parser.parse(value);
                    assertThat(groups).hasSize(1);
                    assertThat(groups.get(0).getDates()).isNotEmpty();
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
