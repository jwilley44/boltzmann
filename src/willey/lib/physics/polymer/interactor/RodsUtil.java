package willey.lib.physics.polymer.interactor;

import static willey.lib.math.linearalgebra.CartesianVector.randomUnitVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.util.ConsumerUtil;
import willey.lib.util.Pair;
import willey.lib.util.StreamUtil;

class RodsUtil
{
	static List<Rod> getRods(int pCount, double pLength, double pRadius,
			double pTranslation, double pRotation, Lattice pLattice,
			Orientation pOrientation, Position pPosition)
	{
		Stream<Rod> vRods = StreamUtil.toStream(new RodSupplier(pLength,
				pRadius, pOrientation.getSupplier(), pPosition.getSupplier(pLattice)), pCount);
		return ConsumerUtil.toCollection(vRods, new ArrayList<Rod>());
	}

	private static class RodSupplier implements Supplier<Rod>
	{
		private final double mLength;
		private final double mRadius;
		private final Supplier<CartesianVector> mOrientation;
		private final Supplier<CartesianVector> mPosition;

		RodSupplier(double pLength, double pRadius, Supplier<CartesianVector> pOrientation,
				Supplier<CartesianVector> pPosition)
		{
			mLength = pLength;
			mRadius = pRadius;
			mOrientation = pOrientation;
			mPosition = pPosition;
		}

		@Override
		public Rod get()
		{
			return new Rod(mOrientation.get(), mPosition.get(), mLength,
					mRadius);
		}
	}

	public enum Orientation
	{
		Random
		{
			@Override
			Supplier<CartesianVector> getSupplier()
			{
				return new RandomDirectionSupplier();
			}
		},
		Ordered
		{
			@Override
			Supplier<CartesianVector> getSupplier()
			{
				return new UniformDirectionSupplier(CartesianVector.of(0, 0, 1));
			}
		};

		abstract Supplier<CartesianVector> getSupplier();
	}

	public enum Position
	{
		Random
		{
			@Override
			Supplier<CartesianVector> getSupplier(Lattice pLattice)
			{
				return new RandomStart(pLattice);
			}
		},
		Ordered
		{
			@Override
			Supplier<CartesianVector> getSupplier(Lattice pLattice)
			{
				return new OrderedStart(pLattice);
			}
		};

		abstract Supplier<CartesianVector> getSupplier(Lattice pLattice);
	}

	static class OrderedStart implements Supplier<CartesianVector>
	{
		private final Iterator<Pair<Integer, Integer>> mCoordinates;

		private OrderedStart(Lattice pLattice)
		{
			List<Pair<Integer, Integer>> vCoordiantes = new ArrayList<Pair<Integer, Integer>>();
			for (int i = 0; i < pLattice.xDimension(); i++)
			{
				for (int j = 0; j < pLattice.yDimension(); j++)
				{
					vCoordiantes.add(Pair.of(Integer.valueOf(i),
							Integer.valueOf(j)));
				}
			}
			Collections.shuffle(vCoordiantes);
			mCoordinates = vCoordiantes.iterator();
		}

		@Override
		public CartesianVector get()
		{
			CartesianVector vReturn = null;
			try
			{
				Pair<Integer, Integer> vXy = mCoordinates.next();
				vReturn = CartesianVector.of(vXy.getA().intValue() + 0.5, vXy
						.getB().intValue() + 0.5, 0.0);
			} catch (NullPointerException e)
			{
				throw new IllegalStateException(
						"Cannot have more than the maximum numbered of ordered rods");
			}
			return vReturn;
		}
	}

	static class RandomStart implements Supplier<CartesianVector>
	{
		private final Lattice mLattice;

		RandomStart(Lattice pLattice)
		{
			mLattice = pLattice;
		}

		@Override
		public CartesianVector get()
		{
			double vX = MathUtil.kRng.nextInt(mLattice.xDimension())
					+ MathUtil.kRng.nextDouble();
			double vY = MathUtil.kRng.nextInt(mLattice.yDimension())
					+ MathUtil.kRng.nextDouble();
			return CartesianVector.of(vX, vY, 0.0);
		}
	}

	static class RandomDirectionSupplier implements Supplier<CartesianVector>
	{
		@Override
		public CartesianVector get()
		{
			return randomUnitVector();
		}
	}

	static class UniformDirectionSupplier implements Supplier<CartesianVector>
	{
		private final CartesianVector mDirection;

		private UniformDirectionSupplier(CartesianVector pDirection)
		{
			mDirection = pDirection;
		}

		@Override
		public CartesianVector get()
		{
			return mDirection;
		}
	}
}
