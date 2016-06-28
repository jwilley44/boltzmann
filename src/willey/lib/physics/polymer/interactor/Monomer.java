package willey.lib.physics.polymer.interactor;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.SegmentUtil;
import willey.lib.math.linearalgebra.SegmentUtil.Segment;

public interface Monomer extends Interactor
{
	public CartesianVector position();

	public void setLeftNeighbor(Monomer pLeftNeighbor);

	public void setRightNeighbor(Monomer pRightNeighbor);

	public Monomer reposition(CartesianVector pNewPosition);

	public void replace(Monomer pMonomer);

	public Monomer rightNeighbor();

	public Monomer leftNeighbor();
	
	public Monomer randomMove();

	public boolean isNeighbor(Monomer pMonomer);
	
	@Override
	default public Segment getLineSegment()
	{
		return SegmentUtil.get(position(), position());
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
