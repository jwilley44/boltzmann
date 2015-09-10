package willey.lib.physics.polymer.measurement;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.CartesianVector.VectorSum;
import willey.lib.physics.polymer.interactor.Polymer;

class PolymerPosition<P extends Polymer> implements
		Measurement<P, CartesianVector>
{
	@Override
	public CartesianVector apply(P pMeasurable)
	{
		VectorSum vConsumer = new VectorSum();
		pMeasurable.getMonomers().map((pMonomer) -> pMonomer.position())
				.forEach(vConsumer);
		return vConsumer.getSum().scale(1.0 / pMeasurable.getSize());
	}

	@Override
	public String getName()
	{
		return "polymer.position";
	}
}