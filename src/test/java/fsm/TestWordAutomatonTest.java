package fsm;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestWordAutomatonTest {

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
            "abcTESTabc, F",
            "abcTES,     3",
            "TEST,       F",
            "TESTabc,    F",
            "abcTEST,    F",
            "TESTTEST,   F",
            "TESTxyz!,   F",
            "'',         S",
            "abc,        S",
            "T,          1",
            "abcT,       1",
            "TE,         2",
            "xxTE,       2",
            "TES,        3",
            "Tx,         S",
            "TEx,        S",
            "TESx,       S",
            "TESx TES,   3",
            "test,       S",
            "TeSt,       S",
            "TE St,      S",
    })
    void functionalCases(String input, String expected) {
        assertEquals(expected, TestWordAutomaton.run(input));
    }

    @ParameterizedTest(name = "bug: \"{0}\" -> {1}")
    @CsvSource({
            "TTEST,       F",
            "TTTEST,      F",
            "abcTTESTabc, F",
            "TETEST,      F",
            "TETTEST,     F",
            "TTETEST,     F",
            "xTETESTx,    F",
            "TT,          1",
            "TET,         1",
            "TETE,        2",
            "TETES,       3",
    })
    void lostTCases(String input, String expected) {
        assertEquals(expected, TestWordAutomaton.run(input));
    }
}
