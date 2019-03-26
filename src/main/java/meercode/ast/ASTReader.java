package meercode.ast;
import java.io.*;
import java.util.*;

import meercode.utils.ReservedWords;

import java.nio.file.*;

/*
    This class takes an AST and converts it into 3AC for the compiler step.
    Examples of AST's can be found in the documentation branch.
    The reader will always prioritize the left side of the tree before moving on to the right.
    The only case where it will go in the middle is for prints and the true condition of an if statement.
*/
public  final class ASTReader 
{
    private AbstractSyntaxTree tree; //The tree to be read from
    
     
    private int flagCount; // the current unique identifier for flags
    private List<String> functionList = new ArrayList<String>(); //List of recognized functions
    private String outputFile; //File path of file the 3AC is stored in
    
    public ASTReader(AbstractSyntaxTree tree, String outputFile) 
    {
        this.tree = tree;
        this.outputFile = outputFile;
        flagCount = 0;
        functionList = ReservedWords.getReservedWords(); //Uses the ReservedWords class to determine keywords
        
        
        
    }
    //Method takes a String and writes it to the outputFile
    private  void write(String mData) {
        try { 
  
            // Open given file in append mode. 
            BufferedWriter out = new BufferedWriter( 
                   new FileWriter(outputFile, true)); 
            out.write(mData); 
            out.close(); 
        } 
        catch (IOException e) { 
            //System.out.println("exception occoured" + e); 
        } 
    }
   //Begins the generation of the 3AC
    private void readTree() 
    {
        //System.out.println("Tree has a head of " + tree.getHead().mData + " with a flag " + tree.getHead().mFlag);
        Node head = tree.getHead(); //Gets the header Node from the tree
       genFunction(head); // Starts the recursive function the generate the 3AC
        
        
    }
    //Recursive method that takes in a node and adds it to the output file
    private String genFunction(Node node) 
    {      
        //System.out.println("Node is " + node.mData + " with flag " + node.mFlag); 
        flagCount++; //Increases the global flag identifier
        int curFlagCount = flagCount; //Locally stores the flag count for use with only the current Node
            
             if( node.mData.equals("if")) // If the node evaluates to and if statement
            {
                String jumpID = "JUMP" + curFlagCount; // Sets the jumpID using the current unique identifier
                String endID = "END" + curFlagCount; //Links the endID to the JumpID
               
                write( "if " + genFunction(node.mLeft) + " goto " + jumpID); // writes the code for the start of an
                                                                            // if statement using the recursive String return
                                                                            // to set up the conditional
                newLine();
                genFunction(node.mRight); // goes right on the tree ( the false branch)
                write("goto " + endID); //sets up the jump to end of if statement
                
                newLine();
                write(jumpID); //Flag for if statement is true
                newLine();
                genFunction(node.mMiddle); // goes middle on the tree (the true branch)
                write(endID); // Adds the flag for the end of the statement
                newLine();
                return("t"+ curFlagCount); //In this case this is unused


            }
            else if(node.mData.equals("NOP")) // If the node evaluates to a No-Operation
            {
                genFunction(node.mLeft); // evaluate the node on the left
                genFunction(node.mRight);// evaluate the node on the right
                return("t" + curFlagCount);//unused
            }
            else if(node.mData.equals("return"))// If the node evaluates to a return statement
            {
                write("ret " + genFunction(node.mMiddle)); //creates return statement using middle evaluation
                newLine();
                return("t" + curFlagCount); //unused
            }
            else if(node.mData.equals("say"))//If the node evaluates to a print statement
            {
                write("print " + genFunction(node.mMiddle));// creates a print statement using middle evaluation
                newLine();
                return("t" + curFlagCount);// unused
            }
            else if(node.mData.equals("repeatWhile"))//If the node evaluates to a while loop
            {
                String jumpID = "JUMP" + curFlagCount;//Sets the jumpID using the current unique identifier
                String endID = "END" + curFlagCount;//Links the endID with the jumpID
                write(jumpID);//Creates the flag for the start of the loop
                newLine();
                /*
                    This next line first finds the condition of the while loop and writes it to the file by
                    generating the function. It then sets the line that inverses the condition to make the code
                    easier going forward.
                */
                write("t" + curFlagCount + " = " + genFunction(node.mLeft) + " == ~k!false");
                newLine();
                /*
                    This line sets up the function that checks the inverse of the condition
                    then jumps to the end of the loop if the inverse is true (the exit condition)
                */
                write("if " + "t" + curFlagCount + " goto " + endID);
                newLine();
                genFunction(node.mRight);// goes right on the tree (gens the body of the loop)
                write("goto " + jumpID); // jumps back to the beginning of the loop
                newLine();
                write(endID); // creates the flag to break out of the loop
                newLine();
                return("t" + curFlagCount);// unused

            }
            else if(node.mData.equals("="))// if function is found to be an equals statement
            {
                write(node.mLeft.mData + " = " + genFunction(node.mRight));//sets the  left side of the tree equal to the right
                newLine();
                return("t" + curFlagCount);//unused
            }
            else if(!isFunction(node.mData))//If the data is found to be none of the above and not a function
            {
                //System.out.println("No function found on: " + node.mData);
                if(node.mFlag == 'v')//if the flag is a variable return the data which is used by other recuarsive methods
                {
                //System.out.println("Data evaluated to variable on: " + node.mData + " with flag " + node.mFlag);
                return(node.mData);
                }
                else // If the node is not a variable return the data with the additional constant flag appended to it
                {
                    //System.out.println("Data evaluated to constant on: " + node.mData + " with flag " + node.mFlag);
                    return("~k!" + node.mData);
                }
            }
            
            else 
            {
                
                /*
                    This is the base case for general functions and will evaluate the left and right sides
                    of the node and apply the operation to each of them
                */
                write( "t" + curFlagCount + " = " + genFunction(node.mLeft) + " " + node.mData + " " + genFunction(node.mRight) + "\n");
               //System.out.print(outputString);
               
               
               return("t"+ curFlagCount);//returns unique identifier to be used by other recursive functions
            }
            
    }
    //Determines if something is a function using the prebuilt function list
    private boolean isFunction(String mData)
    {
        return(functionList.contains(mData));
    }
    //Adds a new line character to the file.
    private void newLine()
    {
        write("\n");
    }
    //General starter method requiring a tree and a file path to wrtie to
    public static boolean convertTo3AC(AbstractSyntaxTree tree, String filePath)
    {
        try
        {
            ASTReader reader = new ASTReader(tree, filePath);
            reader.readTree();
        }
        catch(Exception E)
        {
            return(false);
        }
        return(false);

    }
}