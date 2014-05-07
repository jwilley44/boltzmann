package willey.incubator;

public class DoubleVector
{
	private final double[] mData;
	
	public double get(int pIndex)
	{
		return mData[pIndex];
	}
	
	private DoubleVector(boolean pInherit, double[] pData)
	{
		mData = pInherit ? pData : pData.clone();
	}
	
	public static DoubleVector of(boolean pInherit, double...pElements)
	{
		return new DoubleVector(pInherit, pElements);
	}
	
	public static DoubleVector of(double...pElements)
	{
		return new DoubleVector(false, pElements);
	}
	
}
