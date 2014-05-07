package willey.lib.physics.polymer.lattice;

import static willey.lib.math.linearalgebra.CartesianVector.of;
import willey.lib.math.linearalgebra.CartesianVector;

public class LatticeCube
{
	private final long mX;
	private final long mY;
	private final long mZ;

	LatticeCube(long pX, long pY, long pZ)
	{
		mX = pX;
		mY = pY;
		mZ = pZ;
	}

	public long x()
	{
		return mX;
	}

	public long y()
	{
		return mY;
	}

	public long z()
	{
		return mZ;
	}

	public CartesianVector toVector()
	{
		return of(mX, mY, mZ);
	}

	public CartesianVector add(CartesianVector pVector)
	{
		return of(mX + pVector.x(), mY + pVector.y(), mZ + pVector.z());
	}

	public LatticeCube add(LatticeCube pCube)
	{
		return new LatticeCube(mX + pCube.x(), mY + pCube.y(), mZ + pCube.z());
	}

	public LatticeCube addX(int pDimension)
	{
		return new LatticeCube(mX + pDimension, mY, mZ);
	}

	public LatticeCube addY(int pDimension)
	{
		return new LatticeCube(mX, mY + pDimension, mZ);
	}

	public LatticeCube addZ(int pDimension)
	{
		return new LatticeCube(mX, mY, mZ + pDimension);
	}

	@Override
	public boolean equals(Object pObject)
	{
		boolean vEqual = pObject instanceof LatticeCube;
		if (vEqual)
		{
			LatticeCube vCube = (LatticeCube) pObject;
			vEqual = mX == vCube.mX && mY == vCube.mY && mZ == vCube.mZ;
		}
		return vEqual;
	}

	@Override
	public int hashCode()
	{
		return Long.valueOf(mX).hashCode() + Long.valueOf(mY).hashCode()
				+ Long.valueOf(mZ).hashCode();
	}
}
