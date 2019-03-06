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
        flagCount++;
        int curFlagCount = flagCount;
            
             if( node.data.equals("if"))
            {
                String jumpID = "JUMP" + curFlagCount;
                String endID = "END" + curFlagCount;
               
                write( "if " + genFunction(node.left) + " goto " + jumpID);
                newLine();
                genFunction(node.right);
                write("goto " + endID);
                
                newLine();
                write(jumpID);
                newLine();
                genFunction(node.middle);
                write(endID);
                newLine();
                return("t"+ curFlagCount);


            }
            else if(node.data.equals("NOP"))
            {
                genFunction(node.left);
                genFunction(node.right);
                return("t" + curFlagCount);
            }
            else if(node.data.equals("return"))
            {
                write("ret " + genFunction(node.middle));
                newLine();
                return("t" + curFlagCount);
            }
            else if(node.data.equals("print"))
            {
                write("print " + genFunction(node.middle));
                newLine();
                return("t" + curFlagCount);
            }
            else if(node.data.equals("while"))
            {
                String jumpID = "JUMP" + curFlagCount;
                String endID = "END" + curFlagCount;
                write(jumpID);
                newLine();
                write("t" + curFlagCount + " = " + genFunction(node.left) + " == false");
                newLine();
                write("if " + "t" + curFlagCount + " goto " + endID);
                newLine();
                genFunction(node.right);
                write("goto " + jumpID);
                newLine();
                write(endID);
                newLine();
                return("t" + curFlagCount);

            }
            else if(!isFunction(node.data))
            {
                System.out.println("No function found on: " + node.data);
                if(node.flag == 'v')
                {
                return(node.data);
                }
                else
                {
                    return("~k!" + node.data);
                }
            }
            else 
            {
                
                System.out.println("Function found on: " + node.data);
                write( "t" + curFlagCount + " = " + genFunction(node.left) + " " + node.data + " " + genFunction(node.right) + "\n");
               System.out.print(outputString);
               
               
               return("t"+ curFlagCount);
            }
            
    }
    private boolean isFunction(String data)
    {
        return(functionList.contains(data));
    }
    private void newLine()
    {
        write("\n");
    }
    public void testWrite() throws IOException
    {
        System.out.println("Attempting to write");
        write("This is a test");
    }
}