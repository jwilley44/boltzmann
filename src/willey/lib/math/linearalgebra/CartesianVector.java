package willey.lib.math.linearalgebra;

import static willey.lib.math.MathUtil.equal;

import java.util.function.Consumer;

import willey.lib.math.MathUtil;
import willey.lib.util.Check;
import willey.lib.util.StringJoiner;

public class CartesianVector
{
	private static final double kEqualTolerance = 1e-14;
	private static final CartesianVector kZeroVector = new CartesianVector(0,
			0, 0);

	private final double mX;
	private final double mY;
	private final double mZ;

	private double mMagnitude = -1;

	public static CartesianVector of(double pX, double pY, double pZ)
	{
		return new CartesianVector(pX, pY, pZ);
	}

	private CartesianVector(double pX, double pY, double pZ)
	{
		mX = pX;
		mY = pY;
		mZ = pZ;
	}

	public double x()
	{
		return mX;
	}

	public double y()
	{
		return mY;
	}

	public double z()
	{
		return mZ;
	}

	public double distance(CartesianVector pVector)
	{
		return subtract(pVector).magnitude();
	}

	public CartesianVector scale(double pScalar)
	{
		return new CartesianVector(mX * pScalar, mY * pScalar, mZ * pScalar);
	}

	public CartesianVector add(CartesianVector pVector)
	{
		return new CartesianVector(mX + pVector.mX, mY + pVector.mY, mZ
				+ pVector.mZ);
	}

	/**
	 * 
	 * @param pVector
	 * @return a vector pointing from pVector TO this vector
	 */
	public CartesianVector subtract(CartesianVector pVector)
	{
		return add(pVector.scale(-1));
	}

	public double dotProduct(CartesianVector pVector)
	{
		return mX * pVector.mX + mY * pVector.mY + mZ * pVector.mZ;
	}

	public CartesianVector randomRotaionInPlane(CartesianVector pVector)
	{
		return unitVector().rotateAboutThis(pVector, MathUtil.nextRandomBetween(0, 2*Math.PI));
	}
	
	public CartesianVector rotateAboutThis(CartesianVector pVector, double pRadians)
	{
		Check.kIllegalState.checkTrue(Math.abs(magnitude()  - 1) < 1e-6, "The axis must be a unit vector");
		double vCos = Math.cos(pRadians);
		double v1MinusCos = 1 - vCos;
		double vSin = Math.sin(pRadians);
		double vXY = x()*y()*v1MinusCos;
		double vXZ = x()*z()*v1MinusCos;
		double vYZ = y()*z()*v1MinusCos;
		double vXSin = x()*vSin;
		double vYSin = y()*vSin;
		double vZSin = z()*vSin;
		double vX = (vCos  + x()*x()*v1MinusCos)*pVector.x() + (vXY - vZSin)*pVector.y() + (vXZ  + vYSin)*pVector.z();
		double vY = (vXY + vZSin)*pVector.x() + (vCos  + y()*y()*v1MinusCos)*pVector.y() + (vYZ - vXSin)*pVector.z();
		double vZ = (vXZ - vYSin)*pVector.x() + (vYZ + vXSin)*pVector.y() + (vCos  + z()*z()*v1MinusCos)*pVector.z();
		return of(vX, vY, vZ).unitVector();
	}

	/**
	 * Small Rotation of a vector.
	 * 
	 * @param pFractionOfMagnitude
	 *            fraction of the magnitude to rotate the the vector.
	 * @return
	 */
	public CartesianVector randomSmallRotation(double pFractionOfMagnitude)
	{
		if (pFractionOfMagnitude == 0.0)
			return this;
		double vHeight = magnitude() - pFractionOfMagnitude * magnitude();
		if (pFractionOfMagnitude > 1 && pFractionOfMagnitude < 0)
			throw new IllegalArgumentException(
					"Rotation height must be positive and cannot be greater than the magnitude of the vector being rotated");
		double vRadiusOfRotation = Math.sqrt(magnitude() * magnitude()
				- vHeight * vHeight);
		CartesianVector vPerp = getPerpendicularVector().unitVector().scale(
				vRadiusOfRotation);
		return unitVector().scale(vHeight).add(vPerp);
	}

	public double magnitude()
	{
		if (mMagnitude == -1)
		{
			mMagnitude = Math.sqrt(dotProduct(this));
		}
		return mMagnitude;
	}

	public CartesianVector unitVector()
	{
		return scale(1 / magnitude());
	}

	public CartesianVector crossProduct(CartesianVector pVector)
	{
		double vX = mY * pVector.mZ - mZ * pVector.mY;
		double vY = mZ * pVector.mX - mX * pVector.mZ;
		double vZ = mX * pVector.mY - mY * pVector.mX;
		return new CartesianVector(vX, vY, vZ);
	}

	public boolean isZeroVector()
	{
		return this.coordinatesEqual(kZeroVector);
	}

	public double cosTheta(CartesianVector pVector)
	{
		return dotProduct(pVector) / (magnitude() * pVector.magnitude());
	}

	public double sinTheta(CartesianVector pVector)
	{
		return crossProduct(pVector).magnitude()
				/ (magnitude() * pVector.magnitude());
	}

	/**
	 * Checks if two vectors are equal within a default tolerance.
	 * 
	 * @param pVector
	 * @return
	 */
	public boolean coordinatesEqual(CartesianVector pVector)
	{
		return equalCoor(mX, pVector.x()) && equalCoor(mY, pVector.y())
				&& equalCoor(mZ, pVector.z());
	}

	private boolean equalCoor(double p1, double p2)
	{
		return equal(p1, p2, kEqualTolerance);
	}
	
	public boolean isPerpendicular(CartesianVector pVector)
	{
		return equal(0.0, dotProduct(pVector), kEqualTolerance);
	}

	public CartesianVector getPerpendicularVector()
	{
		if (isZeroVector())
			throw new IllegalArgumentException(
					"Cannot have a vector perpendicular to the zero vector");
		CartesianVector vPerpendicular = crossProduct(randomVector());
		while (vPerpendicular.isZeroVector())
		{
			vPerpendicular = crossProduct(randomVector());
		}
		return vPerpendicular;
	}
	
	/**
	 * Projects this vector onto a plane defined by the normal vector
	 * @param pNormal
	 * @return
	 */
	public CartesianVector projectOntoPlane(CartesianVector pNormal)
	{
		CartesianVector vNorm = pNormal.unitVector();
		CartesianVector vA = vNorm.crossProduct(this.crossProduct(vNorm));
		return vA.scale(vA.dotProduct(this) / vA.magnitude());
	}
	
	public CartesianPlane getNullSpace()
	{
		CartesianVector v1 = getPerpendicularVector();
		CartesianVector v2 = v1.crossProduct(this);
		return new CartesianPlane(v1.unitVector(), v2.unitVector());
	}
	
	public static CartesianVector fromString(String pVector)
	{
		String[] vCoordinates = pVector.substring(1, pVector.length() - 1).split(", ");
		return of(Double.parseDouble(vCoordinates[0].trim()), Double.parseDouble(vCoordinates[1].trim()), Double.parseDouble(vCoordinates[2].trim()));
	}

	@Override
	public String toString()
	{
		StringBuilder vString = new StringBuilder();
		vString.append("(");
		String vCoords = StringJoiner.join(", ", mX, mY, mZ);
		vString.append(vCoords);
		vString.append(")");
		return vString.toString();
	}

	public static CartesianVector randomVector()
	{
		return new CartesianVector(MathUtil.nextRandomBetween(-1, 1),
				MathUtil.nextRandomBetween(-1, 1), MathUtil.nextRandomBetween(
						-1, 1));
	}

	public static CartesianVector randomUnitVector()
	{
		return randomVector().unitVector();
	}

	public static CartesianVector zeroVector()
	{
		return kZeroVector;
	}
	
	public static class VectorSum implements Consumer<CartesianVector>
	{
		private CartesianVector mVector = zeroVector();

		@Override
		public void accept(CartesianVector pVector)
		{
			mVector = mVector.add(pVector);
		}
		
		public CartesianVector getSum()
		{
			return mVector;
		}
	}
}
