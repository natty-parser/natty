package org.natty.grammar;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class HolidayGrammarTest extends AbstractGrammarTest {

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(
            resources = "/test/holidays-test-data.csv",
            delimiter = '|',
            numLinesToSkip = 1,
            useHeadersInDisplayName = false
    )
    void names(String input, String expectedAst) throws Exception {
        ruleName = "holiday_name";
        assertAST(input, expectedAst);
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(
            resources = "/test/holiday-statement-test-data.csv",
            delimiter = '|',
            numLinesToSkip = 1,
            useHeadersInDisplayName = false
    )
    void statments(String input, String expectedAst) throws Exception {
        ruleName = "holiday";
        assertAST(input, expectedAst);
    }
}
