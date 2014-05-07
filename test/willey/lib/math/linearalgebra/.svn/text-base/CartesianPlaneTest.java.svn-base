package willey.lib.math.linearalgebra;

import junit.framework.Assert;

import org.junit.Test;

import willey.lib.test.AbstractTest;

public class CartesianPlaneTest extends AbstractTest
{
	@Test
	public void testRandom()
	{
		CartesianPlane vPlane = CartesianPlane.randomPlane();
		CartesianVector vRandomVector = vPlane.randomUnitVector();
		assertEquals(1.0, vRandomVector.magnitude(), 0.0000001);
		Assert.assertTrue(vRandomVector.crossProduct(vPlane.getV1())
				.crossProduct(vRandomVector.crossProduct(vPlane.getV2()))
				.isZeroVector());
	}
}
