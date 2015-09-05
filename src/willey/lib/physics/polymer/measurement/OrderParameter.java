package willey.lib.physics.polymer.measurement;

import static willey.lib.physics.polymer.measurement.MeasurementUtil.correlation;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.Rods;

class OrderParameter<R extends Rods> implements Measurement<R, Double>
{
	private static final Measurement<Rods, CartesianVector> kAverageRodDirection = Measurements.averageRodDirection();
	
	@Override
	public Double apply(R pMeasurable)
	{
		CartesianVector vDirector = kAverageRodDirection.apply(pMeasurable);
		return pMeasurable.getRods()
		.mapToDouble((pRod) -> 1.5*correlation(vDirector, pRod.direction()) - 0.5)
		.sum()/pMeasurable.rodCount();
	}
	
	@Override
	public String getName()
	{
		return "order.parameter";
	}
}