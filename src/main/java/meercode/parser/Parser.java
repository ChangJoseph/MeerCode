package meercode.parser;

import java.util.ArrayList;
import java.util.List;

public final class Parser {

    List<List<String>> mTokens;

    public static void main(String[] args) {
        List<List<String>> tokens = new ArrayList<List<String>>();
        tokens.add(new ArrayList<String>());
        tokens.get(0).add("test 0 0");
        tokens.get(0).add("test 0 1");
        tokens.add(new ArrayList<String>());
        tokens.get(1).add("test 1 0");

        AbstractSyntaxTree ast = Parser.parseTokens(tokens);
    }

    private Parser() {
        throw Exception;
    }

    public static AbstractSyntaxTree parseTokens(List<List<String>> pTokens) {
        AbstractSyntaxTree tree = new AbstractSyntaxTree();
        //Parse here
        return tree;
    }
}