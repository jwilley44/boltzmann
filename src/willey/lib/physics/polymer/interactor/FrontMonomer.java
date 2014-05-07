package willey.lib.physics.polymer.interactor;

import willey.lib.math.linearalgebra.CartesianVector;

public class FrontMonomer extends TerminusMonomer
{
	public FrontMonomer(CartesianVector pPosition, double pRadius,
			Monomer pRightNeighbor)
	{
		super(pPosition, pRadius, pRightNeighbor);
	}
	
	public void setLeftNeighbor(Monomer pLeftNeighbor)
	{
		throw new UnsupportedOperationException(
				"FrontMonomer cannot have a left neighbor");
	}
	
	public void setRightNeighbor(Monomer pRightNeighbor)
	{
		setNeighbor(pRightNeighbor);
	}
	
	public Monomer rightNeighbor()
	{
		return mNeighbor;
	}
	
	public Monomer leftNeighbor()
	{
		return null;
	}
	
    @Override
	public Monomer reposition(CartesianVector pNewPosition)
	{
		return new FrontMonomer(pNewPosition, interactionRadius(), mNeighbor);
	}
    
    public FrontMonomer randomMove()
	{
    	double vDistance = position().distance(mNeighbor.position());
		FrontMonomer vReturn = new FrontMonomer(mNeighbor.position().add(
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
		makeNeighbors(pMonomer, mNeighbor);
	}
	
	@Override
	public String toString()
	{
		return getMonomerString();
	}
}
