package willey.lib.math.linearalgebra;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.math.MathUtil;
import willey.lib.test.AbstractTest;
import static willey.lib.math.linearalgebra.CartesianVector.*;

public class CartesianVectorTest extends AbstractTest
{
	@Test
	public void testAdd()
	{
		CartesianVector v1 = of(1,0,1);
		Assert.assertTrue(of(2,0,2).coordinatesEqual(v1.add(v1)));
	}
	
	@Test
	public void testScale()
	{
		CartesianVector v1 = of(1,0,1);
		double vMagnitude = v1.magnitude();
		CartesianVector vScaled = v1.scale(2);
		Assert.assertTrue(of(2,0,2).coordinatesEqual(vScaled));
		Assert.assertTrue(vMagnitude*2 == vScaled.magnitude());
	}
	
	@Test
	public void testCosine()
	{
		CartesianVector v1 = of(1, 0, 0);
		assertEquals(1.0, v1.cosTheta(v1), 1e-6);
	}
	
	@Test
	public void testSubtract() throws Exception
	{
		CartesianVector v1 = of(1,0,1);
		CartesianVector v2 = of(2,4,1);
		Assert.assertTrue(of(-1,-4,0).coordinatesEqual(v1.subtract(v2)));
	}
	
	@Test
	public void testMagnitude()
	{
		CartesianVector v1 = of(1,0,1);
		Assert.assertTrue(Math.abs(v1.magnitude() - Math.sqrt(2)) < 0.00001);
	}
	
	@Test
	public void testDotProduct()
	{
		Assert.assertTrue(of(1,2,3).dotProduct(of(4,5,6)) == 32);
	}
	
	@Test
	public void testRandomUnitVector()
	{
		assertEquals(1.0, randomUnitVector().magnitude(), 0.00000001);
		Assert.assertFalse(randomUnitVector().coordinatesEqual(randomUnitVector()));
	}
	
	@Test
	public void testCrossProduct()
	{
		CartesianVector v1 = of(1,0,0);
		CartesianVector v2 = of(0, 1, 0);
		Assert.assertTrue(of(0, 0, 1).coordinatesEqual(v1.crossProduct(v2)));
		
		v1 = randomUnitVector();
		v2 = v1.scale(2.0);
		Assert.assertTrue(v1.crossProduct(v2).isZeroVector());
	}
	
	@Test
	public void testPerpendicular()
	{
		CartesianVector v1 = randomUnitVector();
		assertEquals(0.0, v1.dotProduct(v1.getPerpendicularVector()), 0.00000001);
	}
	
	@Test
	public void randomRotationInPlane()
	{
		CartesianVector v1 = randomVector();
		CartesianVector v2 = v1.getPerpendicularVector();
		CartesianVector vRotated = v1.randomRotaionInPlane(v2);
		assertEquals(0.0, vRotated.dotProduct(v1), 0.00000001);
	}
	
	@Test
	public void testSmallRotation()
	{
		CartesianVector v1 = of(1,1,1);
		CartesianVector vRotated = v1.randomSmallRotation(v1.magnitude()*.1);
		Assert.assertTrue(Math.abs(v1.magnitude() - vRotated.magnitude()) < 0.000000001);
		Assert.assertFalse(v1.crossProduct(vRotated).isZeroVector());
		Assert.assertTrue(v1.randomSmallRotation(0.0).coordinatesEqual(v1));
	}
	
	@Test
	public void testNullSpace()
	{
		CartesianVector v1 = randomUnitVector();
		CartesianPlane vPlane = v1.getNullSpace();
		assertEquals(0.0, v1.dotProduct(vPlane.getV1()), 0.0000001);
		assertEquals(0.0, v1.dotProduct(vPlane.getV2()), 0.0000001);
		assertEquals(0.0, v1.dotProduct(vPlane.randomUnitVector()), .00000001);
	}
	
	@Test
	public void testRotateAboutThis()
	{
		CartesianVector vAxis = randomUnitVector();
		CartesianVector vVector = vAxis.getPerpendicularVector().unitVector();
		double vRadians = MathUtil.nextRandomBetween(0, Math.PI);
		CartesianVector vRotated = vAxis.rotateAboutThis(vVector, vRadians);
		Assert.assertTrue(vAxis.isPerpendicular(vRotated));
		assertEquals(vRadians, Math.acos(vVector.cosTheta(vRotated)), 1e-6);
	}
	
	@Test
	public void testProjectIntoPlane()
	{
		CartesianVector vV = CartesianVector.of(1,1,1);
		CartesianVector vNormal = CartesianVector.of(0,0,1);
		System.out.println(vV.projectOntoPlane(vNormal).toString());
		System.out.println(simpleProject(vV, vNormal));
		Assert.assertTrue(vV.projectOntoPlane(vNormal).coordinatesEqual(simpleProject(vV, vNormal)));
	}
	
	private CartesianVector simpleProject(CartesianVector pV, CartesianVector pNormal)
	{
		CartesianVector vNormal = pNormal.unitVector();
		return pV.subtract(vNormal.scale(pV.dotProduct(vNormal)));
	}
}
