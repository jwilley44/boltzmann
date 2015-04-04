package willey.lib.physics.polymer.interactor;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.physics.polymer.experiment.Equilibrator.Equilibration;

public class RodImplTest
{
	@Test
	public void testProjectedStream()
	{
		int vCount = 10;
		RodsImpl vRods = getRods(vCount, vCount, 0.1, 0.1, 0.1,
				Lattice.cubeLattice(vCount));
		Rod vRandom = vRods.chooseRandom();
		Equilibration<Rods> vEquilibration = vRods.getEquilibration();
		Assert.assertEquals(vCount - 1, vEquilibration.projectedStream(vRandom)
				.count());
	}

	private RodsImpl getRods(int vCount, double pLength, double vRadius,
			double pTranslation, double pRotation, Lattice pLattice)
	{
		return new RodsImpl(vCount, pLength, vRadius, pTranslation, pRotation,
				pLattice, RodsUtil.Orientation.Ordered,
				RodsUtil.Position.Ordered);
	}
}
