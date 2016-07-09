package willey.lib.physics.polymer.measurement;

import static willey.lib.math.linearalgebra.CartesianVector.vectorSum;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.Interactor;
import willey.lib.physics.polymer.interactor.Measurable;
import willey.lib.physics.polymer.interactor.Polymer;
import willey.lib.physics.polymer.interactor.Rods;
import willey.lib.util.Pair;
import willey.lib.util.StreamUtil;

public class MeasurementUtil
{
	static class CountInteractions implements Consumer<Interactor>
	{
		private int mIndex = 1;
		private int mInteractions = 0;
		private final Measurable mInteractors;

		CountInteractions(Measurable pInteractors)
		{
			mInteractors = pInteractors;
			mInteractors.getInteractors().forEach(this);
		}

		@Override
		public void accept(final Interactor pInteractor)
		{
			mInteractions = mInteractors.getInteractors().skip(mIndex)
					.anyMatch((p) -> pInteractor.interactionDistance(p) > 0) ? mInteractions + 1
					: mInteractions;
			mIndex++;
		}

		public int interactions()
		{
			return mInteractions;
		}
	}

	static double correlation(Stream<CartesianVector> pVectors1,
			Stream<CartesianVector> pVectors2, int pCount)
	{
		double vDenominator = pCount * (pCount - 1) / 2;
		return StreamUtil
				.getIncrementedStream(pVectors1, pVectors2)
				.mapToDouble((pPair) -> correlation(pPair.getA(), pPair.getB()))
				.sum()
				/ vDenominator;
	}

	static double correlation(CartesianVector pA, CartesianVector pB)
	{
		double vCos = pA.cosTheta(pB);
		return Math.sqrt(vCos * vCos);
	}
	
	static CartesianVector averageRodDirection(Rods pRods)
	{
		return pRods.getRods().map(pRod -> pRod.direction()).collect(CartesianVector.averageVector());
	}
	
	static CartesianVector polymerCenterOfMass(Polymer pPolymer)
	{
		return pPolymer
				.getMonomers()
				.map(pMonomer -> pMonomer.position())
				.reduce(CartesianVector.zeroVector(), (a, b) -> a.add(b))
				.scale(1.0 / pPolymer.getSize());
	}

	static double orderParameter(List<CartesianVector> pDirections)
	{
		return StreamUtil
				.getIncrementedStream(pDirections, pDirections)
				.mapToDouble(
						pPair -> Math.pow(
								pPair.getA().unitVector()
										.dotProduct(pPair.getB().unitVector()),
								2) - 1 / 3).sum()
				* (2 / (pDirections.size() * (pDirections.size() - 1.0)));
	}

	public static double parallelMagnitude(final CartesianVector pDirection,
			List<CartesianVector> pDirections)
	{
		return pDirections
				.stream()
				.mapToDouble(
						pVector -> Math.pow(
								pVector.unitVector().dotProduct(pDirection), 2))
				.sum()
				/ (pDirections.size());
	}

	public static double perpendicularMagnitude(
			final CartesianVector pDirection, List<CartesianVector> pDirections)
	{
		final double vOrderParameter = orderParameter(pDirections);
		return pDirections.stream()
				.map(pVector -> pDirection.projectOntoPlane(pVector))
				.mapToDouble(pVector -> pVector.magnitude()).sum()
				/ (pDirections.size() * vOrderParameter);
	}

	public static CartesianVector polymerPosition(Polymer pPolymer)
	{
		return pPolymer.getMonomers().map(pMonomer -> pMonomer.position())
				.reduce(vectorSum()).get();
	}
	
	public static List<Double> arcLength2Distance(List<CartesianVector> pPositions)
	{
		return calcDistances((p1,  p2) -> Double.valueOf(p1.distance(p2)), pPositions);
	}

	public static List<Double> arcLength2ParallelDistance(
			final CartesianVector pDirection, List<CartesianVector> pPositions)
	{
		return calcDistances(
				(pV1, pV2) -> Double.valueOf(Math.pow(pV1.subtract(pV2)
						.dotProduct(pDirection), 2)), pPositions);
	}
	
	public static List<Double> arcLenth2PependicularDistance(CartesianVector pDirection, List<CartesianVector> pPositions)
	{
		return calcDistances(
				(pV1, pV2) -> Double.valueOf(pV1.subtract(pV2)
						.projectOntoPlane(pDirection).magnitude()), pPositions);
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
		List<Pair<Integer, Double>> vPairs = StreamUtil
				.getIncrementedStream(vPositions, vPositions)
				.map(pPair -> calcDistance(pDistanceFunction, pPair.getA(),pPair.getB()))
				.collect(Collectors.toList());
		return StreamUtil
				.getIncrementedStream(vPositions, vPositions) //increment the stream to pair up positions
				.map(pPair -> calcDistance(pDistanceFunction, pPair.getA(),pPair.getB())) //calc arc length to distance 
				.collect(Collectors.groupingBy(Pair::getA)) //group by arc length
				.entrySet()
				.stream()
				.sorted((p1, p2) -> p1.getKey().compareTo(p2.getKey())) //sort so that smallest arc length is first
				.map(pEntry -> mean(pEntry.getValue().stream().map(pPair -> pPair.getB()).collect(Collectors.toList()))) //calculate the mean for each arc length
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
	
	public static Double mean(List<Double> pDistances)
	{
		return Double.valueOf(pDistances.stream()
				.mapToDouble(pDouble -> pDouble.doubleValue()).sum()
				/ pDistances.size());
	}
	
}
