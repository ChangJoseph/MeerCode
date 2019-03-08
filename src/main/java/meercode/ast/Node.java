package meercode.ast;
public class Node
    {
        public Node mLeft, mRight, mMiddle, mParent;
        public String mData;

        public Node()
        {
            this(null, null, null, null, null);
        }
        public Node(Node pParent)
        {
            this(null, null, null, null, pParent);
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