package thiel.two;

public class TriangleArrayPathElement
{
	private final int mValue;
	private final int mTotal;
	private final TriangleArrayPathElement mAbove;
	
	public TriangleArrayPathElement(int pValue)
	{
		mValue = pValue;
		mTotal = pValue;
		mAbove = null;
	}
	
	TriangleArrayPathElement(int pValue, TriangleArrayPathElement pAboveElement)
	{
		mValue = pValue;
		mAbove = pAboveElement;
		mTotal = mValue + mAbove.getTotal();
	}
	
	public int getValue()
	{
		return mValue;
	}
	
	public int getTotal()
	{
		return mTotal;
	}
	
	public TriangleArrayPathElement getAbove()
	{
		return mAbove;
	}
}
