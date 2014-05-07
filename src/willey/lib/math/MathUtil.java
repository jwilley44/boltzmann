package willey.lib.math;

import java.util.List;
import java.util.Random;

public class MathUtil
{
	public static final Random kRng = new Random();
	
	private MathUtil()
	{
		
	}
	
	synchronized public static boolean equal(double p1, double p2, double pTolerence)
	{
		return Math.abs(p1 - p2) < pTolerence;
	}
	
	synchronized public static double nextRandomBetween(double pLower, double pUpper)
	{
		if (pLower >= pUpper) throw new IllegalArgumentException("Invalid interval.");
		double vRange = pUpper - pLower;
		return kRng.nextDouble()*vRange + pLower;
	}
	
	public static double sum(List<? extends Number> pNumbers)
	{
		double vSum = 0.0;
		for (Number vElement : pNumbers)
		{
			vSum += vElement.doubleValue();
		}
		return vSum;
	}
}
