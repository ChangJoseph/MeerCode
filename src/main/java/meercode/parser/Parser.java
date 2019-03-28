package meercode.parser;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import meercode.ast.*;
import meercode.utils.*;

// TODO If time allows for these:
// allow whatif AND otherwise able to be in the same statement
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
        // Code line 1
        tokens.add(new ArrayList<String>());
        tokens.get(0).add("if");
        tokens.get(0).add("(");
        tokens.get(0).add("5");
        tokens.get(0).add("<");
        tokens.get(0).add("2");
        tokens.get(0).add(")");
        tokens.get(0).add("then");
        tokens.get(0).add("say");
        tokens.get(0).add("\"true\"");
        tokens.get(0).add("otherwise");
        tokens.get(0).add("say");
        tokens.get(0).add("\"false\"");
        tokens.get(0).add("end");

        // Code line 2
        // tokens.add(new ArrayList<String>());
        // tokens.get(1).add("say");
        // tokens.get(1).add("\"line 2 here\"");

        AbstractSyntaxTree ast = Parser.parseTokens(tokens);

        try {
            System.out.println(ast.toString());
        }
        catch (NullPointerException e) {
            System.out.println("Error: testing tree has a null");
        }
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
        Node temp = tree.getHead();

        for (int count = 0; count < pTokens.size(); count++) {
            List<String> row = pTokens.get(count);
            ParserMeta meta = processMeta(row); // information about the line of code
            boolean list2d = meta.getListBool(); // 1d or 2d list needed
            char astType = meta.getASTType(); // what type of ast
            switch(astType) {
                case 'c':
                    if (list2d) {
                        int endIndex = -1;
                        List<List<String>> conditionalRows;
                        // Loop to find the keyword "end"
                        for (int endFinder = count; endFinder < pTokens.size(); endFinder++) {
                            if (pTokens.get(endFinder).get(0).equals("end")) {
                                endIndex = endFinder;
                                break;
                            }
                        }
                        if (endIndex < 0) {
                            throw new IllegalArgumentException("Syntax Error: Missing keyword 'end'");
                        }
                        // Sets the 2d array from 'if' to 'end'
                        conditionalRows = pTokens.subList(count, endIndex);
                        temp.mLeft = multiLineConditionalAST(conditionalRows);
                        count = endIndex; // TODO check this
                    }
                    else temp.mLeft = conditionalAST(row);
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
                    temp.mLeft = multiLineConditionalAST(pTokens.subList(count, c));
                    count = c;
                case 'o':
                    temp.mLeft = operatorAST(row);
                    // System.out.println(temp.mLeft.mLeft.mData);
                case 'k':
                    temp.mLeft = functionAST(row);
                default:
                    temp.mLeft = operatorAST(row);
                    
            }
            temp.mRight = new Node("NOP");
            temp = temp.mRight; // TODO fencepost problem
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

        if (function.equals("if")) {
            // If the 'if' is a single line
            if (pTokens.contains("end")) meta = new ParserMeta(false, 'c');

            // If the 'if' is a block
            else meta = new ParserMeta(true, 'c');
        }
        else if (function.equals("REPEATWHILE")) {
            meta = new ParserMeta(true, 'l');
        }
        else if (arrayContainsFromReference(pTokens, kOperators)) // find a faster way to check if pTokens has one or more element from kOperators
        {
            meta = new ParserMeta(false, 'o');
        }
        else if (function.matches("[a-z]+")) { // TODO check if this works
            meta = new ParserMeta(false, 'k');
        }
        else {
            throw new IllegalArgumentException("SYNTAX ERROR");
        }
        
        return meta;
    }






    // We have 4 main ast types: operators, intiializers*, conditionals, and functions
    /**
     * Takes in a line of code in form of tokens and turns it into a tree
     * Made for operator statements
     * @param pTokens The tokens to process
     * @return The head of the operation AST generated by the method
     */
    private static Node operatorAST(List<String> pTokens)
    {
        Node headNode = new Node(pTokens.get(0), getOpFlag(pTokens.get(0)));
        Node currentNode = new Node();

        for (int i = 1; i < pTokens.size(); i++)
        {
            //get element from the list
            String data = pTokens.get(i);
            //make a node using the element
            currentNode = new Node(data, getOpFlag(data));
            //check the node's data to see which flag it takes in 
            

            if (data.equals(")"))
            {
                //do nothing
                int b = 1;
            }
            else if (data.equals("("))
            {
                List<String> parenList = pTokens.subList(i + 1, pTokens.indexOf(")"));
                Node temp = operatorAST(parenList);
                headNode.mRight = new Node(pTokens.get(i +1), getOpFlag(pTokens.get(i+1)));
                headNode.mRight.mLeft = temp;
                i += parenList.size();
            }
            else if (headNode.mLeft != null && headNode.mRight != null)
            {
                Node temp = headNode;
                headNode = new Node(pTokens.get(i + 1), getOpFlag(pTokens.get(i + 1)));
                headNode.mLeft = temp;
                i++;
            }
            else if (headNode.mLeft == null)
            {
                Node temp = headNode;
                headNode = currentNode;
                headNode.mLeft = temp;
                
            }
            else if (headNode.mRight == null && !kComparators.contains(data) && !kOperators.contains(data))
            {
                headNode.mRight = new Node(data, getOpFlag(data));
                // System.out.println(headNode.mRight.mData);
            }
            else
            {
                headNode.mRight = operatorAST(pTokens.subList(i, pTokens.size()));
            }            
            
        }
        // System.out.println(headNode.mLeft.mData + headNode.mData + headNode.mRight.mData);
        return headNode;
    }

    private static char getOpFlag(String data)
    {
        if (data.matches("[0-9]+"))
        {
            return 'n';
        }
        else if (data.matches("[a-z]+"))
        {
            return 'v';
        }
        else if (kOperators.contains(data))
        {
            return 'f';
        }
        else if (kComparators.contains(data))
        {
            return 'c';
        }
        else return 'f';
    }

    /**
     * If the line of code is initializing/setting a variable, we use this tree
     * DISCLAIMER: at the time of creation, this method is used because order of operations is not
     * functioning. This method will be deprecated in the future.
     * @param pLine the line of code to be processed
     * @return the full AST for the line of codea
     */
    private static Node initializerAST(List<String> pLine)
    {
        Node headNode = new Node(pLine.get(1));
        headNode.mLeft = new Node(pLine.get(0));
        headNode.mRight = operatorAST(pLine.subList(2, pLine.size()));
        return headNode;
    }

    /**
     * Takes in a line of code in form of tokens and turns it into a tree
     * Made for single line if statements
     * @param pTokens The tokens to process into a conditional tree
     * @return The head of the conditional AST generated by the method
     */
    private static Node conditionalAST(List<String> pTokens) // TODO Add tokens on each node
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
        else if (indexOfEnd == -1) {
            throw new IllegalArgumentException("Syntax Error: Missing keyword 'end'");
        }
        else if (indexOfEnd < indexOfThen) {
            throw new IllegalArgumentException("Syntax Error: Keyword 'end' must not come before keyword 'then'");
        }
        else if (indexOfEnd < indexOfWhatIf || indexOfEnd < indexOfWhatIf) {
            throw new IllegalArgumentException("Syntax Error: Keyword 'end' must come last in conditional (if statement) code");
        }
        else if (indexOfOtherwise < indexOfWhatIf) {
            throw new IllegalArgumentException("Syntax Error: Keyword 'whatif' is never reached because 'otherwise' precedes it");
        }
        // TODO think of more case statements and also add to multiline conditional AST


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
             currentNode.mMiddle = operatorAST(trueTokens);
        }

        // Case when only otherwise (else) exists
        else if (indexOfOtherwise != -1) {
            // The true statements
            List<String> trueTokens = pTokens.subList(indexOfThen, indexOfOtherwise);
            currentNode.mMiddle = operatorAST(trueTokens);

            // The false statements
            List<String> falseTokens = pTokens.subList(indexOfOtherwise, indexOfEnd);
            currentNode.mRight = operatorAST(trueTokens);
        }

        // TODO add this case if time allows
        // // Case when both whatif (else if) and otherwise (else) exist
        // else if (indexOfWhatIf != -1 && indexOfOtherwise != -1) {
        //     // The true statements
        //     List<String> trueTokens = pTokens.subList(indexOfThen, indexOfWhatIf);
        //     currentNode.mMiddle = functionAST(trueTokens);

        //     // The else if statements
        //     List<String> whatIfTokens = pTokens.subList(indexOfWhatIf, indexOfOtherwise);
        //     currentNode.mRight = functionAST(trueTokens);

        //     // The false statements
        //     List<String> falseTokens = pTokens.subList(indexOfOtherwise, indexOfEnd);
        // }

        // Case when only whatif (else if) exists
        else if (indexOfWhatIf != -1) {
            // The true statements
            List<String> trueTokens = pTokens.subList(indexOfThen, indexOfWhatIf);
            currentNode.mMiddle = operatorAST(trueTokens);

            // The false statements
            List<String> falseTokens = pTokens.subList(indexOfWhatIf, indexOfEnd);
            currentNode.mRight = operatorAST(trueTokens);
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
     * @param pTokens The tokens to process into a conditional tree
     * @return The head of the conditional AST generated by the method
     */
    private static Node multiLineConditionalAST(List<List<String>> pTokens)
    {
        Node headNode = new Node(pTokens.get(0).get(0)); // What we return
        Node currentNode = headNode; // A temporary node to write on as we go through tokens

        // Case when the multiple line conditional start with a whatif
        if (pTokens.get(0).get(0).equals("whatif")) {
            pTokens.get(0).set(0, "if");
        }

        // Indices of each keyword (-1 if nonexistant)
        int indexRowOfThen = -1;
        int indexOfThen = -1;
        for (int row = 0; row < pTokens.size(); row++) {
            indexOfThen = pTokens.get(row).indexOf("then");
            if (indexOfThen >= 0) {
                indexRowOfThen = row;
                break;
            }
        }
        int indexOfWhatIf = -1;
        int indexRowOfWhatIf = -1;
        for (int row = indexRowOfThen; row < pTokens.size(); row++) {
            indexOfWhatIf = pTokens.get(row).indexOf("whatif");
            if (indexOfThen >= 0) {
                indexRowOfThen = row;
                break;
            }
        }
        int indexOfOtherwise = -1;
        int indexRowOfOtherwise = -1;
        for (int row = indexRowOfThen; row < pTokens.size(); row++) {
            indexOfOtherwise = pTokens.get(row).indexOf("otherwise");
            if (indexOfOtherwise >= 0) {
                indexRowOfOtherwise = row;
                break;
            }
        }
        int indexOfEnd = -1;
        int indexRowOfEnd = -1;
        for (int row = indexRowOfThen; row < pTokens.size(); row++) {
            indexOfEnd = pTokens.get(row).indexOf("end");
            if (indexOfEnd >= 0) {
                indexRowOfEnd = row;
                break;
            }
        }

        // Bunch of cases to make sure everything in code is written correctly
        if (indexOfThen == -1) {
            throw new IllegalArgumentException("Syntax Error: Missing keyword 'then'");
        }
        else if (indexOfEnd == -1) {
            throw new IllegalArgumentException("Syntax Error: Missing keyword 'end'");
        }
        else if (indexOfEnd < indexOfThen) {
            throw new IllegalArgumentException("Syntax Error: Keyword 'end' must not come before keyword 'then'");
        }
        else if (indexOfEnd < indexOfWhatIf || indexOfEnd < indexOfWhatIf) {
            throw new IllegalArgumentException("Syntax Error: Keyword 'end' must come last in conditional (if statement) code");
        }
        else if (indexOfOtherwise < indexOfWhatIf) {
            throw new IllegalArgumentException("Syntax Error: Keyword 'whatif' is never reached because 'otherwise' precedes it");
        }


        // The conditional expression tokens
        List<String> conditionalTokens = pTokens.get(0).subList(1, indexOfThen);

        // Turning the conditional expression tokens into a tree
        Node conditionalExpressionNode = operatorAST(conditionalTokens);

        // Puts the conditional expression node in the main node's mLeft
        currentNode.mLeft = conditionalExpressionNode;
        

        // Case when neither whatif (else if) or otherwise (else) exist
        if (indexOfWhatIf == -1 && indexOfOtherwise == -1) {
             // The true statements
             List<List<String>> trueTokens = pTokens.subList(indexRowOfThen, indexRowOfEnd);
             currentNode.mMiddle = new Node("NOP");
             currentNode = currentNode.mMiddle;
             for (List<String> row : trueTokens) {
                currentNode.mLeft = operatorAST(row);
                currentNode.mRight = new Node("NOP");
                currentNode = currentNode.mRight;
             }
        }
        // Case when only otherwise (else) exists
        else if (indexOfOtherwise != -1) {
            Node header = currentNode;
            // The true statements
            List<List<String>> trueTokens = pTokens.subList(indexRowOfThen, indexRowOfOtherwise);
            currentNode.mMiddle = new Node("NOP");
            currentNode = currentNode.mMiddle;
            for (List<String> row : trueTokens) {
                currentNode.mLeft = operatorAST(row);
                currentNode.mRight = new Node("NOP");
                currentNode = currentNode.mRight; // TODO fencepost problem
            }

            currentNode = header;
            // The false statements
            List<List<String>> falseTokens = pTokens.subList(indexRowOfOtherwise, indexOfEnd);
            currentNode.mRight = new Node("NOP");
            currentNode = currentNode.mRight;
            for (List<String> row : falseTokens) {
                currentNode.mLeft = operatorAST(row);
                currentNode.mRight = new Node("NOP");
                currentNode = currentNode.mRight; // TODO fencepost problem
            }
        }
        // Case when only whatif (else if) exists
        else if (indexOfWhatIf != -1) {
            // The true statements
            List<List<String>> trueTokens = pTokens.subList(indexRowOfThen, indexRowOfWhatIf);
            currentNode.mMiddle = new Node("NOP");
            currentNode = currentNode.mMiddle;
            for (List<String> row : trueTokens) {
                currentNode.mLeft = operatorAST(row);
                currentNode.mRight = new Node("NOP");
                currentNode = currentNode.mRight; // TODO fencepost problem
            }

            // The false statements
            List<List<String>> falseTokens = pTokens.subList(indexRowOfWhatIf, indexRowOfEnd);
            currentNode.mRight = multiLineConditionalAST(falseTokens);
            
        }
        // Catch-all case
        else {
            throw new IllegalArgumentException("Something bad has happened in the parser");
        }


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
        Node currentNode = new Node(pTokens.get(0));

        int indexParametersStart = -1;
        int indexParametersEnd = -1;

        // Finds where the '(' is
        for (int count = 1; count < pTokens.size(); count++) {
            String token = pTokens.get(count);
            if (token.equals("(")) {
                indexParametersStart = count;
                break;
            }
        }
        // Finds where the ')' is
        for (int count = indexParametersStart; count < pTokens.size(); count++) {
            String token = pTokens.get(count);
            if (token.equals("(")) {
                indexParametersEnd = count;
                break;
            }
        }

        if (indexParametersStart < 0 || indexParametersEnd < 0) {
            throw new IllegalArgumentException("Syntax Error: parenthesis are not found");
        }

        List<String> parameters = pTokens.subList(indexParametersStart, indexParametersEnd);

        // Sets each token in the parenthesis to its own node on the left repeating
        for (String param : parameters) {
            currentNode.mLeft = new Node(param);
            currentNode.mLeft.setParent(currentNode);
            currentNode = currentNode.mLeft;
        }

        return headNode;
    }

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
