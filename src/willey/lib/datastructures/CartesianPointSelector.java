package willey.lib.datastructures;

import willey.lib.datastructures.KdTree.PointSelector;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.LineSegment;

public class CartesianPointSelector implements PointSelector<CartesianVector>
{

	@Override
	public int chooseSide(CartesianVector pPoint, CartesianVector pInsertPoint,
			int pAxis)
	{
		double vPoint = pPoint.getCoordinate(pAxis);
		double vInsertPoint = pInsertPoint.getCoordinate(pAxis);
		return Double.compare(vPoint, vInsertPoint);
	}

	@Override
	public int chooseAxis(int pDepth)
	{
		return pDepth % 2;
	}

	@Override
	public  CartesianVector splitPoint(
			CartesianVector pStart, CartesianVector pEnd,
			CartesianVector pPlane, int pAxis)
	{
		CartesianVector vNormal = CartesianVector.normal(pAxis);
		double vT = vNormal.dotProduct(pPlane.subtract(pStart)) / vNormal.dotProduct(pStart.subtract(pEnd));
		if (vT < 0)
		{
			vT = vNormal.dotProduct(pPlane.subtract(pEnd)) / vNormal.dotProduct(pStart.subtract(pEnd));
		}
		return new LineSegment(pStart, pEnd).getPoint(Math.abs(vT));
	}

	@Override
	public int dimension()
	{
		return 3;
	}

}
