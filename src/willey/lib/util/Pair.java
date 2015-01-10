package willey.lib.util;


public class Pair<A,B>
{
	private final A mA;
	private final B mB;
	
	public Pair(A pA, B pB)
	{
		mA = pA;
		mB = pB;
	}
	
	public A getA()
	{
		return mA;
	}
	
	public B getB()
	{
		return mB;
	}
	
	public static <A,B> Pair<A,B> of(A pA, B pB)
	{
		return new Pair<A,B>(pA, pB);
	}
	
	@Override
	public int hashCode()
	{
		return 31 + 7*getA().hashCode() + 11*getB().hashCode();
	}
	
	@Override
	public boolean equals(Object pObject)
	{
		boolean vEquals = pObject instanceof Pair;
		if (vEquals)
		{
			Pair<?,?> vPair = (Pair<?,?>)pObject;
			vEquals = getA().equals(vPair.getA()) && getB().equals(vPair.getB());
		}
		return vEquals;
	}
	
	@Override
	public String toString()
	{
		return "<" + mA.toString() + "," + mB.toString() + ">";
	}
	
	boolean unorderedEqual(Pair<B, A> pPair)
	{
		return getA().equals(pPair.getB()) && getB().equals(pPair.getA());
	}
}
