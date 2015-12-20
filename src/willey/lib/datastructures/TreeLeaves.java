package willey.lib.datastructures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import willey.lib.util.Pair;

public class TreeLeaves<P, T>
{
	private final Map<P, Pair<Set<T>, Set<T>>> mMap = new HashMap<>();
	
	public TreeLeaves(List<P> pLeaves)
	{
		pLeaves.forEach(pLeaf -> mMap.put(pLeaf, Pair.of(new HashSet<>(), new HashSet<>())));
	}
	
	public void add(P pLeaf, T pPoint, boolean pIsLeft)
	{
		Pair<Set<T>, Set<T>> vPair = mMap.get(pPoint);
		Set<T> vSet = pIsLeft ? vPair.getA() : vPair.getB();
		vSet.add(pPoint);
	}
	
	public Set<T> getPoints(P pLeaf)
	{
		Set<T> vSet = new HashSet<T>();
		vSet.addAll(mMap.get(pLeaf).getA());
		vSet.addAll(mMap.get(pLeaf).getB());
		return vSet;
	}
}
