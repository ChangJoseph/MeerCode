package meercode;
import java.util.*;

import meercode.utils.ReservedWords;

import java.io.File;
public final class Compiler
{   //Map that stores values for all token ID's
    private static HashMap<String, String> tokenMap = new HashMap<>();
    //File path where the 3AC is stored
    private static String input = "C:/Users/robin/Desktop/GitStuff/MeerCode/src/main/java/meercode/TestOutput.txt";
    //Scanner used to read from file
    private static Scanner scan;
    private static String curLine;
    //The flag to jump to when a goto is reached
    private static String curJumpFlag = "";
    private static List<String> comparatorList = new ArrayList<>();
    private static List<String> functionList = new ArrayList<>();
  
    //Main compilation method
    public static void compile(String mInput)
    {
        //sets up the Scanner and the file to scan from
        input = mInput;
        File file = new File(input);
        try
        {
        scan = new Scanner(file);
        }
        catch (Exception e)
        {
            //System.out.println("File not found exception");
        }
        
        //System.out.println("Making Function and Comparator Lists");
        //Creates lists of reserved words from comparators and operators
        comparatorList = ReservedWords.get3ACComparators();
        functionList = ReservedWords.get3ACFunctions();
        //System.out.println("Beginning Scan");
        //System.out.println(scan.hasNextLine());
        //Adds the boolean constants as known values
        tokenMap.put("~k!false", "false");
        tokenMap.put("~k!true", "true");
        //Master loop that goes through the 3AC until it hits the end
        while(scan.hasNextLine())
        {
            //System.out.println(tokenMap.get("x"));
            
            curLine = scan.nextLine();//Grabs the next line
            //System.out.println("Current Line is " + curLine);
            if( curLine.substring(0,2).equals("if"))// if the line is an if statement
            {
                //System.out.println("Line evaluated for if statement");
                if(tokenMap.get(curLine.split(" ")[1]).equals("true")) // determine if the statement evaluates to true
                {                                                        //using the value in the token
                
                    //System.out.println("If statement evaluated to True with: " + curLine.split(" ")[1]);
                    //System.out.println("Token Correspondance is: " + tokenMap.get(curLine.split(" ")[1]));
                    curJumpFlag = curLine.split("goto ")[1]; // Set the jump flag to identifier after the goto
                    try{
                    scan.close();
                    scan = new Scanner(new File(input)); // create a fresh scanner in order to reset position
                    }
                    catch(Exception E)
                    {
                        //System.out.println("No File Found");
                    }
                    while(!scan.nextLine().equals(curJumpFlag)); //iterate over the code until the jump flag is found
                    
                

                
                }
                //System.out.println("If Statement evaluated to false with: " + curLine.split(" ")[1]);
                //System.out.println("Token evaluates to: " + tokenMap.get(curLine.split(" ")[1]));
                //If it's false do nothing
            }
            else if(curLine.contains("print")) //If the function is a print statement
            {
                //System.out.println("Line evaluated as print statement");
                //Change later when Computer Class is functional
                String tempLine = curLine.substring(6); //Grab the thing to be printed
                if(tempLine.substring(0,3).equals("~k!")) //If it is a constant
                {
                    System.out.println(tempLine.substring(3)); //Strip the constant flag and print it
                }
                else 
                {
                    System.out.println(tokenMap.get(tempLine)); // else print the tokens value
                }
            }
            else if(curLine.substring(0,4).equals("goto")) // if the line is a goto
            {
                //System.out.println("Line evaluated for goto");
                curJumpFlag = curLine.split(" ")[1]; // set the current jump flag to the goto line
                //System.out.println("Curren Jump Flag is: " + curJumpFlag);
                    try
                    {
                        scan.close();
                        scan = new Scanner(new File(input)); //create a new scanner to reset the pointer
                    }
                    catch(Exception e)
                    {
                        //System.out.println("File not Found");
                    }
                    while(!scan.nextLine().equals(curJumpFlag)); //Iterate through the code until jump flag is found
                    
            }
            else if(!curLine.contains("JUMP") && !curLine.contains("END"))// if it isn't a label evaluate line as a base case
            {
                //System.out.println("Line Evaluated for normal function");
                String tokenKey = curLine.split(" ")[0]; //The token key is the key for the Map
                ////System.out.println(curLine.split(" ").length);
                String toEvaluate = curLine.split(" ", 3)[2];// toEvaluate is the function that needs to be applied
                //System.out.println("Token Key is " +tokenKey);
                //System.out.println("To Evaluate is " + toEvaluate);
                //System.out.println("Token Evaluates to " + evaluateLine(toEvaluate));
                tokenMap.put(tokenKey,evaluateLine(toEvaluate) ); //puts the evaluted portion in the map with the tokenKey
            }
            
            
        } 
        //System.out.println("Done");  
    }
    //Picks out the constants of a certain line and stores them in the tokenMap
    private static void findConstants(String line)
    {
        //System.out.println("Finding Constants");
        String term1 = line.split(" ")[0]; //Finds the first term (potential constant)
        if(term1.contains("~k!")) //if it contains the constant flag
        {
            //System.out.println("Found Constant " + term1);
            tokenMap.put(term1, term1.substring(3)); // add term into tokenMap with flag stripped
        }
        if(line.split(" ").length > 1)// if the line contains more than 1 term
        {
            String term2 = line.split(" ")[2]; // find the second term ( potential constant)
            if(term2.contains("~k!")) // if it contains the constant flag
            {
                //System.out.println("Found Constant " + term2);
                tokenMap.put(term2, term2.substring(3)); //add to the map with tokenMap stripped
            }
        }
        
    }
    //Function evaluates a line for meaning
    private static String evaluateLine(String line)
    {
        //System.out.println("Evaluating Line");
        Boolean isComparator = false;//Flag to determine if something is a comparator
        for(String str : comparatorList)// Checks to see if it is contained in the compartor list
        {
            ////System.out.println(str);
            if(line.contains(str))
            {
                isComparator = true;
                break;
            }
        }
        if(isComparator) // if it is a comparator
        {
            //System.out.println("Found a comparator");
            findConstants(line);// finds add identifies all constants
            String comparator = line.split(" ")[1]; //stores the comparator
            String term1 = line.split(" ")[0]; // stores each of the terms
            String term2 = line.split(" ")[2];
            //System.out.println("Term1 is " + tokenMap.get(term1));
            //System.out.println("Term2 is " + tokenMap.get(term2));
            /*
                This next switch case block first determiens the comparator to be evaluated 
                then it applies the proper function to it and stores the resulting boolean calculation
                as a string before returning it to be added to the tokenMap
            */
            switch(comparator) 
            {
                case "==":
                return(Boolean.toString(tokenMap.get(term1).equals(tokenMap.get(term2))));
                case "!=":
                return(Boolean.toString(!tokenMap.get(term1).equals(tokenMap.get(term2))));
                case ">":
                return(Boolean.toString(Integer.parseInt(tokenMap.get(term1)) > Integer.parseInt(tokenMap.get(term2))));
                case ">=":
                return(Boolean.toString(Integer.parseInt(tokenMap.get(term1)) >= Integer.parseInt(tokenMap.get(term2))));
                case "<=":
                return(Boolean.toString(Integer.parseInt(tokenMap.get(term1)) <= Integer.parseInt(tokenMap.get(term2))));
                case "<":
                return(Boolean.toString(Integer.parseInt(tokenMap.get(term1)) < Integer.parseInt(tokenMap.get(term2))));
                case "or":
                return(Boolean.toString(Boolean.parseBoolean(tokenMap.get(term1)) || Boolean.parseBoolean(tokenMap.get(term2))));
                case "and":
                return(Boolean.toString(Boolean.parseBoolean(tokenMap.get(term1)) && Boolean.parseBoolean(tokenMap.get(term2))));
            }
        }
        //The whole process is repeated for functions
        Boolean isFunction = false;
        for(String str : functionList)
        {
            if(line.contains(str))
            {
                isFunction = true;
                break;
            }
        }
        if(isFunction)
        {
            //System.out.println("Found a Function");
            findConstants(line);
            String function = line.split(" ")[1];
            String term1 = line.split(" ")[0];
            String term2 = line.split(" ")[2];
            //System.out.println("Term1 is " + tokenMap.get(term1));
            //System.out.println("Term2 is " + tokenMap.get(term2));
            switch(function)
            {
                case "*":
                return(Integer.toString(Integer.parseInt(tokenMap.get(term1)) * Integer.parseInt(tokenMap.get(term2))));
                case "+":
                return(Integer.toString(Integer.parseInt(tokenMap.get(term1)) + Integer.parseInt(tokenMap.get(term2))));
                case "/":
                return(Integer.toString(Integer.parseInt(tokenMap.get(term1)) / Integer.parseInt(tokenMap.get(term2))));
                case "%":
                return(Integer.toString(Integer.parseInt(tokenMap.get(term1)) % Integer.parseInt(tokenMap.get(term2))));
                case "-":
                return(Integer.toString(Integer.parseInt(tokenMap.get(term1)) - Integer.parseInt(tokenMap.get(term2))));
                case "^":
                return(Integer.toString((int)(Math.pow(Double.parseDouble(tokenMap.get(term1)), Integer.parseInt(tokenMap.get(term2))))));
            }
        }
        if(line.charAt(0) == '!') // In the case of a ! it simply inverts the boolean and stores it again
        {
            return(Boolean.toString(!Boolean.parseBoolean(tokenMap.get(line.substring(1)))));
        }
        
        if(line.contains("~k!")) //If the line is a constants strips the flag off of it
        {
            return(line.substring(3));
        }
        else
        {
            return(tokenMap.get(line)); // if nothing else needs to be applied returns the pair to the line
        }
        
    }

}