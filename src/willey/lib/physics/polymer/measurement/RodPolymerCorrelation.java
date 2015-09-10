package willey.lib.physics.polymer.measurement;

import static willey.lib.physics.polymer.measurement.MeasurementUtil.correlation;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.PolymerAndRods;

class RodPolymerCorrelation<PR extends PolymerAndRods> implements
		Measurement<PR, Double>
{
	private static final Measurement<PolymerAndRods, CartesianVector> kAverageRodDirection = Measurements.averageRodDirection();
	
	@Override
	public Double apply(PR pFrom)
	{
		CartesianVector vAverageRodDirection = kAverageRodDirection.apply(pFrom);
		double vCorrelation = pFrom.getDirections().mapToDouble((pDirection) -> correlation(vAverageRodDirection, pDirection)).sum();
		return Double.valueOf(vCorrelation / (pFrom.getSize() - 1));
	}

	@Override
	public String getName()
	{
		return "rod.polymer.correlation";
	}
}