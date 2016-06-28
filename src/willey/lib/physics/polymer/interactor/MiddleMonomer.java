package willey.lib.physics.polymer.interactor;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.SegmentUtil;
import willey.lib.math.linearalgebra.SegmentUtil.Segment;

class MiddleMonomer implements Monomer
{
	private final CartesianVector mPosition;
	private final double mRadius;
	private Monomer mLeftNeighbor;
	private Monomer mRightNeighbor;

	MiddleMonomer(CartesianVector pPosition, double pRadius,
			Monomer pLeftNeighbor)
	{
		this(pPosition, pRadius);
		setLeftNeighbor(pLeftNeighbor);
		mLeftNeighbor.setRightNeighbor(this);
	}

	private MiddleMonomer(CartesianVector pPosition, double pRadius)
	{
		mPosition = pPosition;
		mRadius = pRadius;
	}

	public CartesianVector position()
	{
		return mPosition;
	}

	public double interactionRadius()
	{
		return mRadius;
	}

	public Monomer rightNeighbor()
	{
		return mRightNeighbor;
	}

	public Monomer leftNeighbor()
	{
		return mLeftNeighbor;
	}

	public void setLeftNeighbor(Monomer pLeftNeighbor)
	{
		mLeftNeighbor = pLeftNeighbor;
	}

	public void setRightNeighbor(Monomer pRightNeighbor)
	{
		mRightNeighbor = pRightNeighbor;
	}
	
	public MiddleMonomer randomMove()
	{
		CartesianVector vMidVector = mLeftNeighbor.position()
				.subtract(mRightNeighbor.position()).scale(0.5);
		CartesianVector vMidPoint = vMidVector.add(mRightNeighbor.position());
		CartesianVector vVertical = mPosition.subtract(vMidPoint);
		CartesianVector vNewVertical = vMidVector.randomRotaionInPlane(vVertical).scale(vVertical.magnitude());
		CartesianVector vPosition = vMidPoint.add(vNewVertical);
		MiddleMonomer vReturn =  new MiddleMonomer(vPosition, mRadius);
		vReturn.mLeftNeighbor = mLeftNeighbor;
		vReturn.mRightNeighbor = mRightNeighbor;
		return vReturn;
	}
	
	public void replace(Monomer pMonomer)
	{
		makeNeighbors(mLeftNeighbor, pMonomer);
		makeNeighbors(pMonomer, mRightNeighbor);
	}

	public Monomer reposition(CartesianVector pNewPosition)
	{
		return new MiddleMonomer(pNewPosition, interactionRadius());
	}
	
	@Override
	public boolean isNeighbor(Monomer pMonomer)
	{
		return pMonomer == mLeftNeighbor || pMonomer == mRightNeighbor;
	}
	
	@Override
	public String toString()
	{
		return getMonomerString();
	}

	@Override
	public Segment getLineSegment()
	{
		return SegmentUtil.get(mPosition, mPosition);
	}
}
