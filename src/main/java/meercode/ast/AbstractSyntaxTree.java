package meercode.ast;
public class AbstractSyntaxTree
{
    
    private Node head;

    public AbstractSyntaxTree(Node head)
    {
        this.head = head;
    }
    public Node getHead()
    {
        return(head);
    }
}