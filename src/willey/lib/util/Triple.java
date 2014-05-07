package willey.lib.util;

public class Triple<A, B, C>
{
	private final A mA;
	private final B mB;
	private final C mC;
	
	public Triple(A pA, B pB, C pC)
	{
		mA = pA;
		mB = pB;
		mC = pC;
	}
	
	public A getA()
	{
		return mA;
	}
	
	public B getB()
	{
		return mB;
	}
	
	public C getC()
	{
		return mC;
	}
	
	public static <A,B,C> Triple<A,B,C> of(A pA, B pB, C pC)
	{
		return new Triple<A,B,C>(pA, pB, pC);
	}
	
	@Override
	public String toString()
	{
		return "<" + mA.toString() + "," + mB.toString() + mC.toString() + ">";
	}
}
