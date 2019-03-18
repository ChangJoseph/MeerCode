package meercode.ast;
import java.io.*;
import java.util.*;
import java.nio.file.*;
public final class ASTReader 
{
    private AbstractSyntaxTree tree;
    
    String outputString;
    private int flagCount;
    private ArrayList<String> functionList = new ArrayList<String>();
    private String outputFile;
    private Queue<String> idQueue;
    public ASTReader(AbstractSyntaxTree tree, String outputFile) throws IOException
    {
        this.tree = tree;
        this.outputFile = outputFile;
       
        
        flagCount = 0;
        functionList.add("+");
        functionList.add("-");
        functionList.add("*");
        functionList.add("/");
        functionList.add("^");
        functionList.add("not");
        functionList.add("or");
        functionList.add("and");
        functionList.add("is");
        functionList.add("<");
        functionList.add(">");
        functionList.add("=");
        functionList.add("=<");
        functionList.add(">=");
        functionList.add("!=");
        functionList.add("==");
        functionList.add("%");

        try
        {
            Files.write(Paths.get(outputFile), "3AC Code \n".getBytes());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        outputString = "";
        idQueue = new LinkedList<String>();
    }
    private  void write(String mData) {
        try { 
  
            // Open given file in append mode. 
            BufferedWriter out = new BufferedWriter( 
                   new FileWriter(outputFile, true)); 
            out.write(mData); 
            out.close(); 
        } 
        catch (IOException e) { 
            System.out.println("exception occoured" + e); 
        } 
    }
   
    private void readTree() 
    {
        Node head = tree.getHead();
       genFunction(head);
        write(outputString);
        System.out.println(outputString);
    }
    
    private String genFunction(Node node) 
    {       
        flagCount++;
        int curFlagCount = flagCount;
            
             if( node.mData.equals("if"))
            {
                String jumpID = "JUMP" + curFlagCount;
                String endID = "END" + curFlagCount;
               
                write( "if " + genFunction(node.mLeft) + " goto " + jumpID);
                newLine();
                genFunction(node.mRight);
                write("goto " + endID);
                
                newLine();
                write(jumpID);
                newLine();
                genFunction(node.mMiddle);
                write(endID);
                newLine();
                return("t"+ curFlagCount);


            }
            else if(node.mData.equals("NOP"))
            {
                genFunction(node.mLeft);
                genFunction(node.mRight);
                return("t" + curFlagCount);
            }
            else if(node.mData.equals("return"))
            {
                write("ret " + genFunction(node.mMiddle));
                newLine();
                return("t" + curFlagCount);
            }
            else if(node.mData.equals("print"))
            {
                write("print " + genFunction(node.mMiddle));
                newLine();
                return("t" + curFlagCount);
            }
            else if(node.mData.equals("while"))
            {
                String jumpID = "JUMP" + curFlagCount;
                String endID = "END" + curFlagCount;
                write(jumpID);
                newLine();
                write("t" + curFlagCount + " = " + genFunction(node.mLeft) + " == false");
                newLine();
                write("if " + "t" + curFlagCount + " goto " + endID);
                newLine();
                genFunction(node.mRight);
                write("goto " + jumpID);
                newLine();
                write(endID);
                newLine();
                return("t" + curFlagCount);

            }
            else if(node.mData.equals("="))
            {
                write(node.mLeft.mData + " = " + genFunction(node.mRight));
                newLine();
                return("t" + curFlagCount);
            }
            else if(!isFunction(node.mData))
            {
                System.out.println("No function found on: " + node.mData);
                if(node.mFlag == 'v')
                {
                return(node.mData);
                }
                else
                {
                    return("~k!" + node.mData);
                }
            }
            
            else 
            {
                
                System.out.println("Function found on: " + node.mData);
                write( "t" + curFlagCount + " = " + genFunction(node.mLeft) + " " + node.mData + " " + genFunction(node.mRight) + "\n");
               System.out.print(outputString);
               
               
               return("t"+ curFlagCount);
            }
            
    }
    private boolean isFunction(String mData)
    {
        return(functionList.contains(mData));
    }
    private void newLine()
    {
        write("\n");
    }
    private void testWrite() throws IOException
    {
        System.out.println("Attempting to write");
        write("This is a test");
    }
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