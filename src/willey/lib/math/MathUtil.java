package willey.lib.math;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

public class MathUtil
{
	private MathUtil()
	{
		
	}
	
	public static boolean equal(double p1, double p2, double pTolerence)
	{
		return Math.abs(p1 - p2) < pTolerence;
	}
	
	public static double nextRandomBetween(double pLower, double pUpper)
	{
		if (pLower >= pUpper) throw new IllegalArgumentException("Invalid interval.");
		double vRange = pUpper - pLower;
		return getThreadLocal().nextDouble()*vRange + pLower;
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
	
	public static DoubleStream sequence(double pStart, int pSteps, double pStepSize)
	{
		double[] vSequence = new double[pSteps];
		for (int i=0; i < pSteps; i++)
		{
			vSequence[i] = pStepSize * i + pStart;
		}
		return Arrays.stream(vSequence);
	}
	
	public static DoubleStream randomStream(int pCount, double pMin, double pMax)
	{
		double[] vStream = new double[pCount];
		for (int i = 0; i < pCount; i++)
		{
			vStream[i] = nextRandomBetween(pMin, pMax);
		}
		return Arrays.stream(vStream);
	}
	
	public static Random getThreadLocal()
	{
		return ThreadLocalRandom.current();
	}
	
	public static double pow(double pDouble, int pExponent)
	{
		double vReturn = 1;
		for (int i=0; i < pExponent; i++) vReturn *= pDouble;
		return vReturn;
	}
}
