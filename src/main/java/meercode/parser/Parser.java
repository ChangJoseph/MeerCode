package meercode.parser;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import meercode.ast.*;
import meercode.utils.*;

public final class Parser {

    private List<List<String>> kTokens;
    private final static List<String> kKeywords = ReservedWords.getReservedWords();
    private final static List<String> kOperators = ReservedWords.getOperators();
    private final static List<String> kComparators = ReservedWords.getComparators();

    /**
     * Our main testing method
     */
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

    /**
     * Our main Token interpreter
     * Creates a head (NOP) and puts function on mLeft, next line/node/NOP on mRight
     * @param pTokens The total list of tokens to translate into AST
     * @return The final AST based on all of the tokens given in the parameter
     */
    public static AbstractSyntaxTree parseTokens(List<List<String>> pTokens) {
        AbstractSyntaxTree tree = new AbstractSyntaxTree(new Node("NOP"));

        // for (List<String> rows : pTokens) {
        //     for (String token : rows) {
        //         if (kKeywords.contains(token))
        //         {
        //             tree.getHead().mLeft = new Node(token);
        //         }
        //     }
        // }

        for (int count = 0; count < pTokens.size(); count++) {
            List<String> row = pTokens.get(count);
            ParserMeta meta = processMeta(row);
            boolean list2d = meta.getListBool();
            char astType = meta.getASTType();
            switch(astType) {
                case 'c':
                    tree.getHead().mLeft = conditionalAST(row); // TODO see how we store the methods into our 'tree' AST
                    break;
                case 'l': 
                    int c = count;
                    try {
                        while(!pTokens.get(c).contains("end"))
                        {
                            c++;
                        }
                    }
                    catch (IndexOutOfBoundsException ie) 
                    {
                        System.out.println("You gotta write end after your blocks bucko");
                    }
                    tree.getHead().mLeft = multiLineConditionalAST(pTokens.subList(count, c));
                    count = c;
                case 'k': System.out.println("Yur doin sumthin wrong boi");
                case 'f': tree.getHead().mLeft = functionAST(row);
                case 'o': tree.getHead().mLeft = operatorAST(row);
            }
        }
        return tree;
    }

    /**
     * Processes a line of code and gives back info on it
     * @param pTokens The line of code in form of list of tokens
     * @return ParserMeta class that contains the info
     */
    private static ParserMeta processMeta(List<String> pTokens) {
        ParserMeta meta;
        String function = pTokens.get(0);
        String upperCaseFunction = function.toUpperCase();

        if (upperCaseFunction.equals("IF")) { // TODO What if the END is on the same line? (it would have to be ParserMeta(false, 'c') )
            meta = new ParserMeta(true, 'c');
        }
        else if (upperCaseFunction.equals("REPEATWHILE")) {
            meta = new ParserMeta(true, 'l');
        }
        else if (arrayContainsFromReference(pTokens, kOperators)) // TODO find a faster way to check if pTokens has one or more element from kOperators
        {
            meta = new ParserMeta(false, 'o');
        }
        else if (function.matches("[A-Z]")) {
            meta = new ParserMeta(false, 'a'); // TODO figure out ambiguity
        }
        else {
            throw new IllegalArgumentException("SYNTAX ERROR");
        }
        
        return meta;
    }






    // We have 3 main ast types: operators, conditionals, and functions
    /**
     * Takes in a line of code in form of tokens and turns it into a tree
     * Made for operator statements
     * @param pTokens The tokens to process
     * @return The head of the operation AST generated by the method
     */
    private static Node operatorAST(List<String> pTokens)
    {
        Node headNode = new Node(pTokens.get(0));
        Node currentNode = new Node();

        for (int i = 1; i < pTokens.size(); i++)
        {
            String data = pTokens.get(i);
            data = data.replaceAll("[()]", "");
            currentNode = new Node(data);
            if (headNode.mLeft == null)
            {
                currentNode.mLeft = headNode;
                headNode = currentNode;
            }
            else if (headNode.mRight == null && !kComparators.contains(data) && !kOperators.contains(data))
            {
                headNode.mRight = new Node(data);
            }
            else
            {
                headNode.mRight = operatorAST(pTokens.subList(i, pTokens.size()));
            }            
            
        }
        return headNode;
    }
    /**
     * Takes in a line of code in form of tokens and turns it into a tree
     * Made for if statements
     * @param pTokens The tokens to process
     * @return The head of the conditional AST generated by the method
     */
    private static Node conditionalAST(List<String> pTokens) // TODO I only made a variable to track which tokens to use in each scenario; make them do something
    {
        Node headNode = new Node(pTokens.get(0)); // What we return
        Node currentNode = headNode; // A temporary node to write on as we go through tokens

        // Indices of each keyword (-1 if nonexistant)
        int indexOfThen = pTokens.indexOf("then");
        int indexOfWhatIf = pTokens.indexOf("whatif");
        int indexOfOtherwise = pTokens.indexOf("otherwise");
        int indexOfEnd = pTokens.indexOf("end");

        // Bunch of cases to make sure everything in code is written correctly
        if (indexOfThen == -1) {
            throw new IllegalArgumentException("Syntax Error: Missing keyword 'then'");
        }
        if (indexOfEnd == -1) {
            throw new IllegalArgumentException("Syntax Error: Missing keyword 'end'");
        }
        if (indexOfEnd < indexOfThen) {
            throw new IllegalArgumentException("Syntax Error: Keyword 'end' must not come before keyword 'then'");
        }
        if (indexOfEnd < indexOfWhatIf || indexOfEnd < indexOfWhatIf) {
            throw new IllegalArgumentException("Syntax Error: Keyword 'end' must come last in conditional (if statement) code");
        }
        if (indexOfOtherwise < indexOfWhatIf) {
            throw new IllegalArgumentException("Syntax Error: Keyword 'whatif' is never reached because 'otherwise' precedes it");
        }
        // TODO think of more case statements


        // The conditional expression tokens
        List<String> conditionalTokens = pTokens.subList(1, indexOfThen);

        // Turning the conditional expression tokens into a tree
        Node conditionalExpressionNode = operatorAST(conditionalTokens);

        // Puts the conditional expression node in the main node's mLeft
        currentNode.mLeft = conditionalExpressionNode;
        

        // Case when neither whatif (else if) or otherwise (else) exist
        if (indexOfWhatIf == -1 && indexOfOtherwise == -1) {
             // The true statements
             List<String> trueTokens = pTokens.subList(indexOfThen, indexOfEnd);
        }
        // Case when only otherwise (else) exists
        else if (indexOfOtherwise != -1) {
            // The true statements
            List<String> trueTokens = pTokens.subList(indexOfThen, indexOfOtherwise);

            // The false statements
            List<String> falseTokens = pTokens.subList(indexOfOtherwise, indexOfEnd);
        }
        // Case when both whatif (else if) and otherwise (else) exist
        else if (indexOfWhatIf != -1 && indexOfOtherwise != -1) {
            // The true statements
            List<String> trueTokens = pTokens.subList(indexOfThen, indexOfWhatIf);

            // The else if statements
            List<String> whatIfTokens = pTokens.subList(indexOfWhatIf, indexOfOtherwise);

            // The false statements
            List<String> falseTokens = pTokens.subList(indexOfOtherwise, indexOfEnd);
        }
        // Case when only whatif (else if) exists
        else if (indexOfWhatIf != -1) {
            // The true statements
            List<String> trueTokens = pTokens.subList(indexOfThen, indexOfWhatIf);

            // The false statements
            List<String> falseTokens = pTokens.subList(indexOfWhatIf, indexOfEnd);
        }
        // Catch-all case
        else {
            throw new IllegalArgumentException("Something bad has happened in the parser");
        }


        return headNode;
    }
    /**
     * Takes in a line of code in form of tokens and turns it into a tree
     * Made for if statements that spans multiple lines
     * @param pTokens The tokens to process
     * @return The head of the conditional AST generated by the method
     */
    private static Node multiLineConditionalAST(List<List<String>> pTokens)
    {
        Node headNode = new Node(pTokens.get(0).get(0));
        Node currentNode = headNode;

        return headNode;
    }
    private static Node loopAST(List<String> pTokens)
    {
        Node headNode = new Node();
        Node currentNode = new Node();

        return headNode;
    }
    /**
     * Takes in a line of code in form of tokens and turns it into a tree
     * Made for general functions
     * @param pTokens The tokens to process
     * @return The head of the function AST generated by the method
     */
    private static Node functionAST(List<String> pTokens)
    {
        Node headNode = new Node();
        Node currentNode = new Node();

        return headNode;
    }
    // /**
    //  * 
    //  * @param pTokens The tokens to process 
    //  * @return The head of the operation AST generated by the method
    //  */
    // private static Node parenthesisGrouping(List<String> pTokens) {


    //     return null;
    // }

    /**
     * Checks if a line of code has any tokens that match any element from a reference list
     * @param pCheck The line of code (tokens) to check
     * @param pReference The list of elements to reference
     * @return If the pCheck contains any of pReference's elements
     */
    private static boolean arrayContainsFromReference(List<String> pCheck, List<String> pReference) {
        for (String elem : pCheck) {
            if (pReference.contains(elem)) return true;
        }
        return false;
    }
}
