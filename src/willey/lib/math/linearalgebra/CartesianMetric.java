package willey.lib.math.linearalgebra;

import willey.lib.math.geometry.Metric;

public class CartesianMetric implements Metric<CartesianVector>
{

	@Override
	public double compute(CartesianVector pPoint1, CartesianVector pPoint2)
	{
		return pPoint1.distance(pPoint2);
	}

}
