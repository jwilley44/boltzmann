package willey.lib.physics.polymer.interactor;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.SegmentUtil.Segment;

public interface Interactor
{
	CartesianVector position();
	
	Interactor reposition(CartesianVector pNewPosition);
	
	double interactionRadius();
	
	Segment getLineSegment();
	
	default Interactor project(Lattice pLattice)
	{
		return reposition(pLattice.projectIntoLattice(position()));
	}
	
	default boolean interacts(Interactor pInteractor)
	{
		return distance(pInteractor) <= interactionDistance(pInteractor);
	}
	
	default double distance(Interactor pInteractor)
	{
		return getLineSegment().distance(pInteractor.getLineSegment());
	}
	
	default double interactionDistance(Interactor pInteractor)
	{
		return interactionRadius() + pInteractor.interactionRadius();
	}
	
	default double energy(Interactor pInteractor)
	{
		return energy(distance(pInteractor), interactionDistance(pInteractor));
	}
	
	static double energy(double pDistance, double pInteractionDistance)
	{
		return pDistance >= pInteractionDistance ? 0 : 1/(pDistance*pDistance);
	}
	
	default boolean acceptMove(double pInteractionRadius, double pDistance)
	{
		return pDistance <= pInteractionRadius ?
		MathUtil.getThreadLocal().nextDouble() < Math.exp(-1.0/(10*pDistance*pDistance)) :
		true;
	}
}
