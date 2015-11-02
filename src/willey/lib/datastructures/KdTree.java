package willey.lib.datastructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KdTree<P>
{

	public interface PointSelector<P>
	{
		/**
		 * Return whether the point is on the left or right side of a given
		 * point.
		 * 
		 * @param pPoint
		 *            the point that defines the plan
		 * @param pInsertPoint
		 *            the point to be inserted
		 * @param pAxis
		 *            which axis defines the plane
		 * @return -1 if pInsertPoint is left of pPoint, 1 if pInsertPoint is
		 *         right of pPoint and 0 if it is equal.
		 */
		int chooseSide(P pPoint, P pInsertPoint, int pAxis);

		/**
		 * Choose axis which defines the plane
		 * 
		 * @param pDepth
		 * @return
		 */
		int chooseAxis(int pDepth);

		/**
		 * If a range of points spans multiple sides of a plane split it into
		 * two points
		 * 
		 * @param pStart
		 *            the start of the range
		 * @param pEnd
		 *            the end of the range
		 * @param pAxis
		 *            the axis perpendicular to the plane
		 * @return the point which the range intersects the plane
		 */
		P splitPoint(P pStart, P pEnd, P pPlane, int pAxis);

		/**
		 * @return the dimension of P
		 */
		int dimension();
	}

	private static class PointComparator<P> implements Comparator<P>
	{
		private final int mAxis;
		private final PointSelector<P> mPointSelector;

		public PointComparator(int pAxis, PointSelector<P> pPointSelector)
		{
			mAxis = pAxis;
			mPointSelector = pPointSelector;
		}

		@Override
		public int compare(P pPoint1, P pPoint2)
		{
			return mPointSelector.chooseSide(pPoint1, pPoint2, mAxis);
		}
	}

	private static class Node<P>
	{
		private Node<P> mRightChild;
		private Node<P> mLeftChild;
		private final int mAxis;
		private final P mPoint;

		Node(int pAxis, P pPoint)
		{
			mAxis = pAxis;
			mPoint = pPoint;
		}

		Stream<Node<P>> children()
		{
			List<Node<P>> vChildren = new ArrayList<KdTree.Node<P>>();
			if (mLeftChild != null)
			{
				vChildren.add(mLeftChild);
			}
			if(mRightChild != null)
			{
				vChildren.add(mRightChild);
			}
			return vChildren.stream();
		}

		int getAxis()
		{
			return mAxis;
		}

		P getPoint()
		{
			return mPoint;
		}

		Node<P> left()
		{
			return mLeftChild;
		}

		Node<P> right()
		{
			return mRightChild;
		}

		@Override
		public String toString()
		{
			return "node_" + hashCode();
		}
		
		public String label()
		{
			return "\"" + mPoint.toString() + "\"";
		}
		
		public String writeDot()
		{
			String vString = "";
			if (mLeftChild != null)
			{
				vString = vString + toString() + " -> " + mLeftChild.toString() + "\n";
			}
			if (mRightChild != null)
			{
				vString = vString + toString() + " -> " + mRightChild.toString() + "\n";
			}
			return vString;
		}
	}

	private static class LeafNode<P> extends Node<P>
	{
		LeafNode(int pAxis, P pPoint)
		{
			super(pAxis, pPoint);
		}
		
		@Override
		Stream<Node<P>> children()
		{
			return new ArrayList<Node<P>>().stream();
		}
		
		@Override
		public String writeDot()
		{
			return "";
		}
	}

	private final Node<P> mRoot;
	private final PointSelector<P> mPointSelector;

	public KdTree(PointSelector<P> pPointSelector, List<P> pPoints)
	{
		mPointSelector = pPointSelector;
		mRoot = kdtree(pPoints, 0);
	}

	private Node<P> kdtree(List<P> pPoints, int pDepth)
	{
		Node<P> vNode = null;
		final int vAxis = mPointSelector.chooseAxis(pDepth);
		Collections.sort(pPoints, new PointComparator<P>(vAxis, mPointSelector));
		if (pPoints.size() == 1)
		{
			vNode = new LeafNode<>(vAxis, pPoints.get(0));
		}
		else
		{
			int vMedianIndex = splitIndex(pPoints.size());
			final P vMedian = pPoints.get(vMedianIndex);
			vNode = new Node<>(vAxis, vMedian);
			if (vMedianIndex == 0)
			{
				vNode.mLeftChild = null;
				vNode.mRightChild = new LeafNode<>(vAxis, pPoints.get(1));
			}
			else
			{
				vNode.mLeftChild = kdtree(pPoints.subList(0, vMedianIndex),
						pDepth + 1);
				vNode.mRightChild = kdtree(
						pPoints.subList(vMedianIndex + 1, pPoints.size()),
						pDepth + 1);
			}
		}
		return vNode;
	}

	public P find(P pPoint)
	{
		return find(pPoint, mRoot);
	}

	public P find(P pSearchPoint, Node<P> pNode)
	{
		Node<P> vNext = chooseNode(pSearchPoint, pNode);
		return vNext.mLeftChild == null && vNext.mRightChild == null ? vNext.mPoint : find(pSearchPoint, vNext);
	}

	public Set<P> find(P pStart, P pEnd)
	{
		return find(pStart, pEnd, mRoot);
	}

	private Set<P> find(P pStart, P pEnd, Node<P> pNode)
	{
		Node<P> vStart = chooseNode(pStart, pNode);
		Node<P> vEnd = chooseNode(pEnd, pNode);
		Set<P> vNodes = new HashSet<P>();
		if (vStart.equals(vEnd))
		{
			if (vStart.equals(pNode))
			{
				vNodes.add(pNode.getPoint());
			}
			else
			{
				vNodes = find(pStart, pEnd, vStart);
			}
		}
		else
		{
			P vSplit = mPointSelector.splitPoint(pStart, pEnd,
					pNode.getPoint(), pNode.getAxis());
			vNodes.addAll(find(pStart, vSplit, pNode));
			vNodes.addAll(find(vSplit, pEnd, pNode));
		}
		return vNodes;
	}

	public String toDot()
	{
		StringBuilder vBuilder = new StringBuilder("digraph G{\n");
		vBuilder.append(traverse(mRoot).stream().flatMap(pNode -> writeDot(pNode)).collect(Collectors.joining("\n")));
		vBuilder.append("}");
		return vBuilder.toString();
	}
	
	private List<Node<P>> traverse(Node<P> pNode)
	{
		List<Node<P>> vNodes = new ArrayList<KdTree.Node<P>>();
		vNodes.add(pNode);
		pNode.children().map(pChild -> traverse(pChild)).forEach(pList -> vNodes.addAll(pList));
		return vNodes;
	}
	
	private Stream<String> writeDot(Node<P> pNode)
	{
		return Arrays.asList(pNode.toString() + " [label=" + pNode.label() + "];", pNode.writeDot()).stream();
	}

	private Node<P> chooseNode(P pSearchPoint, Node<P> pNode)
	{
		int vChoice = mPointSelector.chooseSide(pSearchPoint, pNode.getPoint(), 
				pNode.getAxis());
		return vChoice == 0 ? pNode : vChoice == -1 ? pNode.left() : pNode
				.right();
	}

	private int splitIndex(int pSize)
	{
		return Math.floorDiv(pSize - 1, 2);
	}
}
