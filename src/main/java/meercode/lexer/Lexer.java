package meercode.lexer;

// import java.io.File;
// import java.io.FileWriter;
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

    public static void main(String[] args) 
    {
        // System.out.println(getTokenList("C:\\Users\\franc\\OneDrive\\Documents\\excode.txt"));
    }

    /**
     * this method takes in a file, splits the text by line and then by word, then returns the 2D list
     * @param filename the file that the lexer takes text from
     * @return the list of lists of text by line
     */
    public static List<List<String>> getTokenList(String filename)
    {
        try 
        {
            try
            {
                //obtain text from a file
                mInCode = getFileContents(filename);
                System.out.println(mInCode);
            }
            catch (Exception e)
            {
                System.out.println("bad filename");
            }
            //these statements space out parentheses to make it easier to parse
            mInCode = mInCode.replace("(", " ( ");
            mInCode = mInCode.replace(")", " ) ");
            //this gets rid of multiple consecutive new lines with no text 
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
                        // add c to counter b to prevent redundant looping/exceptions
                        b += c + 1;
                        //line.remove(line.size() - 1);
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

    /**
     * this method takes a file and extracts the text
     * @param filename the file containing the needed contents
     * @return a string containing the file text
     * @throws IOException if the filename is invalid
     */
    private static String getFileContents( String filename ) throws IOException
    {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}