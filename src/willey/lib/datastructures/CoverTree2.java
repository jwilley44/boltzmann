package willey.lib.datastructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import willey.lib.math.geometry.Metric;
import willey.lib.util.Check;

import java.util.function.Function;

import willey.lib.util.Pair;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class CoverTree2<P>
{
	private static class Node<P>
	{
		private final P mData;
		private final int mLevel;
		private final Set<Node<P>> mChildren = new HashSet<Node<P>>();
		private Node<P> mParent;

		Node(P pData, int pLevel)
		{
			mData = pData;
			mLevel = pLevel;
		}

		P getData()
		{
			return mData;
		}

		Node<P> getParent()
		{
			return mParent;
		}

		int getLevel()
		{
			return mLevel;
		}

		Collection<Node<P>> getChildren()
		{
			return mChildren;
		}
		
		private Node<P> createAndAddChildData(P pData)
		{
			Node<P> vChild = new Node<P>(pData, mLevel - 1);
			addChild(vChild);
			return vChild; 
		}

		private void addChild(Node<P> pNode)
		{
			mChildren.add(pNode);
			pNode.setParent(this);
		}

		private void setParent(Node<P> pNode)
		{
			mParent = pNode;
		}

		Node<P> createSelfChild()
		{
			Node<P> vChild = new Node<P>(mData, mLevel - 1);
			makeChild(this, vChild);
			return vChild;
		}

		void emancipate()
		{
			mParent.mChildren.remove(this);
			mParent = null;
		}

		@Override
		public String toString()
		{
			return "{" + mData.toString() + "@" + mLevel + "}";
		}

		@Override
		public int hashCode()
		{
			return mData.hashCode();
		}

		@Override
		public boolean equals(Object pObject)
		{
			boolean vEquals = false;
			if (pObject instanceof Node)
			{
				Object vData = ((Node) pObject).getData();
				vEquals = mData.equals(vData);
			}
			return vEquals;
		}

		static <P> void makeChild(Node<P> pParent, Node<P> pChild)
		{
			pParent.addChild(pChild);
			pChild.setParent(pParent);
		}
	}

	private final Metric<P> mMetric;
	private final double mScale;
	// private final double mEpsilon;

	private Node<P> mRoot;
	private int mBottomLevel = 0;
	private Set<Node<P>> mBottomSet = new HashSet<Node<P>>();

	public CoverTree2(Metric<P> pMetric, P pRoot, double pScale)
	{
		mMetric = pMetric;
		mRoot = new Node<P>(pRoot, 0);
		mBottomSet.add(mRoot);
		mScale = pScale;
		Check.kIllegalArgument.checkTrue(mScale > 1,
				"Scale must be greater than one");
	}

	public int size()
	{
		return mBottomSet.size();
	}

	public Iterable<P> getData()
	{
		Set<P> vData = new HashSet<P>();
		for (Node<P> vNode : mBottomSet)
		{
			vData.add(vNode.getData());
		}
		return vData;
	}

	public Pair<P, Double> getNearesetNeighbor(P pPoint)
	{
		int vLevel = mRoot.getLevel();
		CoverSet<P> vCoverSet = rootCoverSet();
		double vMinDistance = mMetric.compute(pPoint, vCoverSet
				.getNearestPoint().getData());
		while (vLevel >= mBottomLevel)
		{
			CoverSet<P> vNext = vCoverSet.next(getFunction(pPoint,
					Math.pow(mScale, vLevel) + vMinDistance));
			if (vNext.isEmpty())
			{
				break;
			}
			vLevel--;
			vCoverSet = vNext;
			vMinDistance = mMetric.compute(pPoint, vCoverSet.getNearestPoint()
					.getData());
		}
		return Pair.of(vCoverSet.getNearestPoint().getData(), vMinDistance);
	}

	private CoverSet<P> rootCoverSet()
	{
		Set<Node<P>> vSet = new HashSet<Node<P>>();
		vSet.add(mRoot);
		return new CoverSet<P>(vSet, mRoot);
	}

	public boolean insert(P pPoint)
	{
		boolean vInserted = false;
		if (!mBottomSet.contains(pPoint))
		{
			boolean vMakeNewRoot = true;
			while (vMakeNewRoot)
			{
				vMakeNewRoot = makeNewRoot(pPoint);
			}
			vInserted = insert(pPoint, rootCoverSet(), mRoot.getLevel());
		}
		return vInserted;
	}

	public boolean contains(P pPoint)
	{
		return mBottomSet.contains(new Node<P>(pPoint, mBottomLevel));
	}

	public boolean remove(P pPoint)
	{
		boolean vRemoved = false;
		if (contains(pPoint))
		{
			if (mRoot.getData().equals(pPoint))
			{
				removeRoot();
			}
			List<CoverSet<P>> vCoverSets = new ArrayList<CoverSet<P>>();
			Set<Node<P>> vSet = new HashSet<Node<P>>();
			vSet.add(mRoot);
			vCoverSets.add(new CoverSet<P>(vSet, mRoot));
			remove(pPoint, vCoverSets, mRoot.getLevel());
			vRemoved = true;
		}
		return vRemoved;
	}
	
	private void removeRoot()
	{
		Collection<Node<P>> vChildren = mRoot.getChildren();
		int vLevel = mRoot.getLevel() - 1;
		while (vChildren.size() == 1)
		{
			vChildren = vChildren.iterator().next().getChildren();
			vLevel--;
		}
		Node<P> vNew = vChildren.stream().filter(new NotNode<P>(mRoot.getData())).findFirst().get();
		mRoot = new Node<P>(vNew.getData(), vLevel + 1);
//		for (Node<P> vNode : IterableUtil.toSkipIterable(vChildren, new NotNode<P>(mRoot.getData())))
//		{
//				mRoot = new Node<P>(vNode.getData(), vLevel + 1);
//				break;
//		}
		for (Node<P> vNode : vChildren)
		{
			Node.makeChild(mRoot, vNode);
		}
	}

	private void remove(P pPoint, List<CoverSet<P>> pCoverSets, int pLevel)
	{
		CoverSet<P> vCoverSet = pCoverSets.get(0).next(
				getFunction(pPoint, Math.pow(mScale, pLevel)));
		pCoverSets.add(0, vCoverSet);

		if (pLevel > mBottomLevel + 1)
		{
			remove(pPoint, pCoverSets, pLevel - 1);
		}
		else
		{
			mBottomSet.remove(vCoverSet.getNearestPoint());
		}
		
		if (vCoverSet.contains(pPoint))
		{
			Node<P> vNode = vCoverSet.getNearestPoint();
			vNode.emancipate();
			for (Node<P> vChild : vNode.getChildren())
			{
				Node<P> vNewNode = vChild;
				int vLevel = pLevel - 1;
				for (CoverSet<P> vCover : pCoverSets)
				{
					Node<P> vNearest = getNearest(vCover.mPoints.stream().filter(new NotNode<P>(pPoint)), pPoint,
							Math.pow(mScale, vLevel));
					if (vNearest != null)
					{
						vNearest.addChild(vNewNode);
						break;
					}
					else
					{
						Node.makeChild(
								new Node<P>(vNewNode.getData(), vNewNode
										.getLevel() + 1), vNewNode);
						vNewNode = vNewNode.getParent();
						vLevel++;
					}
				}
			}
			pCoverSets.remove(0);
		}
	}

	private Node<P> getNearest(Stream<Node<P>> pPoints, P pPoint,
			double vMaxDistance)
	{
				return pPoints.map((pNode) -> 
				Pair.of(pNode, Double.valueOf(mMetric.compute(pPoint, pNode.getData())))).
				min((p1, p2) -> Double.compare(p1.getB().doubleValue(), p2.getB().doubleValue())).get().getA();
	}

	private boolean makeNewRoot(P pPoint)
	{
		double vDistance = computeDistance(pPoint, mRoot.getData());
		boolean vMakeNewRoot = false;
		if (vDistance > Math.pow(mScale, mRoot.getLevel()))
		{
			Node<P> vNewRoot = new Node<P>(mRoot.getData(),
					mRoot.getLevel() + 1);
			Node.makeChild(vNewRoot, mRoot);
			mRoot = vNewRoot;
			vDistance = computeDistance(mRoot.getData(), pPoint);
			vMakeNewRoot = vDistance > Math.pow(mScale, mRoot.getLevel());
		}
		return vMakeNewRoot;
	}
	
	private boolean insert(P pPoint, CoverSet<P> pCoverSet, int pLevel)
	{
		double vMinDistance = mMetric.compute(pCoverSet.getNearestPoint().getData(), pPoint);
		if (pLevel == mBottomLevel)
		{
			extendBottomLevel();
		}
		CoverSet<P> vNext = pCoverSet.next(getFunction(pPoint,
					Math.pow(mScale, pLevel)));
		boolean vReturn;
		if (vNext.isEmpty())
		{
			vReturn = false;
		}
		else
		{
			if (!insert(pPoint, vNext, pLevel-1) && vMinDistance <= Math.pow(mScale, pLevel) && !mBottomSet.contains(new Node<P>(pPoint, mBottomLevel)))
			{
				insert(pPoint, pLevel-1, pCoverSet.getNearestPoint());
				vReturn = true;
			}
			else
			{
				vReturn = false;
			}
		}
		return vReturn;
	}
	
	private void extendBottomLevel()
	{
			mBottomLevel--;
			Set<Node<P>> vNewBottom = new HashSet<Node<P>>();
			for (Node<P> vNode : mBottomSet)
			{
				vNewBottom.add(vNode.createSelfChild());
			}
			mBottomSet = vNewBottom;
	}
	
	private CoverSetFunction<P> getFunction(P pPoint, double pIncludeDistance)
	{
		return new CoverSetFunction<P>(pIncludeDistance, pPoint, mMetric);
	}

	private void insert(P pPoint, int pLevel, Node<P> pParent)
	{
		Node<P> vChild = pParent.createAndAddChildData(pPoint);
		if (vChild.getLevel() < mBottomLevel)
		{
			extendBottomLevel();
		}
		while (vChild.getLevel() > mBottomLevel)
		{
			vChild = vChild.createSelfChild();
		}
		mBottomSet.add(vChild);
	}

	private double computeDistance(P pPoint1, P pPoint2)
	{
		return mMetric.compute(pPoint1, pPoint2);
	}

	public String getString()
	{
		StringBuilder vBuilder = new StringBuilder();
		Set<Node<P>> vLevel = new LinkedHashSet<Node<P>>(mBottomSet);
		while (!vLevel.isEmpty())
		{
			Set<Node<P>> vNext = new LinkedHashSet<Node<P>>();
			for (Node<P> vNode : vLevel)
			{
				vBuilder.append(vNode.toString() + "  ");
				if (vNode.getParent() != null
						&& !vNext.contains(vNode.getParent()))
				{
					vNext.add(vNode.getParent());
				}
			}
			vBuilder.append("\n");
			vLevel = vNext;
		}
		return vBuilder.toString();
	}

	private static class CoverSetFunction<P> implements
			Function<P, Pair<Double, Boolean>>
	{
		private final double mIncludeDistance;
		private final P mPoint;
		private final Metric<P> mMetric;

		CoverSetFunction(double pIncludeDistance, P pPoint, Metric<P> pMetric)
		{
			mIncludeDistance = pIncludeDistance;
			mPoint = pPoint;
			mMetric = pMetric;
		}

		public Pair<Double, Boolean> apply(P pPoint)
		{
			double vDistance = mMetric.compute(mPoint, pPoint);
			boolean vInclude = vDistance <= mIncludeDistance;
			return Pair
					.of(Double.valueOf(vDistance), Boolean.valueOf(vInclude));
		}
	}

	private static class CoverSet<P>
	{
		private final Set<Node<P>> mPoints;
		private final Node<P> mNearesetPoint;

		CoverSet(Set<Node<P>> pPoints, Node<P> pNearestPoint)
		{
			mPoints = pPoints;
			mNearesetPoint = pNearestPoint;
		}

		boolean isEmpty()
		{
			return mPoints.isEmpty();
		}

		boolean contains(P pPoint)
		{
			return mPoints.contains(new Node<P>(pPoint, mNearesetPoint
					.getLevel()));
		}

		boolean remove(P pPoint)
		{
			return mPoints
					.remove(new Node<P>(pPoint, mNearesetPoint.getLevel()));
		}

		Node<P> getNearestPoint()
		{
			return mNearesetPoint;
		}

		CoverSet<P> next(CoverSetFunction<P> pFunction)
		{
			Node<P> vNearest = null;
			double vMin = Double.MAX_VALUE;
			Set<Node<P>> vPoints = new HashSet<Node<P>>();

			for (Node<P> vParent : mPoints)
			{
				for (Node<P> vPoint : vParent.getChildren())
				{
					Pair<Double, Boolean> vPair = pFunction.apply(vPoint
							.getData());
					if (vPair.getA().doubleValue() < vMin)
					{
						vMin = vPair.getA().doubleValue();
						vNearest = vPoint;
					}
					if (vPair.getB().booleanValue())
					{
						vPoints.add(vPoint);
					}
				}
			}
			return new CoverSet<P>(vPoints, vNearest);
		}

		@Override
		public String toString()
		{
			return isEmpty() ? "EMPTY" : mNearesetPoint.toString() + "  "
					+ mPoints.toString();
		}
	}
	
	private static class NotNode<P> implements Predicate<Node<P>>
	{
		private final P mData;
		
		NotNode(P pData)
		{
			mData = pData;
		}
		
		@Override
		public boolean test(Node<P> pArgument)
		{
			return !pArgument.equals(mData);
		}
	}
}
