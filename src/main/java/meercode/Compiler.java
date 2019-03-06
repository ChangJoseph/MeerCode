package meercode;
import java.util.*;
public final class Compiler
{
    private static HashMap<String, String> tokenMap = new HashMap<>();
    private static String input = "C:/Users/robin/Desktop/GitStuff/MeerCode/src/main/java/meercode/TestOutput.txt";
    private static Scanner scan = new Scanner(input);
    private static String curLine = scan.nextLine();
    private static String curJumpFlag = "";

    private static void compile()
    {
        while(scan.hasNextLine())
        {
            curLine = scan.nextLine();
            if( curLine.substring(0,2).equals("if"))
            {
                if(tokenMap.get(curLine.split(" ")[1]).equals("true"))
                {
                    curJumpFlag = "JUMP" + curLine.charAt(curLine.length()-1);
                    scan = new Scanner(input);
                    while(!scan.nextLine().equals(curJumpFlag));
                    curLine = scan.nextLine();
                

                
                }
            }
            else if(curLine.contains("print"))
            {
                //Change later when Computer Class is functional
                String tempLine = curLine.substring(6);
                if(tempLine.substring(0,3).equals("~k!"))
                {
                    System.out.println(tempLine.substring(3));
                }
                else 
                {
                    System.out.println(tempLine);
                }
            }
            
        }   
    }

}