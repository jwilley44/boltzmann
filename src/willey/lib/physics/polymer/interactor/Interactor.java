package willey.lib.physics.polymer.interactor;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.LineSegment;

public interface Interactor
{
	CartesianVector position();
	
	Interactor reposition(CartesianVector pNewPosition);
	
	double interactionRadius();
	
	CartesianVector getNearestPoint(Interactor pInteractor);
	
	double getDistance(CartesianVector pVector);
	
	LineSegment getLineSegment();
	
	default Interactor project(Lattice pLattice)
	{
		return reposition(pLattice.projectIntoLattice(position()));
	}
	
	default boolean interacts(Interactor pInteractor)
	{
		return getDistance(pInteractor) <= interactionDistance(pInteractor);
	}
	
	default double distance(Interactor pInteractor)
	{
		return getDistance(pInteractor.getNearestPoint(this));
	}
	
	default double interactionDistance(Interactor pInteractor)
	{
		return interactionRadius() + pInteractor.interactionRadius();
	}
	
	default double getDistance(Interactor pInteractor)
	{
		return getDistance(pInteractor.getNearestPoint(this));
	}
	
	default double energy(Interactor pInteractor)
	{
		CartesianVector vClosest = pInteractor.getNearestPoint(this);
		double vDistance = getDistance(vClosest);
		return energy(vDistance, interactionDistance(pInteractor));
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
