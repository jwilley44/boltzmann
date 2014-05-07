package willey.lib.physics.polymer.interactor;

import willey.lib.math.linearalgebra.CartesianVector;

public interface Monomer extends Interactor
{
	public CartesianVector position();

	public void setLeftNeighbor(Monomer pLeftNeighbor);

	public void setRightNeighbor(Monomer pRightNeighbor);

	public Monomer reposition(CartesianVector pNewPosition);

	public void replace(Monomer pMonomer);

	public Monomer rightNeighbor();

	public Monomer leftNeighbor();

	public boolean isNeighbor(Monomer pMonomer);

	default boolean interacts(Interactor pInteractor)
	{
		boolean vInteracts = false;
		boolean vIsMonomer = pInteractor instanceof Monomer;
		if (vIsMonomer)
		{
			if (!isNeighbor((Monomer)pInteractor))
			{
				vInteracts = pInteractor.getNearestPoint(this)
						.distance(position()) < 
						(pInteractor.interactionRadius() + interactionRadius());
			}
		}
		else
		{
			vInteracts = pInteractor.getNearestPoint(this)
					.distance(position()) < 
					(pInteractor.interactionRadius() + interactionRadius());
		}
		return vInteracts;
	}

	default public void makeNeighbors(Monomer pLeft, Monomer pRight)
	{
		pLeft.setRightNeighbor(pRight);
		pRight.setLeftNeighbor(pLeft);
	}

	default public String getMonomerString()
	{
		return "{" + position() + "/" + interactionRadius() + "}";
	}
}
