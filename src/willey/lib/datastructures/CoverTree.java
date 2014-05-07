package willey.lib.datastructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import willey.lib.math.geometry.Metric;
import willey.lib.util.Pair;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class CoverTree<P>
{
	private final Metric<P> mMetric;
	private final double mEpsilon;
	
	private Node<P> mRoot;
	private int mRootLevel = 0;
	private int mBottomLevel = 0;

	public CoverTree(Metric<P> pMetric, double pEpsilon, P pRoot)
	{
		mMetric = pMetric;
		mEpsilon = pEpsilon;
		mRoot = new Node<P>(pRoot);
	}

	public Pair<P, Double> findNN(P pPoint)
	{
		Set<Node<P>> vTop = new HashSet<Node<P>>();
		vTop.add(mRoot);
		Pair<Node<P>, Double> vNearest = findNN(pPoint, vTop, mRootLevel);
		return Pair.of(vNearest.getA().getData(), vNearest.getB());
	}

	private Pair<Node<P>, Double> getNearest(P pPoint, Iterable<Node<P>> pNodes)
	{
		double vMinDistance = Double.MAX_VALUE;
		Node<P> vNearest = null;
		for (Node<P> vNode : pNodes)
		{
			double vDistance = mMetric.compute(pPoint, vNode.getData());
			if (vDistance < vMinDistance)
			{
				vMinDistance = vDistance;
				vNearest = vNode;
			}
		}
		return Pair.of(vNearest, Double.valueOf(vMinDistance));
	}

	private Pair<Node<P>, Double> findNN(P pPoint, Set<Node<P>> pNodes,
			int pLevel)
	{
		Set<Node<P>> vQ = new HashSet<CoverTree.Node<P>>();
		Pair<Node<P>, Double> vNearest = getNearest(pPoint, pNodes);
		if (pLevel > mBottomLevel - 1)
		{
			for (Node<P> vNode : pNodes)
			{
				DistancePredicate<P> vDistance = new DistancePredicate<P>(
						pPoint, vNearest.getB().doubleValue()
								+ Math.pow(2, pLevel), mMetric);
				for (Node<P> vChild : vNode.getChildren())
				{
					if (vDistance.test(vChild))
					{
						vQ.add(vChild);
					}
				}
			}
			if (!vQ.isEmpty())
			{
				vNearest = findNN(pPoint, vQ, pLevel - 1);
			}
		}
		return vNearest;
	}

	public void insert(P pPoint)
	{
		Set<Node<P>> vTop = new HashSet<Node<P>>();
		vTop.add(mRoot);
		if (insert(pPoint, vTop, mRootLevel))
		{
			double vDistanceToRoot = mMetric.compute(pPoint, mRoot.getData());
			while (vDistanceToRoot > Math.pow(2, mRootLevel))
			{
				mRoot = mRoot.createSelfParent();
				mRootLevel++;
			}
			mRoot.addChild(new Node<P>(pPoint));
		}
	}

	private boolean insert(P pPoint, Set<Node<P>> pNodes, int pLevel)
	{
		Set<Node<P>> vQ = new HashSet<Node<P>>();
		for (Node<P> vNode : pNodes)
		{
			getValidChildren(
					vNode.addSelfAndGetChildren().stream(), Math.pow(2, pLevel), pPoint).forEach((pNode) ->
			{
				vQ.add(pNode);
			});
		}
		boolean vNoParentFound = vQ.isEmpty();
		if (!vNoParentFound)
		{
			if (!insert(pPoint, vQ, pLevel - 1))
			{
				Pair<Node<P>, Double> vNearest = getNearest(pPoint, vQ); 
				Node<P> vNode = vNearest.getA();
				if (vNearest.getB() > mEpsilon)
				{
				vNode.addChild(new Node<P>(pPoint));
				if (pLevel < mBottomLevel)
				{
					mBottomLevel = pLevel - 1;
				}
				}
				vNoParentFound = false;
			}
		}
		return vNoParentFound;
	}

	public void remove(P pPoint)
	{
		if (isRoot(pPoint))
		{
			Node<P> vChild = mRoot.getChildren().iterator().next();
			vChild.emancipate();
			mRoot = vChild.createSelfParent();
		}
		Node<P> vPointNode = new Node<P>(pPoint);
		List<Map<P, Node<P>>> vTop = new ArrayList<Map<P, Node<P>>>();
		Map<P, Node<P>> vRoot = new HashMap<P, Node<P>>();
		vRoot.put(mRoot.getData(), mRoot);
		vTop.add(vRoot);
		remove(vPointNode, vTop, mRootLevel);
	}
	
	private boolean isRoot(P pPoint)
	{
		return mRoot.getData().equals(pPoint);
	}

	private void remove(Node<P> pPointNode, List<Map<P, Node<P>>> pCoverSets,
			int pLevel)
	{
		Map<P, Node<P>> vQ = new HashMap<P, Node<P>>();
		for (Node<P> vNode : pCoverSets.get(0).values())
		{
			DistancePredicate<P> vDistance = new DistancePredicate<P>(
					pPointNode.getData(), Math.pow(2, pLevel), mMetric);
			for (Node<P> vChild : vNode.getChildren())
			{
				if (vDistance.test(vChild))
				{
					vQ.put(vChild.getData(), vChild);
				}
			}
		}
		if (!vQ.isEmpty())
		{
			List<Map<P, Node<P>>> vCoverSets = new ArrayList<Map<P, Node<P>>>();
			vCoverSets.add(vQ);
			vCoverSets.addAll(pCoverSets);
			remove(pPointNode.getSelf(), vCoverSets, pLevel - 1);
		}
		if (pCoverSets.get(0).get(pPointNode.getData()) != null)
		{
			Node<P> vNode = pCoverSets.get(0).remove(pPointNode.getData());
			if (vNode.mParent != null)
			{
				vNode.emancipate();
			}

			for (Node<P> vChild : vNode.getChildren())
			{
				int vNextLevel = pLevel - 1;
				vChild.emancipate();
				while (pLevel < pCoverSets.size() && getNearest(vChild.getData(), pCoverSets.get(vNextLevel).values())
						.getB() > Math.pow(2, vNextLevel))
				{
					vChild = vChild.createSelfParent();
					vNextLevel++;
				}
				getNearest(vChild.getData(), pCoverSets.get(vNextLevel).values()).getA().addChild(vChild);
			}
		}
	}
	
	public boolean contains(P pPoint)
	{
		return findNN(pPoint).getA().equals(pPoint);
	}

	private Stream<Node<P>> getValidChildren(Stream<Node<P>> pNodes,
			double pDistance, P pPoint)
	{
		return pNodes.filter(new DistancePredicate<P>(
				pPoint, pDistance, mMetric));
	}
	
	public void insert2(P pPoint)
	{
		
	}
	
//	public boolean insert2(P pPoint, List<Set<Node<P>>> pCoverSets, int pLevel)
//	{
//		Set<Node<P>> vQ = new HashSet<Node<P>>();
//		for (Node<P> vNode : pCoverSets.get(0))
//		{
//			vQ.addAll(vNode.getChildren());
//		}
//	}

	private static class Node<P>
	{
		private Node<P> mParent = null;
		private final P mData;
		private Node<P> mSelf;
		private final Set<Node<P>> mChildren = new HashSet<Node<P>>();

		Node(P pData)
		{
			mData = pData;
		}

		Node<P> createSelfParent()
		{
			Node<P> vParent = new Node<P>(mData);
			vParent.mSelf = this;
			vParent.addChild(this);
			return vParent;
		}

		P getData()
		{
			return mData;
		}

		void setParent(Node<P> pParent)
		{
			mParent = pParent;
		}

		Node<P> getSelf()
		{
			if (mSelf == null)
			{
				createSelfChild();
			}
			return mSelf;
		}

		private Node<P> createSelfChild()
		{
			mSelf = new Node<P>(mData);
			addChild(mSelf);
			return mSelf;
		}

		void addChild(Node<P> pChild)
		{
			pChild.setParent(this);
			mChildren.add(pChild);
		}

		void emancipate()
		{
			mParent.removeChild(this);
		}

		void removeChild(Node<P> pChild)
		{
			if (mChildren.remove(pChild))
			{
				pChild.setParent(null);
			}
		}

		Collection<Node<P>> getChildren()
		{
			return mChildren;
		}

		Collection<Node<P>> addSelfAndGetChildren()
		{
			if (mSelf == null)
			{
				createSelfChild();
			}
			return mChildren;
		}
		
		boolean hasChildren()
		{
			return !mChildren.isEmpty();
		}

		@Override
		public int hashCode()
		{
			return mData.hashCode();
		}

		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object pNode)
		{
			boolean vEquals = pNode instanceof Node;
			if (vEquals)
			{
				vEquals = ((Node) pNode).getData().getClass() == mData
						.getClass();
				if (vEquals)
				{
					@SuppressWarnings("unchecked")
					Node<P> vNode = (Node<P>) pNode;
					vEquals = mData.equals(vNode.getData());
				}
			}
			return vEquals;
		}

		@Override
		public String toString()
		{
			return mData.toString();
		}
	}

	private static class DistancePredicate<P> implements Predicate<Node<P>>
	{
		private final P mPoint;
		private double mDistance;
		private final Metric<P> mMetric;

		private DistancePredicate(P pPoint, double pDistance, Metric<P> pMetric)
		{
			mPoint = pPoint;
			mDistance = pDistance;
			mMetric = pMetric;
		}

		@Override
		public boolean test(Node<P> pArgument)
		{
			return mMetric.compute(mPoint, pArgument.getData()) <= mDistance;
		}
	}
}
