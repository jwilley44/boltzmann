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
		double vInteractionRadius = pInteractor.interactionRadius()
				+ interactionRadius();
		CartesianVector vClosest = pInteractor.getNearestPoint(this);
		double vDistance = getDistance(vClosest);
		return !acceptMove(vInteractionRadius, vDistance);
	}
	
	default double distance(Interactor pInteractor)
	{
		return getDistance(pInteractor.getNearestPoint(this));
	}
	
	default double interactionDistance(Interactor pInteractor)
	{
		double vInteractionRadius = pInteractor.interactionRadius()
				+ interactionRadius();
		CartesianVector vClosest = pInteractor.getNearestPoint(this);
		double vDistance = getDistance(vClosest);
		return vInteractionRadius-vDistance;
	}
	
	default double energy(Interactor pInteractor)
	{
		CartesianVector vClosest = pInteractor.getNearestPoint(this);
		double vDistance = getDistance(vClosest);
		return energy(vDistance, interactionDistance(pInteractor));
	}
	
	static double energy(double pDistance, double pInteractionDistance)
	{
		return Math.pow(pInteractionDistance/pDistance, 12.0) - 2*Math.pow(pInteractionDistance/pDistance, 6);
	}
	
	default boolean acceptMove(double pInteractionRadius, double pDistance)
	{
		return pDistance <= pInteractionRadius ?
		MathUtil.getThreadLocal().nextDouble() < Math.exp(-1.0/(10*pDistance*pDistance)) :
		true;
	}
}
