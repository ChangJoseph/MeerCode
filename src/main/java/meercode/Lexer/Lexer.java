package meercode.lexer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
            
            List<String> lines = new ArrayList<>(Arrays.asList(mInCode.split("\n")));
            for (int i = 0; i < lines.size(); i++)
            {
                if (lines.get(i).contains("#"))
                {
                    lines.set(i, lines.get(i).replaceAll("#.*", ""));
                }

            }
            //remove all occurences of empty strings with super fancy code
            lines.removeAll(Collections.singleton(""));
            List<List<String>> wordsByLine = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++)
            {
                try
                {
                    wordsByLine.add(new ArrayList<String>(Arrays.asList(lines.get(i).split("\\s+"))));
                }
                catch (Exception e)
                {
                    System.out.println("bad add");
                }
            }
            for (int a = 0; a < wordsByLine.size(); a++)
            {
                //create a temp list to search through for ["items, like, this"]
                List<String> line = wordsByLine.get(a);
                for (int b = 0; b < line.size(); b++)
                {
                    String word = line.get(b);
                    if (word.charAt(0) == '\"')
                    {
                        //make a counter to get sequential items from the list
                        int c = 1;
                        while (b + c < line.size() && word.charAt(word.length() - 1) != '\"')
                        {
                            word = word + " " + line.get(b + c);
                            line.set(b, word);
                            c++;
                        }
                        // add c to counter b to prevent redundant looping
                        b += c;
                    }
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