package meercode.ast;
public class Node
    {
        public Node left, right, middle;
        public String data;
        public char flag;

        public Node()
        {
            this(null, 'n');
        }
        public Node(String data, char flag)
        {
            this.data = data;
            left = null;
            right = null;
            middle = null;
            this.flag = flag;
        }
        
        public Node(String data, Node left, Node right)
        {
            this.data = data;
            this.left = left;
            this.right = right;
            middle = null;
        }
        public Node(String data, Node left, Node middle, Node right)
        {
            this.data = data;
            this.left = left;
            this.right = right;
            this.middle = middle;
        }



        
        

    }