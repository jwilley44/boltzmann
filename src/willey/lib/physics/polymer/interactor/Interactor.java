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
	
	default boolean interacts(Interactor pInteractor)
	{
		double vInteractionRadius = pInteractor.interactionRadius()
				+ interactionRadius();
		CartesianVector vClosest = pInteractor.getNearestPoint(this);
		double vDistance = getDistance(vClosest);
		return !acceptMove(vInteractionRadius, vDistance);
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
		return 1 /(vDistance * vDistance);
	}
	
	default boolean acceptMove(double pInteractionRadius, double pDistance)
	{
		return pDistance <= pInteractionRadius ?
		MathUtil.kRng.nextDouble() < Math.exp(-1.0/(10*pDistance*pDistance)) :
		true;
	}
}
