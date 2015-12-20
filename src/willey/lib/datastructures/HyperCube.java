package willey.lib.datastructures;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import willey.lib.datastructures.KdTree.PointSelector;
import willey.lib.util.Pair;

public class HyperCube<P>
{
	public static class Builder<P>
	{
		private final Map<Integer, Pair<P, P>> mCorners  = new HashMap<Integer, Pair<P,P>>();
		
		public Builder<P> add(P p1, P p2, int pAxis)
		{
			mCorners.put(Integer.valueOf(pAxis), Pair.of(p1, p2));
			return this;
		}
		
		public HyperCube<P> build(PointSelector<P> pPointSelector)
		{
			return new HyperCube<P>(pPointSelector, mCorners);
		}
	}
	
	public static <P> Builder<P> builder()
	{
		return new Builder<P>();
	}
	
	private final Map<Integer, Pair<P, P>> mCorners;
	private final PointSelector<P> mPointSelector;

	private HyperCube(PointSelector<P> pPointSelector, Map<Integer, Pair<P, P>> pCorners)
	{
		mPointSelector = pPointSelector;
		mCorners = new HashMap<Integer, Pair<P,P>>(pCorners);
	}

	boolean intersects(final P pTest)
	{
		return mCorners.entrySet().stream().allMatch(pEntry -> apply(pTest, pEntry));
	}
	
	boolean apply(P pTest, Entry<Integer, Pair<P, P>> pEntry)
	{
		return mPointSelector.contains(pTest, pEntry.getValue().getA(), pEntry.getValue().getB(), pEntry.getKey().intValue());
	}
	
	Pair<P, P> getCorner(int pAxis)
	{
		return mCorners.get(Integer.valueOf(pAxis));
	}
}
