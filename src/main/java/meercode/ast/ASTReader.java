package meercode.ast;
import java.io.*;
import java.util.*;
import java.nio.file.*;
public class ASTReader 
{
    private AbstractSyntaxTree tree;
    
    
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
    }
    private  void write(String data) {
        try {
            Files.write(Paths.get(outputFile), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    public void readTree() 
    {
        Node head = tree.getHead();
       genFunction(head);

    }
    
    private String genFunction(Node node) 
    {       
            if(!isFunction(node.data))
            {
                return(node.data);
            }
            else 
            {
                
               write( genFunction(node.left) + " " + node.data + " " + genFunction(node.right) + "\n");
               
               flagCount++;
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