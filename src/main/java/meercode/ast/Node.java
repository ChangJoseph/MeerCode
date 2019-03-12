package meercode.ast;
public class Node
    {
<<<<<<< HEAD
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
=======
        public Node mLeft, mRight, mMiddle, mParent;
        public String mData;

        public Node()
        {
            this(null, null, null, null, null);
        }
        public Node(String pData) {
            this(pData, null, null, null, null);
        }
        public Node(Node pParent)
        {
            this(null, null, null, null, pParent);
>>>>>>> master
        }
        public Node(String pData, Node pLeft, Node pMiddle, Node pRight)
        {
            this.mData = pData;
            this.mLeft = pLeft;
            this.mRight = pRight;
            this.mMiddle = pMiddle;
            this.mParent = null;
        }
        
        /**
         * Main constructor
         */
        public Node(String pData, Node pLeft, Node pMiddle, Node pRight, Node pParent)
        {
            this.mData = pData;
            this.mLeft = pLeft;
            this.mRight = pRight;
            this.mMiddle = pMiddle;
            this.mParent = pParent;
        }



        
        

    }