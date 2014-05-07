package willey.lib.physics.polymer.interactor;


import org.junit.Assert;
import org.junit.Test;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.EndMonomer;
import willey.lib.physics.polymer.interactor.FrontMonomer;
import willey.lib.physics.polymer.interactor.Monomer;
import willey.lib.test.AbstractTest;

public class FrontMonomerTest extends AbstractTest
{
	@Test
	public void testData()
	{
		CartesianVector vPosition = CartesianVector.randomUnitVector();
		double vRadius = 0.5;
		Monomer vMonomer = new FrontMonomer(vPosition, vRadius, null);
		Assert.assertEquals(vPosition, vMonomer.position());
		Assert.assertEquals(vRadius, vMonomer.interactionRadius(), 1e-6);
		Assert.assertNull(vMonomer.leftNeighbor());
	}
	
	@Test
	public void randomMove()
	{
		FrontMonomer vMonomer = create();
		double vRightDistance = vMonomer.position().distance(vMonomer.rightNeighbor().position());
		FrontMonomer vMoved = vMonomer.randomMove();
		assertEquals(vRightDistance, vMoved.position().distance(vMonomer.rightNeighbor().position()), 0.00000001);
	}
	
	@Test
	public void replace()
	{
		FrontMonomer vMonomer = create();
		Monomer vRight = vMonomer.rightNeighbor();
		FrontMonomer vMoved = vMonomer.randomMove();
		vMonomer.replace(vMoved);
		Assert.assertEquals(vMoved, vRight.leftNeighbor());
		Assert.assertEquals(vRight, vMoved.rightNeighbor());
	}
	
	@Test
	public void createRelative()
	{
		FrontMonomer vMonomer = create();
		CartesianVector vNewPosition = CartesianVector.randomUnitVector();
		Monomer vRelativeMonomer = vMonomer.reposition(vNewPosition);
		Assert.assertEquals(vMonomer.interactionRadius(), vRelativeMonomer.interactionRadius(), 1e-6);
		Assert.assertEquals(vNewPosition, vRelativeMonomer.position());
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void setLeftNeighbor()
	{
		create().setLeftNeighbor(create());
	}
	
	private FrontMonomer create()
	{
		CartesianVector vPosition = CartesianVector.randomUnitVector();
		FrontMonomer vFrontMonomer = new FrontMonomer(vPosition, 0.5, null);
		EndMonomer vEndMonomer = new EndMonomer(vPosition.add(CartesianVector.randomUnitVector()), 0.5, vFrontMonomer);
		vFrontMonomer.setRightNeighbor(vEndMonomer);
		return vFrontMonomer;
	}
}
