package willey.lib.physics.polymer.measurement;

import java.util.stream.Collectors;

import willey.lib.physics.polymer.interactor.Rods;

class OrderParameter<R extends Rods> implements Measurement<R, Double>
{
	@Override
	public Double apply(R pMeasurable)
	{
		return Double.valueOf(MeasurementUtil.orderParameter(pMeasurable.getRods().map(pRod -> pRod.direction()).collect(Collectors.toList())));
	}
	
	@Override
	public String getName()
	{
		return "order.parameter";
	}
}