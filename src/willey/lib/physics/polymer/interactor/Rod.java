package willey.lib.physics.polymer.interactor;

import java.io.Serializable;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.SegmentUtil;
import willey.lib.math.linearalgebra.SegmentUtil.Segment;

public class Rod implements Interactor, Serializable
{
	private static final long serialVersionUID = 1L;
	private final Segment mLineSegment;
	private final double mRadius;

	public Rod(CartesianVector pDirection, CartesianVector pPosition,
			double pLength, double pRadius)
	{
		mLineSegment = SegmentUtil.get(pPosition,
				pPosition.add(pDirection.unitVector().scale(pLength)));
		mRadius = pRadius;
	}

	Rod(Segment pLineSegment, double pRadius)
	{
		mLineSegment = pLineSegment;
		mRadius = pRadius;
	}

	public double volume()
	{
		return Math.PI * mRadius * mRadius
				* (mLineSegment.length() + mRadius * 4 / 3);
	}

	Rod move(Segment pNewLineSegment)
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

	public Rod reposition(CartesianVector pNewCenterPosition)
	{
		return new Rod(SegmentUtil.get(
				pNewCenterPosition.subtract(direction().scale(0.5)),
				pNewCenterPosition.add(direction().scale(0.5))), mRadius);
	}

	@Override
	public String toString()
	{
		return "{" + mLineSegment.start() + "/" + mLineSegment.end() + "/"
				+ mRadius + "}";
	}

	@Override
	public Segment getLineSegment()
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
