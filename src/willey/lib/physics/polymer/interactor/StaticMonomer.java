package willey.lib.physics.polymer.interactor;

import willey.lib.math.linearalgebra.CartesianVector;

public class StaticMonomer implements Monomer
{
	private final double mRadius;
	private final CartesianVector mPosition;

	StaticMonomer(CartesianVector pPosition, double pRadius)
	{
		mRadius = pRadius;
		mPosition = pPosition;
	}

	@Override
	public double interactionRadius()
	{
		return mRadius;
	}

	@Override
	public CartesianVector position()
	{
		return mPosition;
	}

	@Override
	public void setLeftNeighbor(Monomer pLeftNeighbor)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRightNeighbor(Monomer pRightNeighbor)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Monomer reposition(CartesianVector pNewPosition)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void replace(Monomer pMonomer)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Monomer rightNeighbor()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Monomer leftNeighbor()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Monomer randomMove()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isNeighbor(Monomer pMonomer)
	{
		throw new UnsupportedOperationException();
	}

}
