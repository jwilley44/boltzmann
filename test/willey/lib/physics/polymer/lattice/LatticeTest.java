package willey.lib.physics.polymer.lattice;

import java.util.Iterator;

import org.junit.Assert;

import org.junit.Test;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.test.AbstractTest;
import java.util.function.Function;
import static willey.lib.math.linearalgebra.CartesianVector.*;

public class LatticeTest extends AbstractTest
{
	@Test
	public void testProject()
	{
		Lattice vLattice = Lattice.cubeLattice(4);
		CartesianVector vVector = CartesianVector.of(1.5, 3.5, 2.5);
		Assert.assertTrue(vVector.coordinatesEqual(vLattice.projectIntoLattice(vVector)));
		vVector = CartesianVector.of(1.4, 6.3, -0.2);
		Assert.assertTrue(CartesianVector.of(1.4, 2.3, 3.8).coordinatesEqual(vLattice.projectIntoLattice(vVector)));
	}
	
	@Test
	public void testMovements()
	{
		int vCount = 0;
		Iterator<Function<CartesianVector, CartesianVector>> vIterator = Lattice.cubeLattice(
				4).getNewLatticeCoordinatesFunctions().iterator();
		while (vIterator.hasNext())
		{
			vIterator.next();
			vCount++;
		}
		Assert.assertEquals(27, vCount);
	}

	@Test
	public void testInNewLattice()
	{
		Lattice vLattice = Lattice.cubeLattice(4);
		CartesianVector vVector = of(2.1, 3.2, -1.7);
		Assert.assertTrue(vLattice.getInNewLattice(vVector, new NoMove())
				.coordinatesEqual(of(2.1, 3.2, 2.3)));
		Assert.assertTrue(vLattice.getInNewLattice(vVector, new SubtractX(4))
				.coordinatesEqual(of(-1.9, 3.2, 2.3)));

		int vDimension = 8;
		vLattice = Lattice.cubeLattice(vDimension);
		for (int i = 0; i < 1000000; i++)
		{
			vVector = vLattice.getInNewLattice(
					randomUnitVector().scale(MathUtil.kRng.nextInt()),
					new SubXYAddZ(vDimension));
			Assert.assertTrue(vVector.x() < 0 && vVector.x() >= -vDimension);
			Assert.assertTrue(vVector.y() < 0 && vVector.y() >= -vDimension);
			Assert.assertTrue(vVector.z() < vDimension * 2
					&& vVector.z() >= vDimension);
		}
	}

	private static class NoMove implements Function<LatticeCube, LatticeCube>
	{
		@Override
		public LatticeCube apply(LatticeCube pFrom)
		{
			return pFrom;
		}
	}

	private static class SubXYAddZ implements
			Function<LatticeCube, LatticeCube>
	{
		private final int mDimension;

		private SubXYAddZ(int pDimension)
		{
			mDimension = pDimension;
		}

		@Override
		public LatticeCube apply(LatticeCube pFrom)
		{
			return pFrom.addX(-mDimension).addY(-mDimension).addZ(mDimension);
		}
	}

	private static class SubtractX implements
			Function<LatticeCube, LatticeCube>
	{
		private final int mDimension;

		private SubtractX(int pDimension)
		{
			mDimension = -pDimension;
		}

		@Override
		public LatticeCube apply(LatticeCube pFrom)
		{
			return pFrom.addX(mDimension);
		}
	}
}
