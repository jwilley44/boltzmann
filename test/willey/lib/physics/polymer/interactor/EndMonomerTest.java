package willey.lib.physics.polymer.interactor;


import org.junit.Assert;
import org.junit.Test;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.EndMonomer;
import willey.lib.physics.polymer.interactor.FrontMonomer;
import willey.lib.physics.polymer.interactor.Monomer;
import willey.lib.test.AbstractTest;

public class EndMonomerTest extends AbstractTest
{
	@Test
	public void testData()
	{
		CartesianVector vPosition = CartesianVector.randomUnitVector();
		double vRadius = 0.5;
		Monomer vMonomer = new EndMonomer(vPosition, vRadius, null);
		Assert.assertEquals(vPosition, vMonomer.position());
		Assert.assertEquals(vRadius, vMonomer.interactionRadius(), 1e-6);
		Assert.assertNull(vMonomer.rightNeighbor());
	}
	
	@Test
	public void randomMove()
	{
		EndMonomer vMonomer = create();
		double vLeftDistance = vMonomer.position().distance(vMonomer.leftNeighbor().position());
		EndMonomer vMoved = vMonomer.randomMove();
		assertEquals(vLeftDistance, vMoved.position().distance(vMonomer.leftNeighbor().position()), 0.00000001);
	}
	
	@Test
	public void replace()
	{
		EndMonomer vMonomer = create();
		Monomer vLeft = vMonomer.leftNeighbor();
		EndMonomer vMoved = vMonomer.randomMove();
		vMonomer.replace(vMoved);
		Assert.assertEquals(vMoved, vLeft.rightNeighbor());
		Assert.assertEquals(vLeft, vMoved.leftNeighbor());
	}
	
	@Test
	public void createRelative()
	{
		EndMonomer vMonomer = create();
		CartesianVector vNewPosition = CartesianVector.randomUnitVector();
		Monomer vRelativeMonomer = vMonomer.reposition(vNewPosition);
		Assert.assertEquals(vMonomer.interactionRadius(), vRelativeMonomer.interactionRadius(), 1e-6);
		Assert.assertEquals(vNewPosition, vRelativeMonomer.position());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void setRightNeighbor()
	{
		create().setRightNeighbor(create());
	}
	
	private EndMonomer create()
	{
		CartesianVector vPosition = CartesianVector.randomUnitVector();
		FrontMonomer vFrontMonomer = new FrontMonomer(vPosition, 0.5, null);
		EndMonomer vEndMonomer = new EndMonomer(vPosition.add(CartesianVector.randomUnitVector()), 0.5, vFrontMonomer);
		vFrontMonomer.setRightNeighbor(vEndMonomer);
		return vEndMonomer;
	}
}
