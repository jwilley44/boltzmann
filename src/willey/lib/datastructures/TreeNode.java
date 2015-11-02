package willey.lib.datastructures;

import java.util.stream.Stream;


public interface TreeNode<E>
{
	public E getData();
	
	public void addChild(TreeNode<E> pChild);
	
	public void removeChild(TreeNode<E> pChild);
	
	public Stream<TreeNode<E>> getChildren();
}
