package willey.lib.physics.polymer.interactor;

import willey.lib.math.linearalgebra.CartesianVector;

class EndMonomer extends TerminusMonomer
{
	EndMonomer(CartesianVector pPosition, double pRadius,
			Monomer pLeftNeighbor)
	{
		super(pPosition, pRadius, pLeftNeighbor);
	}

	public void setRightNeighbor(Monomer pRightNeighbor)
	{
		throw new UnsupportedOperationException(
				"EndMonomer cannot have a right neighbor");
	}

	public void setLeftNeighbor(Monomer pLeftNeighbor)
	{
		setNeighbor(pLeftNeighbor);
		pLeftNeighbor.setRightNeighbor(this);
	}

	public Monomer rightNeighbor()
	{
		return null;
	}
	
	public Monomer leftNeighbor()
	{
		return mNeighbor;
	}
	
	@Override
	public Monomer reposition(CartesianVector pNewPosition)
	{
		return new EndMonomer(pNewPosition, interactionRadius(), mNeighbor);
	}
	
	public EndMonomer randomMove()
	{
		double vDistance = position().distance(mNeighbor.position());
		EndMonomer vReturn = new EndMonomer(mNeighbor.position().add(
				CartesianVector.randomUnitVector().scale(
						vDistance)), interactionRadius(), null);
		vReturn.mNeighbor = mNeighbor;
		return vReturn;
	}
	
	@Override
	public boolean isNeighbor(Monomer pMonomer)
	{
		return pMonomer == mNeighbor;
	}

	@Override
	public void replace(Monomer pMonomer)
	{
		makeNeighbors(mNeighbor, pMonomer);
	}
	
	@Override
	public String toString()
	{
		return getMonomerString();
	}
}
