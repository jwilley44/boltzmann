package willey.lib.physics.polymer.measurement;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.Polymer;

class PolymerCenter<P extends Polymer> implements Measurement<P, Double>
{
	public Double apply(P pMeasurable)
	{
		return Double.valueOf(pMeasurable.getLattice().centerStart().distance(
				pMeasurable.getMonomers()
				.map(pMonomer -> pMonomer.position())
				.reduce(CartesianVector.zeroVector(), (a, b) -> a.add(b)).scale(1 / pMeasurable.getSize())));
	}

	@Override
	public String getName()
	{
		return "polymer.center";
	}
}
