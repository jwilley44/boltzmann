package willey.lib.physics.polymer.interactor;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.util.Pair;
import willey.lib.util.StreamUtil;

class RodsUtil
{
	static List<Rod> getRods(int pCount, double pLength, double pRadius,
			double pTranslation, double pRotation, Lattice pLattice,
			Orientation pOrientation, Position pPosition)
	{
		return StreamUtil.zipStreams(pOrientation.stream(pCount),
				pPosition.stream(pLattice, pCount),
				UniformRodCreator.get(pLength, pRadius)).collect(
				Collectors.toList());
	}

	private static class UniformRodCreator implements BiFunction<CartesianVector, CartesianVector, Rod>
	{
		private final double mLength;
		private final double mRadius;
		
		static UniformRodCreator get(double pLength, double pRadius)
		{
			return new UniformRodCreator(pLength, pRadius);
		}
		
		public UniformRodCreator(double pLength, double pRadius)
		{
			mLength = pLength;
			mRadius = pRadius;
		}
		
		@Override
		public Rod apply(CartesianVector pDirection, CartesianVector pPosition)
		{
			return new Rod(pDirection, pPosition, mLength, mRadius);
		}
		
	}

	public enum Orientation
	{
		Random
		{
			@Override
			Stream<CartesianVector> stream(int pCount)
			{
//				Stream<Pair<Double, Double>> vXY = StreamUtil.zipStreams(
//						MathUtil.randomStream(pCount, 0, 1).boxed(), 
//						MathUtil.randomStream(pCount, 0, 1).boxed(), Pair::of);
//				
//				return StreamUtil.zipStreams(
//						vXY, 
//						MathUtil.randomStream(pCount, 0, 1).boxed(),Pair::of)
//						.map(pCoor -> CartesianVector.of(
//						pCoor.getA().getA().doubleValue(), 
//						pCoor.getA().getB().doubleValue(), 
//						pCoor.getB().doubleValue()));
				return StreamUtil.toStream(() -> CartesianVector.randomUnitVector(), pCount);
			}
		},
		Ordered
		{
			@Override
			Stream<CartesianVector> stream(int pCount)
			{
				return StreamUtil.toStream(CartesianVector.of(0,  0, 1), pCount, false);
			}
		};

		abstract Stream<CartesianVector> stream(int pCount);
	}

	public enum Position
	{
		Random
		{
			@Override
			Stream<CartesianVector> stream(Lattice pLattice, int pCount)
			{
				Stream<Pair<Double, Double>> vXY = StreamUtil.zipStreams(
						MathUtil.randomStream(pCount, 0, pLattice.xDimension()).boxed(), 
						MathUtil.randomStream(pCount, 0, pLattice.xDimension()).boxed(), Pair::of);
				
				return StreamUtil.zipStreams(
						vXY, 
						MathUtil.randomStream(pCount, 0, pLattice.xDimension()).boxed(),Pair::of)
						.map(pCoor -> CartesianVector.of(
						pCoor.getA().getA().doubleValue(), 
						pCoor.getA().getB().doubleValue(), 
						pCoor.getB().doubleValue()));
			}
		},
		Ordered
		{
			@Override
			Stream<CartesianVector> stream(Lattice pLattice, int pCount)
			{
				int vScale = (int) Math.ceil((double)pCount / (pLattice.xDimension() * pLattice.yDimension()));
				double vStepSize = 1.0 / vScale;
				List<CartesianVector> vCoordinates = StreamUtil
						.nestedStream(
								MathUtil.sequence(0.0,
										pLattice.xDimension() * vScale,
										vStepSize).boxed(),
								MathUtil.sequence(0.0,
										pLattice.yDimension() * vScale,
										vStepSize).boxed())
						.map(pPair -> CartesianVector.of(pPair.getA()
								.doubleValue(), pPair.getB().doubleValue(), 0))
						.collect(Collectors.toList());
				Collections.shuffle(vCoordinates);
				return vCoordinates.subList(0, pCount - 1).stream();
			}
		};

		abstract Stream<CartesianVector> stream(Lattice pLattice, int pCount);
	}
}
