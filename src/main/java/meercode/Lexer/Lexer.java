//package meercode;
import java.util.*;
import java.io.*;
import java.nio.File;

public class Lexer()
{
    private String mInCode;
    private String mFile = "";
    public Lexer()
    {
        try 
        {
            inCode = getFileContents(file);
            String [] lines = mInCode.split("\\\\n+");
            List<ArrayList<String>> words = new ArrayList<>();;
            for (int i = 0; i < lines.length; i++)
            {
                words.add(new List<arrayList<String>(Arrays.asList(lines[i].split("\\s+"))));
            }
            
        } 
        catch (Exception e)
        {
            System.out.println("Your filename sucks buddy");
        }
    }

    public static void main(String [] args)
    {
        new Lexer();
    }

    private static void writeFileContents( String filename, String data) throws IOException
    {
        FileWriter fw = new FileWriter(new File(filename));
        fw.write(data);
        fw.close();
    }

    private static String getFileContents( String filename ) throws IOException
    {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
    
    public boolean isSymbol(String s)
    {
        return s.matches(symbolRegex);
    }

    public boolean isDigit(String s)
    {
        return s.matches(numRegex);
    }
}