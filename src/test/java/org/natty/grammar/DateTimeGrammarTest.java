package org.natty.grammar;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class DateTimeGrammarTest extends AbstractGrammarTest {

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(
            resources = "/test/datetime-alternative-test-data.csv",
            delimiter = '|',
            numLinesToSkip = 1,
            useHeadersInDisplayName = false
    )
    void date_time_alternative(String input, String expectedAst) throws Exception {
        ruleName = "date_time_alternative";
        assertAST(input, expectedAst);
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(
            resources = "/test/datetime-test-data.csv",
            delimiter = '|',
            numLinesToSkip = 1,
            useHeadersInDisplayName = false
    )
    void date_time(String input, String expectedAst) throws Exception {
        ruleName = "date_time";
        assertAST(input, expectedAst);
    }

}
