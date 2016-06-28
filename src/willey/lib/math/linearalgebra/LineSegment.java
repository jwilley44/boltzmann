package willey.lib.math.linearalgebra;

import static willey.lib.math.linearalgebra.CartesianVector.of;

import java.io.Serializable;

public class LineSegment implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final CartesianVector mStart;
	private final CartesianVector mEnd;
	private final CartesianVector mDirection;
	private final double mLength;

	public LineSegment(CartesianVector pStart, CartesianVector pEnd)
	{
		mStart = pStart;
		mEnd = pEnd;
		mDirection = mEnd.subtract(mStart);
		mLength = mDirection.magnitude();
	}

	private LineSegment(CartesianVector pStart, CartesianVector pEnd,
			CartesianVector pDirection, double pLength)
	{
		mStart = pStart;
		mEnd = pEnd;
		mDirection = pDirection;
		mLength = pLength;
	}

	public double length()
	{
		return mLength;
	}

	public CartesianVector start()
	{
		return mStart;
	}

	public CartesianVector end()
	{
		return mEnd;
	}

	public CartesianVector direction()
	{
		return mDirection;
	}

	public CartesianVector getPoint(double pScale)
	{
		return start().add(end().subtract(start()).scale(pScale));
	}

	public LineSegment translate(CartesianVector pVector)
	{
		return new LineSegment(start().add(pVector), end().add(pVector));
	}
	
	public LineSegment rotate(double pRotationFraction)
	{
		CartesianVector vNewDirection = direction().unitVector()
				.randomSmallRotation(pRotationFraction);
		return changeDirectionThroughMiddle(vNewDirection);
	}

	public LineSegment changeDirectionThroughMiddle(
			CartesianVector pNewDirection)
	{
		CartesianVector vNewDirection = pNewDirection.unitVector().scale(
				length());
		CartesianVector vMidPoint = getPoint(0.5);
		return new LineSegment(vMidPoint.add(vNewDirection.scale(-0.5)),
				vMidPoint.add(vNewDirection.scale(0.5)), vNewDirection,
				length());
	}
	
	public CartesianVector getClosestPointToSegment(LineSegment pLineSegment)
	{
		return direction().crossProduct(pLineSegment.direction())
		.isZeroVector() ? closestPointParallel(pLineSegment)
		: closestPointNonParallel(pLineSegment);
	}

	public double minimumDistance(LineSegment pLineSegment)
	{
		return direction().crossProduct(pLineSegment.direction())
				.isZeroVector() ? minimumDistanceParallel(pLineSegment)
				: minimumDistanceNonParallel(pLineSegment);
	}

	private double minimumDistanceNonParallel(LineSegment pLineSegment)
	{
		double vDistance1 = pLineSegment.minimumDistance(closestPoint(pLineSegment));
		double vDistance2 = minimumDistance(pLineSegment
				.closestPoint(this));
		return Math.min(vDistance1, vDistance2);
	}

	private double minimumDistanceParallel(LineSegment pLineSegment)
	{
		double vDistance1 = start().distance(pLineSegment.start());
		double vDistance2 = end().distance(pLineSegment.start());
		return vDistance1 < vDistance2 ? minimumDistance(pLineSegment.start())
				: minimumDistance(pLineSegment.end());
	}
	
	private CartesianVector closestPointParallel(LineSegment pLineSegment)
	{
		double vMinDistance;
		CartesianVector vClosest;
		double vDistance1 = start().distance(pLineSegment.start());
		double vDistance2 = end().distance(pLineSegment.start());
		if (vDistance1 < vDistance2)
		{
			vMinDistance=vDistance1;
			vClosest = start();
		}
		else
		{
			vMinDistance = vDistance2;
			vClosest = end();
		}
		double vDistance3 = start().distance(pLineSegment.end());
		if (vDistance3 < vMinDistance)
		{
			vMinDistance = vDistance3;
			vClosest = start();
		}
		double vDistance4 = end().distance(pLineSegment.end());
		if (vDistance4 < vMinDistance)
		{
			vClosest = end();
		}
		return vClosest;
	}
	
	public CartesianVector closestPoint(LineSegment pLineSegment)
	{
		return direction().crossProduct(pLineSegment.direction())
				.isZeroVector() ? closestPointParallel(pLineSegment)
				: closestPointNonParallel(pLineSegment);
	}
	
	private CartesianVector closestPointNonParallel(LineSegment pLineSegment)
	{
		
		CartesianPlane vNullSpace = pLineSegment.direction().getNullSpace();
		
		CartesianVector vStartProj = vNullSpace.multiply(start()
				.subtract(pLineSegment.start()));
		CartesianVector vEndProj = vNullSpace.multiply(end()
				.subtract(pLineSegment.start()));
		LineSegment vProjected = new LineSegment(vStartProj, vEndProj);
		CartesianVector vDirection = vProjected.direction();
		CartesianVector vNorm = get2DUnitNorm(vDirection);
		double vDistance = vNorm.dotProduct(vStartProj);
		double vP = vDirection.dotProduct(vNorm.scale(vDistance).subtract(
				vStartProj))
				/ vDirection.dotProduct(vDirection);
		vP = vP < 0 ? 0 : vP > 1 ? 1 : vP;
		return getPoint(vP);
	}
	
//	private CartesianVector closestPoint(CartesianVector pPoint)
//	{
//		CartesianVector vToPoint = start().subtract(pPoint);
//		CartesianVector vNorm = vToPoint.crossProduct(direction());
//		CartesianVector vClosest;
//		if (vNorm.isZeroVector())
//		{
//			double vStartDist = vToPoint.magnitude();
//			double vEndDist = end().distance(pPoint);
//			vClosest = vStartDist < length() && vEndDist < length() ? pPoint
//					: vStartDist < vEndDist ? start() : end();
//		}
//		else
//		{
//			CartesianVector vUnitNorm = vNorm.crossProduct(direction())
//					.unitVector();
//			double vDistanceToInf = vToPoint.dotProduct(vUnitNorm);
//			double vT = direction().dotProduct(
//					pPoint.add(vUnitNorm.scale(vDistanceToInf)).subtract(
//							start()))
//					/ direction().dotProduct(direction());
//			vT = vT < 0 ? 0 : vT > 1 ? 1 : vT;
//			vClosest = getPoint(vT);
//		}
//		return vClosest;
//	}
	
	public double minimumDistance(CartesianVector pPoint)
	{
//		CartesianVector vToPoint = start().subtract(pPoint);
//		CartesianVector vNorm = vToPoint.crossProduct(direction());
//		double vDistance = -1;
//		if (vNorm.isZeroVector())
//		{
//			double vStartDist = vToPoint.magnitude();
//			double vEndDist = end().distance(pPoint);
//			vDistance = vStartDist < length() && vEndDist < length() ? 0.0
//					: Math.min(vStartDist, vEndDist);
//		}
//		else
//		{
//			CartesianVector vUnitNorm = vNorm.crossProduct(direction())
//					.unitVector();
//			double vDistanceToInf = vToPoint.dotProduct(vUnitNorm);
//			double vT = direction().dotProduct(
//					pPoint.add(vUnitNorm.scale(vDistanceToInf)).subtract(
//							start()))
//					/ direction().dotProduct(direction());
//			vT = vT < 0 ? 0 : vT > 1 ? 1 : vT;
//			vDistance = getPoint(vT).distance(pPoint);
//		}
//		return vDistance;
		
		CartesianVector vFromPoint = pPoint.subtract(start());
		double vT = vFromPoint.dotProduct(direction())/(length()*length());
		return getPoint(vT < 0 ? 0 : vT > 1 ? 1 : vT).distance(pPoint);
	}
	
	private static CartesianVector get2DUnitNorm(CartesianVector p2DVector)
	{
		return of(p2DVector.y(), -1 * p2DVector.x(), 0).unitVector();
	}

	public static LineSegment get(CartesianVector pStart, CartesianVector pEnd)
	{
		return new LineSegment(pStart, pEnd);
	}
	
	public boolean segmentsEqual(LineSegment pLineSegment)
	{
		return pLineSegment.mDirection.coordinatesEqual(mDirection) &&
				pLineSegment.mEnd.coordinatesEqual(mEnd) &&
				pLineSegment.mStart.coordinatesEqual(mStart);
	}
}
