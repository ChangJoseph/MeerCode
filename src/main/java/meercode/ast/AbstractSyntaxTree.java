package meercode.ast;
public class AbstractSyntaxTree
{
    
    private Node mHead;

    public AbstractSyntaxTree(Node pHead)
    {
        
        this.mHead = pHead;
        
    }
    public Node getHead()
    {
        
        return(mHead);
    }

    @Override
    public String toString() {
        Node head = mHead;
        return toString(head);
    }
    /**
     * Recursive method to show the tree in a single line
     * @param node Current node to process
     * @return The line representing the tree
     */
    private String toString(Node node) {
        String stringTree = "";
        if (node.mData != null) {
            stringTree += node.mData + " ";
        }
        else {
            stringTree += "~ ";
        }
        if (node.mLeft != null) {
            stringTree += toString(node.mLeft);
        }
        else {
            stringTree += "/ "; // left branch null
        }
        if (node.mRight != null) {
            stringTree += toString(node.mRight);
        }
        else {
            stringTree += "\\ "; // right branch null
        }
        return stringTree;
    }
    
}