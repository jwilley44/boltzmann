package willey.lib.physics.polymer.interactor;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.LineSegment;

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

	@Override
	public CartesianVector getNearestPoint(Interactor pInteractor)
	{
		return mPosition;
	}

	@Override
	public double getDistance(CartesianVector pVector)
	{
		return pVector.distance(mPosition);
	}

	@Override
	public LineSegment getLineSegment()
	{
		return new LineSegment(mPosition, mPosition);
	}
}
