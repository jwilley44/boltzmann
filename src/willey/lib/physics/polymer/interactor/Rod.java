package willey.lib.physics.polymer.interactor;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.LineSegment;

public class Rod implements Interactor
{
	private final LineSegment mLineSegment;
	private final double mRadius;

	public Rod(CartesianVector pDirection, CartesianVector pPosition,
			double pLength, double pRadius)
	{
		mLineSegment = LineSegment.get(pPosition,
				pPosition.add(pDirection.unitVector().scale(pLength)));
		mRadius = pRadius;
	}

	Rod(LineSegment pLineSegment, double pRadius)
	{
		mLineSegment = pLineSegment;
		mRadius = pRadius;
	}

	public double volume()
	{
		return Math.PI * mRadius * mRadius
				* (mLineSegment.length() + mRadius * 4 / 3);
	}

	Rod move(LineSegment pNewLineSegment)
	{
		return new Rod(pNewLineSegment, mRadius);
	}

	public CartesianVector direction()
	{
		return mLineSegment.direction();
	}

	public CartesianVector position()
	{
		return mLineSegment.getPoint(0.5);
	}

	public CartesianVector endPoint()
	{
		return mLineSegment.end();
	}

	public double length()
	{
		return mLineSegment.length();
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
				pNewPosition.add(mLineSegment.direction())), mRadius);
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
	
	public static double volume(double pRadius, double pLength)
	{
		return Math.PI * pRadius * pRadius
				* (pLength + pRadius * 4 / 3);
	}
}
