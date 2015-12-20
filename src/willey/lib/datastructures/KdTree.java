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
	public enum Side
	{
		Left(-1), Right(1);
		
		private final int mInt;
		
		Side(int pInt)
		{
			mInt = pInt;
		}
		
		public static Side convert(int pComparison)
		{
			return pComparison > 0 ? Right : Left;
		}
	}
	
	public interface PointSelector<P>
	{
		/**
		 * Return whether the point is on the left or right side of a given
		 * point.
		 * 
		 * @param pPoint
		 *            the point that defines the plane
		 * @param pInsertPoint
		 *            the point to be inserted
		 * @param pAxis
		 *            which axis defines the plane
		 * @return -1 if pInsertPoint is left of pPoint, 1 if pInsertPoint is
		 *         right of pPoint and 0 if it is equal.
		 */
		Side chooseSide(P pPoint, P pInsertPoint, int pAxis);

		/**
		 * Choose axis which defines the plane
		 * 
		 * @param pDepth
		 * @return
		 */
		int chooseAxis(int pDepth);

		/**
		 * Is the test point located between the two end points;
		 * 
		 * @param pTest the point to test
		 * @param pEnd1 endpoint 1
		 * @param pEnd2 endpoint 2
		 * @param pAxis axis that defines the range of pEnd1 and pEnd2
		 * @return
		 */
		boolean contains(P pTest, P pEnd1, P pEnd2, int pAxis);
		
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
			return mPointSelector.chooseSide(pPoint1, pPoint2, mAxis).mInt;
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
		
		boolean isLeaf()
		{
			return false;
		}

		int getAxis()
		{
			return mAxis;
		}

		P getPoint()
		{
			return mPoint;
		}
		
		Node<P> child(Side pSide)
		{
			return pSide.equals(Side.Left) ? left() : right();
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
		
		@Override
		boolean isLeaf()
		{
			return true;
		}
	}

	private final Node<P> mRoot;
	private final PointSelector<P> mPointSelector;
	private final int mDepth;

	public KdTree(PointSelector<P> pPointSelector, List<P> pPoints)
	{
		mPointSelector = pPointSelector;
		mRoot = kdtree(new ArrayList<P>(pPoints), 0);
		mDepth = (int)Math.ceil(Math.log(pPoints.size())/Math.log(2));
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
	
	public int getDepth()
	{
		return mDepth;
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

	/**
	 * Find the set of points contained in the range
	 * 
	 * @param pRange the hypercube which defines the range
	 * @return the set of leaf nodes contained in the hyper cube
	 */
	public Set<P> find(HyperCube<P> pRange)
	{
		return find(mRoot, pRange);
	}
	
	private Set<P> find(Node<P> pNode, HyperCube<P> pRange)
	{
		Set<P> vPoints = new HashSet<P>();
		if (pNode.isLeaf())
		{
			vPoints.add(pNode.getPoint());
		}
		else
		{
			if (pRange.intersects(pNode.getPoint()))
			{
				vPoints.addAll(find(pNode.left(), pRange));
				vPoints.addAll(find(pNode.right(), pRange));
			}
			else
			{
				Side vSide = mPointSelector.chooseSide(pNode.getPoint(), pRange.getCorner(pNode.getAxis()).getA(), pNode.getAxis());
				vPoints.addAll(find(pNode.child(vSide), pRange));
			}
		}
		return vPoints;
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
		return pNode.child(mPointSelector.chooseSide(pSearchPoint, pNode.getPoint(), pNode.getAxis()));
	}
	
	private int splitIndex(int pSize)
	{
		return Math.floorDiv(pSize - 1, 2);
	}
}
