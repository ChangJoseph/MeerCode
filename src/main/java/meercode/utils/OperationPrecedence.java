package meercode.utils;
import java.util.List;
import java.util.ArrayList;

public class OperationPrecedence {
    // private List<String> mText;
    public static List<String> getPrecedence(List<String> pText)
    {
        List<String> newList = new ArrayList<>();
        for (int i = 0; i < pText.size(); i++)
        {
            String token = pText.get(i);
            if (token.equals(")"))
            {
                int a = 1;
                a++;
            }
            else if (token.equals("("))
            {
                List<String> parenList = pText.subList(i+1, pText.indexOf(")"));
                for (int j = i; j < pText.indexOf(")"); j++ )
                {
                    pText.remove(j);
                }
                newList.addAll(parenList);
            }
            else if (token.equals("^"))
            {
                
            }
        }
        return newList;
    }

}