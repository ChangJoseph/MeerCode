package meercode.lexer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Lexer
{
    private static String mInCode;
    private Lexer()
    {
    }


    public static List<List<String>> getTokenList(String filename)
    {
        try 
        {
            try
            {
                mInCode = getFileContents(filename);
            }
            catch (Exception e)
            {
                System.out.println("bad filename");
            }
            mInCode = mInCode.replace("(", " ( ");
            mInCode = mInCode.replace(")", " ) ");
            mInCode = mInCode.replaceAll("[\r\n]+", "\n");
            mInCode = mInCode.replaceAll("#.*", "");
            String [] lines = mInCode.split("\n");

            List<List<String>> wordsByLine = new ArrayList<>();
            for (int i = 0; i < lines.length; i++)
            {
                try
                {
                    wordsByLine.add(new ArrayList<String>(Arrays.asList(lines[i].split("\\s+"))));
                }
                catch (Exception e)
                {
                    System.out.println("bad add");
                }
            }
            return wordsByLine;
        } 
        catch (Exception e)
        {
            System.out.println("?????");
            return null;
        }
    }

    private static String getFileContents( String filename ) throws IOException
    {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}