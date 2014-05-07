package willey.lib.math.linearalgebra;

import willey.lib.math.MathUtil;

public class CartesianPlane
{
	private final CartesianVector mColumn1;
	private final CartesianVector mColumn2;
	
	public CartesianPlane(CartesianVector p1, CartesianVector p2)
	{
		mColumn1 = p1;
		mColumn2 = p2;
	}
	
	public CartesianVector getV1()
	{
		return mColumn1;
	}
	
	public CartesianVector getV2()
	{
		return mColumn2;
	}
	
	public CartesianVector multiply(CartesianVector pVector)
	{
		return CartesianVector.of(pVector.dotProduct(mColumn1), pVector.dotProduct(mColumn2), 0);
	}
	
	public CartesianVector randomUnitVector()
	{
		return mColumn1.scale(MathUtil.kRng.nextDouble()).add(mColumn2.scale(MathUtil.kRng.nextDouble())).unitVector();
	}
	
	@Override
	public String toString()
	{
		return mColumn1.toString() + "\n" + mColumn2.toString();
	}
	
	public static CartesianPlane randomPlane()
	{
		return new CartesianPlane(CartesianVector.randomUnitVector(), CartesianVector.randomUnitVector());
	}
}
