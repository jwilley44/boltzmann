package willey.lib.physics.polymer.measurement;

import static willey.lib.physics.polymer.measurement.MeasurementUtil.correlation;
import willey.lib.physics.polymer.interactor.Rods;

class RodCorrelation<R extends Rods> implements Measurement<R, Double>
{
	@Override
	public Double apply(R pFrom)
	{
		return correlation(
				pFrom.getRods().map((pRodA) -> pRodA.direction()), 
				pFrom.getRods().map((pRodB) -> pRodB.direction()),
				pFrom.rodCount());
	}

	public String getName()
	{
		return "rod.correlation";
	}
}