package willey.lib.physics.polymer.interactor;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.LineSegment;

public class Rod implements Interactor
{
	private final LineSegment mLineSegment;
	private final CartesianVector mMidPoint;
	private final double mLength;
	private final double mRadius;
	
	private final double mTranslation;
	private final double mRotation;

	public Rod(CartesianVector pDirection, CartesianVector pPosition,
			double pLength, double pRadius, double pTranslation, double pRotation)
	{
		mLineSegment = LineSegment.get(pPosition,
				pPosition.add(pDirection.unitVector().scale(pLength)));
		mLength = pLength;
		mMidPoint = mLineSegment.getPoint(0.5);
		mRadius = pRadius;
		mTranslation = pTranslation;
		mRotation = pRotation;
	}

	private Rod(LineSegment pLineSegment, CartesianVector pMidPoint,
			double pLength, double pRadius, double pTranslation, double pRotation)
	{
		mLineSegment = pLineSegment;
		mMidPoint = pMidPoint;
		mLength = pLength;
		mRadius = pRadius;
		mTranslation = pTranslation;
		mRotation = pRotation;
	}

	public double volume()
	{
		return Math.PI * mRadius * mRadius * (mLength + mRadius * 4 / 3);
	}

	Rod move(double pTranslation, double pRotationPercentage)
	{
		LineSegment vNewLineSegment = mLineSegment.translate(
				CartesianVector.randomUnitVector().scale(pTranslation)).rotate(
				pRotationPercentage);
		return new Rod(vNewLineSegment, vNewLineSegment.getPoint(0.5), mLength,
				mRadius, mTranslation, mRotation);

	}
	
	public Rod randomMove()
	{
		return move(mTranslation, mRotation);
	}

	public CartesianVector direction()
	{
		return mLineSegment.direction();
	}

	public CartesianVector position()
	{
		return mLineSegment.start();
	}

	public CartesianVector endPoint()
	{
		return mLineSegment.end();
	}

	public double length()
	{
		return mLength;
	}

	public double radius()
	{
		return mRadius;
	}

	/**
	 * Defines a parametric equation that defines the direction of the Rod. If a
	 * value in the interval [0,1] is passed in, it gives a point that lies in
	 * the line segment that defines the rod.
	 * 
	 * @param pScale
	 * @return
	 */
	CartesianVector parametricEquation(double pScale)
	{
		return mLineSegment.getPoint(pScale);
	}

	public double minimumDistance(Rod pRod)
	{
		return mLineSegment.minimumDistance(pRod.mLineSegment);
	}

	public double minimumDistance(CartesianVector pPoint)
	{
		return mLineSegment.minimumDistance(pPoint);
	}

	public Rod reposition(CartesianVector pNewPosition)
	{
		return new Rod(LineSegment.get(pNewPosition,
				pNewPosition.add(mLineSegment.direction())), mMidPoint,
				mLength, mRadius, mTranslation, mRotation);
	}

	@Override
	public String toString()
	{
		return "{" + mLineSegment.start() + "/" + mLineSegment.end() + "/"
				+ mRadius + "}";
	}

	@Override
	public CartesianVector getNearestPoint(Interactor pInteractor)
	{
		return mLineSegment.closestPoint(pInteractor.getLineSegment());
	}

	@Override
	public double getDistance(CartesianVector pVector)
	{
		return mLineSegment.minimumDistance(pVector);
	}

	@Override
	public LineSegment getLineSegment()
	{
		return mLineSegment;
	}

	@Override
	public double interactionRadius()
	{
		return mRadius;
	}
}
