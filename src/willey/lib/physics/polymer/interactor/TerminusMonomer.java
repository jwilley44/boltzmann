package willey.lib.physics.polymer.interactor;

import willey.lib.math.linearalgebra.CartesianVector;

abstract class TerminusMonomer implements Monomer
{
	private CartesianVector mPosition;
	private final double mRadius;
	protected Monomer mNeighbor;

	TerminusMonomer(CartesianVector pPosition, double pRadius, Monomer pNeighbor)
	{
		setPosition(pPosition);
		mRadius = pRadius;
		mNeighbor = pNeighbor;
	}

	public CartesianVector position()
	{
		return mPosition;
	}

	public double interactionRadius()
	{
		return mRadius;
	}

	protected void setNeighbor(Monomer pNeighbor)
	{
		mNeighbor = pNeighbor;
	}

	protected void setPosition(CartesianVector pPosition)
	{
		mPosition = pPosition;
	}
}
