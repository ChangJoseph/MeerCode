package meercode;
import java.util.*;
import java.io.File;
public final class Compiler
{
    private static HashMap<String, String> tokenMap = new HashMap<>();
    private static String input = "C:/Users/robin/Desktop/GitStuff/MeerCode/src/main/java/meercode/TestOutput.txt";
    private static Scanner scan = new Scanner(input);
    private static String curLine = scan.nextLine();
    private static String curJumpFlag = "";
    private static ArrayList<String> comparatorList = new ArrayList<>();
    private static ArrayList<String> functionList = new ArrayList<>();
  

    public static void compile()
    {
        input = "C:/Users/robin/Desktop/GitStuff/MeerCode/src/main/java/meercode/TestOutput.txt";
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
        comparatorList.add("<");
        comparatorList.add(">");
        comparatorList.add("<=");
        comparatorList.add(">=");
        comparatorList.add("!=");
        comparatorList.add("==");
        comparatorList.add("or");
        comparatorList.add("and");
        functionList.add("+");
        functionList.add("-");
        functionList.add("*");
        functionList.add("/");
        functionList.add("^");
        functionList.add("%");
        //System.out.println("Beginning Scan");
        //System.out.println(scan.hasNextLine());
        tokenMap.put("~k!false", "false");
        tokenMap.put("~k!true", "true");
        while(scan.hasNextLine())
        {
            //System.out.println(tokenMap.get("x"));
            curLine = scan.nextLine();
            //System.out.println("Current Line is " + curLine);
            if( curLine.substring(0,2).equals("if"))
            {
                //System.out.println("Line evaluated for if statement");
                if(tokenMap.get(curLine.split(" ")[1]).equals("true"))
                {
                    //System.out.println("If statement evaluated to True with: " + curLine.split(" ")[1]);
                    //System.out.println("Token Correspondance is: " + tokenMap.get(curLine.split(" ")[1]));
                    curJumpFlag = curLine.split("goto ")[1];
                    try{
                    scan = new Scanner(new File(input));
                    }
                    catch(Exception E)
                    {
                        //System.out.println("No File Found");
                    }
                    while(!scan.nextLine().equals(curJumpFlag));
                    
                

                
                }
                //System.out.println("If Statement evaluated to false with: " + curLine.split(" ")[1]);
                //System.out.println("Token evaluates to: " + tokenMap.get(curLine.split(" ")[1]));
            }
            else if(curLine.contains("print"))
            {
                //System.out.println("Line evaluated as print statement");
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
            else if(curLine.substring(0,4).equals("goto"))
            {
                //System.out.println("Line evaluated for goto");
                curJumpFlag = curLine.split(" ")[1];
                //System.out.println("Curren Jump Flag is: " + curJumpFlag);
                    try
                    {
                        scan = new Scanner(new File(input));
                    }
                    catch(Exception e)
                    {
                        //System.out.println("File not Found");
                    }
                    while(!scan.nextLine().equals(curJumpFlag));
                    
            }
            else if(!curLine.contains("JUMP") && !curLine.contains("END"))
            {
                //System.out.println("Line Evaluated for normal function");
                String tokenKey = curLine.split(" ")[0];
                ////System.out.println(curLine.split(" ").length);
                String toEvaluate = curLine.split(" ", 3)[2];
                //System.out.println("Token Key is " +tokenKey);
                //System.out.println("To Evaluate is " + toEvaluate);
                //System.out.println("Token Evaluates to " + evaluateLine(toEvaluate));
                tokenMap.put(tokenKey,evaluateLine(toEvaluate) );
            }
            
            
        } 
        //System.out.println("Done");  
    }
    private static void findConstants(String line)
    {
        //System.out.println("Finding Constants");
        String term1 = line.split(" ")[0];
        if(term1.contains("~k!"))
        {
            //System.out.println("Found Constant " + term1);
            tokenMap.put(term1, term1.substring(3));
        }
        if(line.split(" ").length > 1)
        {
            String term2 = line.split(" ")[2];
            if(term2.contains("~k!"))
            {
                //System.out.println("Found Constant " + term2);
                tokenMap.put(term2, term2.substring(3));
            }
        }
        
    }
    private static String evaluateLine(String line)
    {
        //System.out.println("Evaluating Line");
        Boolean isComparator = false;
        for(String str : comparatorList)
        {
            ////System.out.println(str);
            if(line.contains(str))
            {
                isComparator = true;
                break;
            }
        }
        if(isComparator)
        {
            //System.out.println("Found a comparator");
            findConstants(line);
            String comparator = line.split(" ")[1];
            String term1 = line.split(" ")[0];
            String term2 = line.split(" ")[2];
            //System.out.println("Term1 is " + tokenMap.get(term1));
            //System.out.println("Term2 is " + tokenMap.get(term2));
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
        if(line.charAt(0) == '!')
        {
            return(Boolean.toString(!Boolean.parseBoolean(tokenMap.get(line.substring(1)))));
        }
        
        if(line.contains("~k!"))
        {
            return(line.substring(3));
        }
        else
        {
            return(tokenMap.get(line));
        }
        
    }

}