package willey.lib.physics.polymer.interactor;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.physics.polymer.measurement.Measurements;
import willey.lib.test.AbstractTest;
import willey.lib.util.StreamUtil;

public class PolymerTest extends AbstractTest
{
	@Test
	public void testSizeAndRadius()
	{
		int vSize = 20;
		double vRadius = 0.5;
		PolymerImpl vPolymer = new PolymerImpl(vSize, vRadius,
				Lattice.cubeLattice(vSize));
		StreamUtil.dualSream(
				vPolymer.getMonomers().limit(vPolymer.getSize() - 1),
				vPolymer.getMonomers().skip(1)).forEach(
				(pPair) -> {
					Monomer vMonomer = pPair.getA();
					Assert.assertEquals(vRadius, vMonomer.interactionRadius(),
							1e-6);
					double vDistance = vMonomer.distance(pPair.getB());
					Assert.assertEquals(vMonomer.interactionRadius() * 2,
							vDistance, 1e-6);
					Assert.assertTrue(!vMonomer.interacts(pPair.getB()));
				});
		Assert.assertTrue(vPolymer.getEndToEndDistance() <= vSize);
	}

	@Test
	public void chooseRandom()
	{
		int vSize = 20;
		double vRadius = 0.5;
		PolymerImpl vPolymer = new PolymerImpl(vSize, vRadius,
				Lattice.cubeLattice(vSize));
		for (int i = 0; i < 1000000; i++)
		{
			Assert.assertNotNull(vPolymer.chooseRandom());
		}
	}

	@Test
	public void testPolymerCenter()
	{
		int vSize = 20;
		double vRadius = 0.5;
		PolymerImpl vPolymer = new PolymerImpl(vSize, vRadius,
				Lattice.cubeLattice(vSize));
		for (int i = 0; i < 100; i++)
		{
			Monomer vMonomer = vPolymer.chooseRandom();
			vPolymer.replace(vMonomer, vMonomer.randomMove());
			System.out.println(Measurements.polymerCenter().apply(vPolymer));
		}
	}
}
