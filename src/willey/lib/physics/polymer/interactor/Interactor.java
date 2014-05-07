package willey.lib.physics.polymer.interactor;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.LineSegment;

public interface Interactor
{
	CartesianVector position();
	
	Interactor reposition(CartesianVector pNewPosition);
	
	double interactionRadius();
	
	Interactor randomMove();
	
	CartesianVector getNearestPoint(Interactor pInteractor);
	
	double getDistance(CartesianVector pVector);
	
	LineSegment getLineSegment();
	
	default boolean interacts(Interactor pInteractor)
	{
		double vInteractionRadius = pInteractor.interactionRadius()
				+ interactionRadius();
		CartesianVector vClosest = pInteractor.getNearestPoint(this);
		double vDistance = getDistance(vClosest);
		return vDistance < vInteractionRadius;
	}
}
