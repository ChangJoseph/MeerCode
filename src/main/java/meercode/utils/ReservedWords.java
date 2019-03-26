package meercode.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ReservedWords {

    private static final String[] kArrayReservedWords =
        {"import","if","then","whatif","if","while","otherwise","repeatwhile","break","end","none","+","-","*","/","^",">","<",">=","<=","==","!=","not","or","and","is","%"};
    private static final String[] kArray3ACComparators = 
        {"<",">","<=",">=","!=","==","or","and"};    
    private static final String[] kArray3ACOperators = 
        {"+","-","/","*","%","^"};
    private static final List<String> kReservedWords = new ArrayList<String>(Arrays.asList(kArrayReservedWords));
    private static final List<String> k3ACComparators = new ArrayList<String>(Arrays.asList(kArray3ACComparators));
    private static final List<String> k3ACOperators = new ArrayList<String>(Arrays.asList(kArray3ACOperators));
    public static List<String> getReservedWords() {
        return kReservedWords;
    }
    public static List<String> getOperators() {
        return kReservedWords.subList(9, kReservedWords.size());
    }
    public static List<String> getComparators() {
        return kReservedWords.subList(14, kReservedWords.size());
    }
    public static List<String> get3ACComparators()
    {
        return ( k3ACComparators);
    }
    public static List<String> get3ACOperators()
    {
        return( k3ACOperators);
    }

}