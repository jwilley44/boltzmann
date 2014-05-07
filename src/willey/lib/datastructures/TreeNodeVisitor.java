package willey.lib.datastructures;

public abstract class TreeNodeVisitor<E extends TreeNode<?>>
{
	protected abstract void visit(E pNode);
	
	protected abstract E nextNode();
	
	protected abstract boolean found(E pNode);
	
	public E find(E pRoot)
	{
		E vNode = pRoot;
		visit(vNode);
		boolean vStop = false;
		while(vNode != null && !vStop)
		{
			vNode = nextNode();
			visit(vNode);
			vStop = found(vNode);
		}
		return vNode;
	}
}
