package willey.lib.datastructures;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Tree<P>
{
	abstract protected TreeNode<P> getRoot();

	public String toDot()
	{
		StringBuilder vBuilder = new StringBuilder("digraph G{");
		vBuilder.append(traverse(getRoot()).map(pNode -> toDot(pNode)).collect(
				Collectors.joining("\n")));
		vBuilder.append("}");
		return vBuilder.toString();
	}

	public Stream<TreeNode<P>> traverse(TreeNode<P> pNode)
	{
		return pNode.getChildren().flatMap(pChild -> traverse(pChild));
	}

	private String toDot(TreeNode<P> pNode)
	{
		return pNode.getChildren()
				.map(pChild -> pNode.toString() + "->" + pChild.toString())
				.collect(Collectors.joining("\n"));
	}
}
