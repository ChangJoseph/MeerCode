package meercode;
import java.util.*;
public final class Compiler
{
    private static HashMap<String, String> tokenMap = new HashMap<>();
    private static String input = "C:/Users/robin/Desktop/GitStuff/MeerCode/src/main/java/meercode/TestOutput.txt";
    private static Scanner scan = new Scanner(input);
    private static String curLine = scan.nextLine();
    private static String curJumpFlag = "";
    private static ArrayList<String> comparatorList = new ArrayList<>();
    private static ArrayList<String> functionList = new ArrayList<>();
  

    private static void compile()
    {
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
            else if(curLine.substring(0,4).equals("goto"))
            {
                curJumpFlag = "JUMP" + curLine.charAt(curLine.length()-1);
                    scan = new Scanner(input);
                    while(!scan.nextLine().equals(curJumpFlag));
                    curLine = scan.nextLine();
            }
            else if(!curLine.contains("JUMP"))
            {
                String tokenKey = curLine.split(" ")[0];
                String toEvaluate = curLine.split(" ", 3)[2];
                tokenMap.put(tokenKey,evaluateLine(toEvaluate) );
            }
            
            
        }   
    }
    private static void findConstants(String line)
    {
        String term1 = line.split(" ")[0];
        if(term1.contains("~k!"))
        {
            tokenMap.put(term1, term1.substring(3));
        }
        if(line.split(" ").length > 1)
        {
            String term2 = line.split(" ")[2];
            tokenMap.put(term2, term2.substring(3));
        }
        
    }
    private static String evaluateLine(String line)
    {
        Boolean isComparator = false;
        for(String str : comparatorList)
        {
            if(line.contains(str))
            {
                isComparator = true;
                break;
            }
        }
        if(isComparator)
        {
            findConstants(line);
            String comparator = line.split(" ")[1];
            String term1 = line.split(" ")[0];
            String term2 = line.split(" ")[2];
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
            findConstants(line);
            String function = line.split(" ")[1];
            String term1 = line.split(" ")[0];
            String term2 = line.split(" ")[2];
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
                return(Double.toString(Math.pow(Double.parseDouble(tokenMap.get(term1)), Integer.parseInt(tokenMap.get(term2)))).replace("\\..*",""));
            }
        }
        
    }

}