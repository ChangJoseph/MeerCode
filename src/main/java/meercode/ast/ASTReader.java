package meercode.ast;
import java.io.*;
import java.util.*;

public class ASTReader 
{
    private AnnotatedSyntaxTree tree;
    private BufferedWriter writer;
    private int count;
    private int flagCount;
    private ArrayList<String> functionList = new ArrayList<String>();

    public ASTReader(AnnotatedSyntaxTree tree, String outputFile) throws IOException
    {
        this.tree = tree;
        writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write("3AC \n");
        count = 0;
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
    public void write(String text) throws IOException
    {
        writer.append(text);
    }
    public void newLine() throws IOException
    {
        writer.newLine();
    }
    public void readTree() throws IOException
    {
        Node head = tree.getHead();
       genFunction(head);

    }
    
    private String genFunction(Node node) throws IOException
    {       
            if(!isFunction(node.data))
            {
                return(node.data);
            }
            else 
            {
                
               System.out.println( genFunction(node.left) + " " + node.data + " " + genFunction(node.right));
               count++;
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
        writer.write("This is a test");
    }
}