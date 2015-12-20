package willey.lib.datastructures;

import willey.lib.datastructures.KdTree.PointSelector;
import willey.lib.datastructures.KdTree.Side;
import willey.lib.math.linearalgebra.CartesianVector;

public class CartesianPointSelector implements PointSelector<CartesianVector>
{

	@Override
	public Side		 chooseSide(CartesianVector pPoint, CartesianVector pInsertPoint,
			int pAxis)
	{
		double vPoint = pPoint.getCoordinate(pAxis);
		double vInsertPoint = pInsertPoint.getCoordinate(pAxis);
		return Side.convert(Double.compare(vPoint, vInsertPoint));
	}

	@Override
	public int chooseAxis(int pDepth)
	{
		return pDepth % 2;
	}

	@Override
	public boolean contains(CartesianVector pTest, CartesianVector pEnd1,
			CartesianVector pEnd2, int pAxis)
	{
		double vTest = pTest.getCoordinate(pAxis);
		double vEnd1 = pEnd1.getCoordinate(pAxis);
		double vEnd2 = pEnd2.getCoordinate(pAxis);
		double vMax = Math.max(vEnd1, vEnd2);
		double vMin = Math.min(vEnd1, vEnd2);
		return vTest <= vMax && vTest >= vMin;
	}

	@Override
	public int dimension()
	{
		return 3;
	}
}
