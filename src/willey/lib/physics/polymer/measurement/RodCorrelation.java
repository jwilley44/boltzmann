package willey.lib.physics.polymer.measurement;

import java.util.stream.Collectors;

import willey.lib.physics.polymer.interactor.Rods;

class RodCorrelation<R extends Rods> implements Measurement<R, Double>
{
	@Override
	public Double apply(R pFrom)
	{
		return MeasurementUtil.legendreOrder(pFrom.getRods().map(pRod -> pRod.direction()).collect(Collectors.toList()));
	}

	public String getName()
	{
		return "rod.correlation";
	}
}