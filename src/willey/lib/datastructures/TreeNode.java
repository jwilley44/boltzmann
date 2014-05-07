package willey.lib.datastructures;


public interface TreeNode<E>
{
	public E getData();
	
	public void addChild(TreeNode<E> pChild);
	
	public void removeChild(TreeNode<E> pChild);
	
	public Iterable<? extends TreeNode<E>> getChildren();
}
