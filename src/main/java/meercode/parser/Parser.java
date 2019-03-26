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
        Node temp = tree.getHead();

        // for (List<String> rows : pTokens) {
        //     for (String token : rows) {
        //         if (kKeywords.contains(token))
        //         {
        //             tree.getHead().mLeft = new Node(token);
        //         }
        //     }
        // }

        // TODO make the 
        for (int count = 0; count < pTokens.size(); count++) {
            List<String> row = pTokens.get(count);
            ParserMeta meta = processMeta(row); // information about the line of code
            boolean list2d = meta.getListBool(); // does this line of code need more lines?
            char astType = meta.getASTType(); // what type of ast is this?
            switch(astType) {
                case 'c':
                    if (list2d) {
                        int endIndex = -1;
                        List<List<String>> conditionalRows;
                        for (int endFinder = count; endFinder < pTokens.size(); endFinder++) {
                            if (pTokens.get(endFinder).get(0).equals("end")) {
                                endIndex = endFinder;
                                break; // TODO do I want this break here?
                            }
                        }
                        if (endIndex < 0) {
                            throw new IllegalArgumentException("Syntax Error: Missing keyword 'end'");
                        }
                        conditionalRows = pTokens.subList(count, endIndex);
                        temp.mLeft = multiLineConditionalAST(conditionalRows);
                    }
                    else temp.mLeft = conditionalAST(row); // TODO see how we store the methods into our 'tree' AST
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
                case 'k': System.out.println("Yur doin sumthin wrong boi");
                case 'f': temp.mLeft = functionAST(row);
                case 'o': temp.mLeft = operatorAST(row);
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
            // data = data.replaceAll("[()]", ""); bad idea m8
            currentNode = new Node(data);
            if (data.equals(")"))
            {
                //do nothing
                int b = 1;
            }
            else if (data.equals("("))
            {
                List<String> parenList = pTokens.subList(i + 1, pTokens.indexOf(")"));
                Node temp = operatorAST(parenList);
                headNode.mRight = new Node(pTokens.get(i +1));
                headNode.mRight.mLeft = temp;
                i+= parenList.size();
            }
            else if (headNode.mLeft != null && headNode.mRight != null)
            {
                Node temp = headNode;
                headNode = new Node(pTokens.get(i + 1));
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
             currentNode.mMiddle = functionAST(trueTokens);
        }

        // Case when only otherwise (else) exists
        else if (indexOfOtherwise != -1) {
            // The true statements
            List<String> trueTokens = pTokens.subList(indexOfThen, indexOfOtherwise);
            currentNode.mMiddle = functionAST(trueTokens);

            // The false statements
            List<String> falseTokens = pTokens.subList(indexOfOtherwise, indexOfEnd);
            currentNode.mRight = functionAST(trueTokens);
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
            currentNode.mMiddle = functionAST(trueTokens);

            // The false statements
            List<String> falseTokens = pTokens.subList(indexOfWhatIf, indexOfEnd);
            currentNode.mRight = functionAST(trueTokens);
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

        // Indices of each keyword (-1 if nonexistant)
        int indexRowOfThen;
        int indexOfThen;
        for (int row = 0; row < pTokens.size(); row++) {
            indexOfThen = pTokens.get(row).indexOf("then");
            if (indexOfThen >= 0) {
                indexRowOfThen = row;
                break;
            }
        }
        int indexOfWhatIf;
        int indexRowOfWhatIf;
        for (int row = indexRowOfThen; row < pTokens.size(); row++) {
            indexOfWhatIf = pTokens.get(row).indexOf("whatif");
            if (indexOfThen >= 0) {
                indexRowOfThen = row;
                break;
            }
        }
        int indexOfOtherwise;
        int indexRowOfOtherwise;
        for (int row = indexRowOfThen; row < pTokens.size(); row++) {
            indexOfOtherwise = pTokens.get(row).indexOf("otherwise");
            if (indexOfOtherwise >= 0) {
                indexRowOfOtherwise = row;
                break;
            }
        }
        int indexOfEnd;
        int indexRowOfEnd;
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
                currentNode.mLeft = functionAST(row);
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
                currentNode.mLeft = functionAST(row);
                currentNode.mRight = new Node("NOP");
                currentNode = currentNode.mRight; // TODO fencepost problem
            }

            currentNode = header;
            // The false statements
            List<List<String>> falseTokens = pTokens.subList(indexRowOfOtherwise, indexOfEnd);
            currentNode.mRight = new Node("NOP");
            currentNode = currentNode.mRight;
            for (List<String> row : falseTokens) {
                currentNode.mLeft = functionAST(row);
                currentNode.mRight = new Node("NOP");
                currentNode = currentNode.mRight; // TODO fencepost problem
            }
        }
        // Case when only whatif (else if) exists
        else if (indexOfWhatIf != -1) {
            // The true statements
            List<String> trueTokens = pTokens.subList(indexOfThen, indexOfWhatIf);
            currentNode.mMiddle = functionAST(trueTokens);

            // The false statements
            List<String> falseTokens = pTokens.subList(indexOfWhatIf, indexOfEnd);
            currentNode.mRight = functionAST(trueTokens);
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
