package willey.lib.math.linearalgebra;

import static willey.lib.math.MathUtil.equal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import willey.lib.math.MathUtil;
import willey.lib.util.Check;

public class CartesianVector implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private static final double kEqualTolerance = 1e-14;
	private static final CartesianVector kZeroVector = new CartesianVector(0,
			0, 0);

	private final double mX;
	private final double mY;
	private final double mZ;
	
	private final double[] mCoordinates = new double[3];

	private double mMagnitude = -1;
	
	private CartesianVector mUnitVector = null;

	public static CartesianVector of(double pX, double pY, double pZ)
	{
		return new CartesianVector(pX, pY, pZ);
	}
	
	public static CartesianVector of(double[] pCoordinates)
	{
		return new CartesianVector(pCoordinates);
	}

	private CartesianVector(double pX, double pY, double pZ)
	{
		mX = pX;
		mY = pY;
		mZ = pZ;
		mCoordinates[0] = mX;
		mCoordinates[1] = mY;
		mCoordinates[2] = mZ;
	}
	
	private CartesianVector(double[] pCoordinates)
	{
		this(pCoordinates[0], pCoordinates[1], pCoordinates[2]);
	}
	
	public double[] getCoordinates()
	{
		return mCoordinates;
	}
	
	public double getCoordinate(int pIndex)
	{
		return mCoordinates[pIndex];
	}
	
	public static CartesianVector normal(int pIndex)
	{
		double[] vCoord = new double[3];
		vCoord[pIndex] = 1.0;
		return new CartesianVector(vCoord);
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
		if (mUnitVector == null)
		{
			mUnitVector = scale(1 / magnitude());
		}
		return mUnitVector;
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

	private static boolean equalCoor(double p1, double p2)
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
		CartesianVector vNormal = pNormal.unitVector();
		return this.subtract(vNormal.scale(vNormal.dotProduct(this)));
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
		return StreamSupport
				.doubleStream(Spliterators.spliterator(mCoordinates, 0), false)
				.boxed().map(pD -> pD.toString()).collect(Collectors.joining(", ", "(", ")"));
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
	
	public static BinaryOperator<CartesianVector> vectorSum()
	{
		return (p1, p2) -> p1.add(p2);
	}
	
	public static Collector<CartesianVector, List<CartesianVector>, CartesianVector> averageVector(CartesianVector pCenter)
	{
		return new AverageVectorCollector(pCenter);
	}
	
	public static Collector<CartesianVector, List<CartesianVector>, CartesianVector> averageVector()
	{
		return new AverageVectorCollector(zeroVector());
	}
	
	private static class AverageVectorCollector implements Collector<CartesianVector, List<CartesianVector>, CartesianVector>
	{
		private final CartesianVector mCenter;
		
		public AverageVectorCollector(CartesianVector pCenter)
		{
			mCenter = pCenter;
		}
		
		@Override
		public Supplier<List<CartesianVector>> supplier()
		{
			return () -> new ArrayList<>();
		}

		@Override
		public BiConsumer<List<CartesianVector>, CartesianVector> accumulator()
		{
			return (p1, p2) -> p1.add(p2.unitVector());
		}

		@Override
		public BinaryOperator<List<CartesianVector>> combiner()
		{
			return (p1, p2) -> {p1.addAll(p2); return p1;};
		}

		@Override
		public Function<List<CartesianVector>, CartesianVector> finisher()
		{
			return p1 -> MomentOfInertiaTensor.get(p1).usingCenterOfMass(mCenter).dominantDirection();
		}

		@Override
		public Set<java.util.stream.Collector.Characteristics> characteristics()
		{
			Set<Characteristics> vChar = new HashSet<Collector.Characteristics>();
			vChar.add(Characteristics.CONCURRENT);
			vChar.add(Characteristics.UNORDERED);
			return vChar;
		}
		
	}
}
