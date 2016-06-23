package willey.lib.physics.polymer.measurement;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.Polymer;

class PolymerPosition<P extends Polymer> implements
		Measurement<P, CartesianVector>
{
	@Override
	public CartesianVector apply(P pMeasurable)
	{
		return pMeasurable.getMonomers().map(pMonomer -> pMonomer.position())
				.reduce(CartesianVector.vectorSum()).get();
	}

	@Override
	public String getName()
	{
		return "polymer.position";
	}
}