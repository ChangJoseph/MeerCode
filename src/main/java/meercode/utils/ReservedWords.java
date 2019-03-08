package meercode.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ReservedWords {

    private static final String[] kArrayReservedWords =
        {"import","if","then","whatIf","otherwise","repeatWhile","break","end","none","+","-","*","/","^",">","<",">=","<=","==","!=","not","or","and","is"};
    private static final List<String> kReservedWords = new ArrayList<String>(Arrays.asList(kArrayReservedWords));

    public static List<String> getReservedWords() {
        return kReservedWords.subList(0, 9);
    }
    public static List<String> getOperators() {
        return kReservedWords.subList(9, kReservedWords.size());
    }
    public static List<String> getComparators() {
        return kReservedWords.subList(14, kReservedWords.size());
    }

}