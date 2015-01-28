package willey.lib.physics.polymer.interactor;

import static willey.lib.math.linearalgebra.CartesianVector.of;
import static willey.lib.math.linearalgebra.CartesianVector.randomUnitVector;
import static willey.lib.math.linearalgebra.CartesianVector.zeroVector;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.LineSegment;
import willey.lib.physics.polymer.interactor.FrontMonomer;
import willey.lib.physics.polymer.interactor.Rod;
import willey.lib.test.AbstractTest;

public class RodTest extends AbstractTest
{
	@Test
	public void testEndPoint()
	{
		Rod vRod = new Rod(of(1, 0, 0), of(0, 0, 0), 10, 0.5);
		Assert.assertTrue(of(10.0, 0, 0).coordinatesEqual(vRod.endPoint()));
	}

	@Test
	public void testParametric()
	{
		Rod vRod = createRodAtOrigin(0, 0);
		CartesianVector vPoint = vRod.parametricEquation(MathUtil.kRng
				.nextDouble());
		Assert.assertTrue(vPoint.magnitude() <= vRod.endPoint().magnitude());
		Assert.assertTrue(vPoint.crossProduct(vRod.direction()).isZeroVector());
	}

	@Test
	public void testTranslateOnly()
	{
		double vDistance = 1.0;
		Rod vRod = createRodAtOrigin(vDistance, 0);
		Rod vTranslated = move(vRod, vDistance, 0);
		Assert.assertTrue(vRod.direction().coordinatesEqual(
				vTranslated.direction()));
		assertEquals(
				vDistance,
				vRod.parametricEquation(0.5).distance(
						vTranslated.parametricEquation(0.5)), 0.0000001);
	}

	@Test
	public void testBad()
	{
		Rod vRod = new Rod(of(0.0, 0.0, 10.0), of(0.9532062418932963,
				3.772754707565036, 0.0), 10, 0.5);
		Rod vNoRotation = move(vRod, 0.0, 0.0);
		Assert.assertTrue(vNoRotation.position().coordinatesEqual(
				vRod.position()));
		Assert.assertTrue(vNoRotation.endPoint().coordinatesEqual(
				vRod.endPoint()));
		Assert.assertTrue(vNoRotation.direction().coordinatesEqual(
				vRod.direction()));
		assertEquals(1.0, vNoRotation.direction().cosTheta(vRod.direction()),
				1e-6);
	}

	@Test
	public void testRotateOnly()
	{
		Rod vRod = createRodAtOrigin(0, 0.1);
		Rod vRotated = move(vRod, 0.0, 0.1);
		Assert.assertTrue(vRod.parametricEquation(0.5).coordinatesEqual(
				vRotated.parametricEquation(0.5)));
		CartesianVector vDir = vRod.direction();
		Assert.assertTrue(vDir
				.coordinatesEqual(move(vRod, 0.0, 0.0).direction()));

		Rod vNoRotation = move(vRod, 0.0, 0.0);
		Assert.assertTrue(vNoRotation.position().coordinatesEqual(
				vRod.position()));
		Assert.assertTrue(vNoRotation.endPoint().coordinatesEqual(
				vRod.endPoint()));
		Assert.assertTrue(vNoRotation.direction().coordinatesEqual(
				vRod.direction()));
		assertEquals(1.0, vNoRotation.direction().cosTheta(vRod.direction()),
				1e-6);
	}

	@Test
	public void monomerInteraction()
	{
		Rod vRod1 = new Rod(of(1, 0, 0), zeroVector(), 10, 0.5);
		FrontMonomer vMonomer = new FrontMonomer(CartesianVector.of(0, 0, 0.3),
				0.5, null);
		Assert.assertTrue(vRod1.interacts(vMonomer));
	}
	
	@Test
	public void testRodInteraction()
	{
		Rod vRod1 = new Rod(of(1, 0, 0), zeroVector(), 10, 0.5);
		Rod vRod2 = new Rod(of(0,-1,0), of(1,1,0.7), 10, 0.5);
		Rod vRod3 = new Rod(of(1,0,0), zeroVector().add(randomUnitVector().scale(2)), 10, 0.5);
		Assert.assertTrue(vRod1.interacts(vRod2));
		Assert.assertTrue(vRod1.interacts(vRod2));
		Assert.assertFalse(vRod1.interacts(vRod3));
		for (int i=0; i < 1000; i++)
		{
			Rod vRod4 = move(vRod1, MathUtil.kRng.nextDouble(), 0.0);
			Assert.assertTrue(vRod1.interacts(vRod4));
		}
		
		for (int i=0; i < 1000; i++)
		{
			Rod vRod4 = move(vRod1, MathUtil.kRng.nextDouble() + 11, 0.0);
			boolean vInteracts = vRod1.interacts(vRod4);
			if (vInteracts)
			{
				System.err.println(vRod4.position());
			}
			Assert.assertFalse(vRod1.interacts(vRod4));
		}
	}
	
	private Rod move(Rod pRod, double pTranslation, double pRotation)
	{
		LineSegment vLineSegment = pRod.getLineSegment().rotate(pRotation).translate(CartesianVector.randomUnitVector().scale(pTranslation));
		return pRod.move(vLineSegment);
	}

	private Rod createRodAtOrigin(double pTranslation, double pRotation)
	{
		return new Rod(randomUnitVector(), zeroVector(), 10, 0.5);
	}
}
