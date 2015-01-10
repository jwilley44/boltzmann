package willey.lib.physics.polymer.lattice;

import static willey.lib.math.linearalgebra.CartesianVector.of;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;

public class Lattice
{
	private final List<Function<CartesianVector, CartesianVector>> mLatticeMovements = new ArrayList<Function<CartesianVector, CartesianVector>>();
	private final int mXDimension;
	private final int mYDimension;
	private final int mZDimension;

	public static Lattice cubeLattice(int pDimension)
	{
		return new Lattice(pDimension, pDimension, pDimension);
	}

	public static Lattice rectangleLattice(int pXDimension, int pYDimension,
			int pZDimension)
	{
		return new Lattice(pXDimension, pYDimension, pZDimension);
	}

	public Lattice copy()
	{
		return new Lattice(xDimension(), yDimension(), zDimension());
	}

	public int xDimension()
	{
		return mXDimension;
	}

	public int yDimension()
	{
		return mYDimension;
	}

	public int zDimension()
	{
		return mZDimension;
	}

	public long volume()
	{
		return mXDimension * mYDimension * mZDimension;
	}

	private Lattice(int pXDimension, int pYDimension, int pZDimension)
	{
		mXDimension = pXDimension;
		mYDimension = pYDimension;
		mZDimension = pZDimension;

		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				for (int k = -1; k <= 1; k++)
				{
					CartesianVector vRepositionVector = CartesianVector.of(i
							* mXDimension, j * mYDimension, k * mZDimension);
					Function<CartesianVector, CartesianVector> vRespostionFunction = (
							pVector) -> pVector.add(vRepositionVector);
					mLatticeMovements.add(vRespostionFunction);
				}
			}
		}
	}

	public CartesianVector centerStart()
	{
		double vX = 0.5 * mXDimension;
		double vY = 0.5 * mYDimension;
		double vZ = 0.5 * mZDimension;
		return CartesianVector.of(vX, vY, vZ);
	}

	public CartesianVector randomStart()
	{
		double vX = MathUtil.kRng.nextInt(mXDimension)
				+ MathUtil.kRng.nextDouble();
		double vY = MathUtil.kRng.nextInt(mYDimension)
				+ MathUtil.kRng.nextDouble();
		double vZ = MathUtil.kRng.nextInt(mZDimension)
				+ MathUtil.kRng.nextDouble();
		return CartesianVector.of(vX, vY, vZ);
	}

	CartesianVector getInNewLattice(CartesianVector pVector,
			Function<LatticeCube, LatticeCube> pCubeMovement)
	{
		return pCubeMovement.apply(getCube(pVector)).add(getRelative(pVector));
	}

	public Collection<Function<CartesianVector, CartesianVector>> getNewLatticeCoordinatesFunctions()
	{
		return mLatticeMovements;
	}

	private CartesianVector floor(CartesianVector pVector)
	{
		return of(Math.floor(pVector.x()), Math.floor(pVector.y()),
				Math.floor(pVector.z()));
	}

	private CartesianVector getRelative(CartesianVector pCoordinates)
	{
		return pCoordinates.subtract(floor(pCoordinates));
	}

	public CartesianVector projectIntoLattice(CartesianVector pVector)
	{
		return CartesianVector.of(project(pVector.x(), mXDimension),
				project(pVector.y(), mYDimension),
				project(pVector.z(), mZDimension));
	}

	private double project(double pX, long pDimension)
	{
		return Math.signum(pX) < 0 ? mXDimension - Math.abs(pX % pDimension)
				: pX % pDimension;
	}

	private long xMod(long pArgument)
	{
		return pArgument >= 0 ? pArgument % mXDimension
				: (mXDimension + (pArgument % mXDimension)) % mXDimension;
	}

	private long yMod(long pArgument)
	{
		return pArgument >= 0 ? pArgument % mYDimension
				: (mYDimension + (pArgument % mYDimension)) % mYDimension;
	}

	private long zMod(long pArgument)
	{
		return pArgument >= 0 ? pArgument % mZDimension
				: (mZDimension + (pArgument % mZDimension)) % mZDimension;
	}

	private LatticeCube getCube(CartesianVector pVector)
	{
		CartesianVector vFloor = floor(pVector);
		return new LatticeCube(xMod((long) vFloor.x()),
				yMod((long) vFloor.y()), zMod((long) vFloor.z()));
	}
}
