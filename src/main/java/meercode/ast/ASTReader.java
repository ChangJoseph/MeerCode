package meercode.ast;
import java.io.*;
import java.util.*;
import java.nio.file.*;
public class ASTReader 
{
    private AbstractSyntaxTree tree;
    
    String outputString;
    private int flagCount;
    private ArrayList<String> functionList = new ArrayList<String>();
    private String outputFile;
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
        try
        {
            Files.write(Paths.get(outputFile), "3AC Code \n".getBytes());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        outputString = "";
    }
    private  void write(String data) {
        try { 
  
            // Open given file in append mode. 
            BufferedWriter out = new BufferedWriter( 
                   new FileWriter(outputFile, true)); 
            out.write(data); 
            out.close(); 
        } 
        catch (IOException e) { 
            System.out.println("exception occoured" + e); 
        } 
    }
   
    public void readTree() 
    {
        Node head = tree.getHead();
       genFunction(head);
        write(outputString);
        System.out.println(outputString);
    }
    
    private String genFunction(Node node) 
    {       
            if(!isFunction(node.mData))
            {
                return(node.mData);
            }
            else 
            {
                
                flagCount++;
                
                write( "t" + flagCount + " = " + genFunction(node.mLeft) + " " + node.mData + " " + genFunction(node.mRight) + "\n");
               System.out.print(outputString);
               
               return("t"+ flagCount);
            }
            
    }
    private boolean isFunction(String data)
    {
        return(functionList.contains(data));
    }
    public void testWrite() throws IOException
    {
        System.out.println("Attempting to write");
        write("This is a test");
    }
}