package willey.lib.physics.polymer.measurement;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.Polymer;

class PolymerDirection<P extends Polymer> implements
		Measurement<P, CartesianVector>
{
	@Override
	public CartesianVector apply(P pMeasurable)
	{
		return pMeasurable.getDirection().unitVector();
	}

	@Override
	public String getName()
	{
		return "PolymerDirection";
	}
}