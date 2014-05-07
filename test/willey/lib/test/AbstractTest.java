package willey.lib.test;

import junit.framework.Assert;

public class AbstractTest
{
	protected void assertEquals(double pExpected, double pActual, double pTolerance)
	{
		Assert.assertTrue("Expected <" + pExpected + "> was not within tolerance of actual <" + pActual + ">", Math.abs(pExpected - pActual) <= pTolerance);
	}
}
