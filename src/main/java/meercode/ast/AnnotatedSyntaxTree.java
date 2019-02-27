package meercode.ast;
public class AnnotatedSyntaxTree
{
    
    private Node head;

    public AnnotatedSyntaxTree(Node head)
    {
        this.head = head;
    }
    public Node getHead()
    {
        return(head);
    }
}