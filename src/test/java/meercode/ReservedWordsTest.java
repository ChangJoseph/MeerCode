package meercode;

import org.junit.Test;

import meercode.utils.ReservedWords;

import static org.junit.Assert.*;

public class ReservedWordsTest {
    private String testReservedWords = "[import, if, then, whatIf, otherwise, repeatWhile, break, end, none, +, -, *, /, ^, >, <, >=, <=, ==, !=, not, or, and, is]";

    @Test public void reservedWordsMethods() {
        // assertNotNull("Reserved Words exists", ReservedWords.getReservedWords());
        // assertNotNull("Operators exists", ReservedWords.getOperators());
        // assertNotNull("Comparators exists", ReservedWords.getComparators());
        // assertEquals("Reserved Words Equal", testReservedWords, ReservedWords.getReservedWords().toString());
    }
}