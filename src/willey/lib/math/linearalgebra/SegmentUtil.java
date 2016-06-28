package willey.lib.math.linearalgebra;

import static willey.lib.math.linearalgebra.CartesianVector.of;

public class SegmentUtil
{
	public interface Segment
	{
		CartesianVector closestToThis(Segment pSegment);

		CartesianVector direction();

		CartesianVector start();

		CartesianVector end();

		double length();

		public default CartesianVector getPoint(double pScale)
		{
			pScale = pScale < 0 ? 0 : pScale > 1 ? 1 : pScale;
			return start().add(direction().scale(pScale));
		}

		public default double distance(Segment pSegment)
		{
			return closestToThis(pSegment).distance(
					pSegment.closestToThis(this));
		}
		
		public default Segment translate(CartesianVector pVector)
		{
			return get(start().add(pVector), end().add(pVector));
		}
		
		public default Segment rotate(double pRotationFraction)
		{
			CartesianVector vNewDirection = direction().unitVector()
					.randomSmallRotation(pRotationFraction);
			return changeDirectionThroughMiddle(vNewDirection);
		}

		default public Segment changeDirectionThroughMiddle(
				CartesianVector pNewDirection)
		{
			CartesianVector vNewDirection = pNewDirection.unitVector().scale(
					length());
			CartesianVector vMidPoint = getPoint(0.5);
			return get(vMidPoint.add(vNewDirection.scale(-0.5)),
					vMidPoint.add(vNewDirection.scale(0.5)));
		}
	}

	public static Segment get(CartesianVector pStart, CartesianVector pEnd)
	{
		return pStart.coordinatesEqual(pEnd) ? new PointSegment(pStart)
				: new RaySegment(pStart, pEnd);
	}

	private static class RaySegment implements Segment
	{
		private final CartesianVector mStart;
		private final CartesianVector mEnd;
		private final CartesianVector mDirection;
		private final double mLength;

		private RaySegment(CartesianVector pStart, CartesianVector pEnd)
		{
			mStart = pStart;
			mEnd = pEnd;
			mDirection = mEnd.subtract(mStart);
			mLength = mStart.distance(mEnd);
		}

		@Override
		public CartesianVector closestToThis(Segment pSegment)
		{
			return pSegment.start().coordinatesEqual(pSegment.end()) ? line2point(pSegment.start()) : line2line(pSegment);
		}
		
		private CartesianVector line2point(CartesianVector pPoint)
		{
			CartesianVector vFromPoint = pPoint.subtract(start());
			double vT = vFromPoint.dotProduct(direction())
					/ (length() * length());
			return getPoint(vT);
		}

		private CartesianVector line2line(Segment pSegment)
		{
			double vP;
			if(pSegment.direction().unitVector().subtract(direction().unitVector()).isZeroVector())
			{
				CartesianVector vTo = pSegment.start().subtract(start());
				vP = vTo.dotProduct(direction().unitVector())/length();
			}
			else
			{
				CartesianPlane vNullSpace = pSegment.direction().getNullSpace();

				CartesianVector vStartProj = vNullSpace.multiply(start()
						.subtract(pSegment.start()));
				CartesianVector vEndProj = vNullSpace.multiply(end().subtract(
						pSegment.start()));
				RaySegment vProjected = new RaySegment(vStartProj, vEndProj);
				CartesianVector vDirection = vProjected.direction();
				CartesianVector vNorm = get2DUnitNorm(vDirection);
				double vDistance = vNorm.dotProduct(vStartProj);
				vP = vDirection.dotProduct(vNorm.scale(vDistance).subtract(
						vStartProj))
						/ vDirection.dotProduct(vDirection);
			}
			return getPoint(vP);
		}

		@Override
		public CartesianVector direction()
		{
			return mDirection;
		}

		@Override
		public CartesianVector start()
		{
			return mStart;
		}

		@Override
		public CartesianVector end()
		{
			return mEnd;
		}

		@Override
		public double length()
		{
			return mLength;
		}

		private CartesianVector get2DUnitNorm(CartesianVector p2DVector)
		{
			return of(p2DVector.y(), -1 * p2DVector.x(), 0).unitVector();
		}
	}

	private static class PointSegment implements Segment
	{
		private final CartesianVector mPoint;

		private PointSegment(CartesianVector pPoint)
		{
			mPoint = pPoint;
		}

		@Override
		public CartesianVector closestToThis(Segment pSegment)
		{
			return mPoint;
		}

		@Override
		public CartesianVector direction()
		{
			return CartesianVector.zeroVector();
		}

		@Override
		public CartesianVector start()
		{
			return mPoint;
		}

		@Override
		public CartesianVector end()
		{
			return mPoint;
		}

		@Override
		public double length()
		{
			return 0;
		}
	}
}
