package willey.lib.test;

import org.junit.Assert;

public class AbstractTest
{
	protected static void assertEquals(double pExpected, double pActual, double pTolerance)
	{
		Assert.assertTrue("Expected <" + pExpected + "> was not within tolerance of actual <" + pActual + ">", Math.abs(pExpected - pActual) <= pTolerance);
	}
}
