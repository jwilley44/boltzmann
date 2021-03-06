package willey.lib.physics.polymer.interactor;

import org.junit.Assert;

import org.junit.Test;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.EndMonomer;
import willey.lib.physics.polymer.interactor.FrontMonomer;
import willey.lib.physics.polymer.interactor.MiddleMonomer;
import willey.lib.physics.polymer.interactor.Monomer;
import willey.lib.test.AbstractTest;

public class MiddleMonomerTest extends AbstractTest
{
	@Test
	public void testData()
	{
		CartesianVector vPosition = CartesianVector.randomUnitVector();
		double vRadius = 0.5;
		Monomer vFrontMonomer = new FrontMonomer(CartesianVector.randomUnitVector(), vRadius, null);
		MiddleMonomer vMonomer = new MiddleMonomer(vPosition, vRadius, vFrontMonomer);
		Assert.assertEquals(vPosition, vMonomer.position());
		Assert.assertEquals(vRadius, vMonomer.interactionRadius(), 1e-6);
	}
	
	@Test
	public void testRandomMove()
	{
		MiddleMonomer vMonomer = create();
		double vLeftDistance = vMonomer.position().distance(vMonomer.leftNeighbor().position());
		double vRightDistance = vMonomer.position().distance(vMonomer.rightNeighbor().position());
		MiddleMonomer vMoved = vMonomer.randomMove();
		Assert.assertFalse(vMoved.position().coordinatesEqual(vMonomer.position()));
		assertEquals(vLeftDistance, vMoved.position().distance(vMonomer.leftNeighbor().position()),   0.00000001);
		assertEquals(vRightDistance, vMoved.position().distance(vMonomer.rightNeighbor().position()), 0.00000001);
	}
	
	@Test
	public void testReplace()
	{
		MiddleMonomer vMonomer = create();
		Monomer vLeft = vMonomer.leftNeighbor();
		Monomer vRight = vMonomer.rightNeighbor();
		MiddleMonomer vMoved = vMonomer.randomMove();
		vMonomer.replace(vMoved);
		Assert.assertEquals(vLeft, vMoved.leftNeighbor());
		Assert.assertEquals(vMoved, vMoved.leftNeighbor().rightNeighbor());
		Assert.assertEquals(vRight, vMoved.rightNeighbor());
		Assert.assertEquals(vMoved, vMoved.rightNeighbor().leftNeighbor());
	}
	
	@Test
	public void testCreateRelative()
	{
		MiddleMonomer vMonomer = create();
		CartesianVector vNewPosition = CartesianVector.randomUnitVector();
		Monomer vRelativeMonomer = vMonomer.reposition(vNewPosition);
		Assert.assertEquals(vMonomer.interactionRadius(), vRelativeMonomer.interactionRadius(), 1e-6);
		Assert.assertEquals(vNewPosition, vRelativeMonomer.position());
	}
	
	private MiddleMonomer create()
	{
		CartesianVector vPosition = CartesianVector.randomUnitVector();
		double vRadius = 1.5;
		Monomer vFrontMonomer = new FrontMonomer(vPosition, vRadius, null);
		vPosition = vPosition.add(CartesianVector.randomUnitVector());
		MiddleMonomer vMonomer = new MiddleMonomer(vPosition, vRadius, vFrontMonomer);
		vPosition = vPosition.add(CartesianVector.randomUnitVector());
		EndMonomer vEndMonomer = new EndMonomer(vPosition, vRadius, vMonomer);
		vEndMonomer.setLeftNeighbor(vMonomer);
		return vMonomer;
	}
}
