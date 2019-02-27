package meercode.parser;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    List<List<String>> mTokens;

    public static void main(String[] args) {
        List<List<String>> tokens = new ArrayList<List<String>>();
        tokens.add(new ArrayList<String>());
        tokens.get(0).add("test 0 0");
        tokens.get(0).add("test 0 1");
        tokens.add(new ArrayList<String>());
        tokens.get(1).add("test 1 0");
        Parser parser = new Parser(tokens);
    }

    public Parser(List<List<String>> pTokens) {

    }

    public static Abstract
}