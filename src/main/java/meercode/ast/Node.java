package meercode.ast;
public class Node
    {
        public Node mLeft, mRight, mMiddle;
        private Node mParent;
        public String mData;
        public char mFlag;

        public Node()
        {
            this(null, '~', null, null, null);
        }
        public Node(String pData) {
            this(pData, '~', null, null, null);
        }
        public Node(Node pParent)
        {
            this(null, '~', null, null, pParent);
        }
        public Node(String pData, char pFlag) {
            
            this(pData, pFlag, null, null, null, null);
        }
        public Node(String pData, char pFlag, Node pLeft, Node pMiddle, Node pRight)
        {
            this(pData, pFlag, pLeft, pMiddle, pRight, null);
        }
        
        /**
         * Main constructor
         */
        public Node(String pData, char pFlag, Node pLeft, Node pMiddle, Node pRight, Node pParent)
        {
            
            this.mData = pData;
            this.mLeft = pLeft;
            this.mRight = pRight;
            this.mMiddle = pMiddle;
            this.mParent = pParent;
            this.mFlag = pFlag;
        }



        public void setParent(Node pParent) {
            this.mParent = pParent;
        }
        public Node getParent() {
            return mParent;
        }
        

    }