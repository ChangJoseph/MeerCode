package meercode.parser;

import java.util.ArrayList;
import java.util.List;
// import java.lang.Exception;
import meercode.ast.*;
import meercode.utils.*;

public final class Parser {

    private List<List<String>> mTokens;
    private static List<String> mKeywords = ReservedWords.getReservedWords();
    private static List<String> Merators = ReservedWords.getOperators();
    private static List<String> mComparators = ReservedWords.getComparators();

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
        // throw new Exception("Error");
    }

    public static AbstractSyntaxTree parseTokens(List<List<String>> pTokens) {
        AbstractSyntaxTree tree = new AbstractSyntaxTree(new Node("NOP"));
        for (List<String> rows : pTokens) {
            for (String token : rows) {
                if (keywords.contains(token))
                {
                    tree.getHead().left = new Node(token, tree.getHead());
                }
            }
        }
        return tree;
    }

    private static Node conditionalAST()
    {
        return null;
    }
}