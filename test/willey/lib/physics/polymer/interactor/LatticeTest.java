package willey.lib.physics.polymer.interactor;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.test.AbstractTest;

public class LatticeTest extends AbstractTest
{
	@Test
	public void testProject()
	{
		Lattice vLattice = Lattice.cubeLattice(4);
		CartesianVector vVector = CartesianVector.of(1.5, 3.5, 2.5);
		Assert.assertTrue(vVector.coordinatesEqual(vLattice
				.projectIntoLattice(vVector)));
		vVector = CartesianVector.of(1.4, 6.3, -0.2);
		Assert.assertTrue(CartesianVector.of(1.4, 2.3, 3.8).coordinatesEqual(
				vLattice.projectIntoLattice(vVector)));
	}
	
	@Test
	public void testBoundaryConditions()
	{
		int vSize = 4;
		Lattice vLattice = Lattice.cubeLattice(vSize);
		final CartesianVector vVector = CartesianVector.of(1.5, 3.5, 2.5);
		vLattice.getNewLatticeCoordinatesFunctions()
				.stream()
				.forEach(
						pFunction -> assertEquals(
								pFunction.apply(CartesianVector.zeroVector())
										.magnitude(), pFunction.apply(vVector)
										.distance(vVector), 1e-4));
		final CartesianVector vPoint1 = CartesianVector.zeroVector();
		final CartesianVector vAdjustment = CartesianVector.of(0.01, 0.01, 0.01);
		final CartesianVector vPoint2 = CartesianVector.of(4,4,4).subtract(vAdjustment);
		assertEquals(vAdjustment.magnitude(), vLattice.getNewLatticeCoordinatesFunctions()
				.stream()
				.map(pFunction -> pFunction.apply(vPoint1))
				.mapToDouble(pVector -> pVector.distance(vPoint2)).min().getAsDouble(), 1e-4);
	}
	
	@Test
	public void testBoundaryFunctions()
	{
		final Lattice vLattice = Lattice.cubeLattice(5);
		final CartesianVector vVector = CartesianVector.of(6, 7, 8);
		List<CartesianVector> vNew = vLattice.getNewLatticeCoordinatesFunctions().stream().map(pFunction -> pFunction.apply(vVector)).collect(Collectors.toList());
		vNew.stream().forEach(pVector -> assertTrue(vLattice.projectIntoLattice(vVector).distance(pVector) <= Math.sqrt(75)));
	}
	
	@Test
	public void testMovements()
	{
		int vDimension = 4;
		Lattice vLattice = Lattice.cubeLattice(vDimension);
		CartesianVector vPoint = CartesianVector.of(0.1, 1.3, 3.5);
		Set<CartesianVector> vPointImages = new HashSet<CartesianVector>();
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				for (int k = -1; k <= 1; k++)
				{
					vPointImages.add(vPoint.add(CartesianVector.of(i, j, k)
							.scale(vDimension)));
				}
			}
		}
		Assert.assertEquals(vPointImages.size(), vLattice.getNewLatticeCoordinatesFunctions().size());
		vLattice.getNewLatticeCoordinatesFunctions().stream()
				.map((pFunction) -> pFunction.apply(vPoint))
				.forEach((pPoint) -> Assert.assertTrue(vPointImages.stream().anyMatch((pImage) -> pImage.coordinatesEqual(pPoint))));
		
	}
}
