package willey.lib.physics.polymer.measurement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.util.Pair;
import willey.lib.util.StreamUtil;

public class ArcLength2Distance
{
	private ArcLength2Distance(){}
	
	private static class ArcLengthConsumer implements
			Consumer<Pair<Integer, Double>>
	{
		private final Map<Integer, List<Double>> mArcLengthToDistance = new HashMap<Integer, List<Double>>();

		@Override
		public void accept(Pair<Integer, Double> pPair)
		{
			List<Double> vDistances = mArcLengthToDistance.get(pPair.getA());
			if (vDistances == null)
			{
				vDistances = new ArrayList<Double>();
				mArcLengthToDistance.put(pPair.getA(), vDistances);
			}
			vDistances.add(pPair.getB());
		}

		private List<Double> getDistances()
		{
			return mArcLengthToDistance.keySet().stream().sorted()
					.map(pKey -> mean(mArcLengthToDistance.get(pKey)))
					.collect(Collectors.toList());
		}

		
	}

	public static List<Double> calcDistances(
			final BiFunction<CartesianVector, CartesianVector, Double> pDistanceFunction,
			final List<CartesianVector> pPositions)
	{
		List<Pair<Integer, CartesianVector>> vPositions = pPositions
				.stream()
				.map(pPosition -> Pair.of(
						Integer.valueOf(pPositions.indexOf(pPosition)),
						pPosition)).collect(Collectors.toList());
		return StreamUtil
				.getIncrementedStream(vPositions, vPositions)
				.map(pPair -> calcDistance(pDistanceFunction, pPair.getA(),
						pPair.getB()))
						.collect(Collectors.groupingBy(Pair::getA))
						.entrySet()
						.stream()
						.sorted((p1, p2) -> p1.getKey().compareTo(p2.getKey()))
						.map(pEntry -> mean(pEntry.getValue().stream().map(pPair -> pPair.getB()).collect(Collectors.toList())))
						.collect(Collectors.toList());
	}

	private static Pair<Integer, Double> calcDistance(
			BiFunction<CartesianVector, CartesianVector, Double> pDistanceFunction,
			Pair<Integer, CartesianVector> p1, Pair<Integer, CartesianVector> p2)
	{
		return Pair.of(
				Integer.valueOf(p2.getA().intValue() - p1.getA().intValue()),
				pDistanceFunction.apply(p1.getB(), p2.getB()));
	}
	
	private static List<Double> getDistances(final Map<Integer, List<Double>> pMap)
	{
		return pMap.keySet().stream().sorted()
				.map(pKey -> mean(pMap.get(pKey)))
				.collect(Collectors.toList());
	}
	
	private static Double mean(List<Double> pDistances)
	{
		return Double.valueOf(pDistances.stream()
				.mapToDouble(pDouble -> pDouble.doubleValue()).sum()
				/ pDistances.size());
	}
}
