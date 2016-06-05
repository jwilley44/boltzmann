package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.PolymerAndRods;

public class PolymerParallelRadius<PR extends PolymerAndRods> implements
Measurement<PR, Double>
{

	@Override
	public Double apply(PR pMeasurable)
	{
		return Measurements.averageRodDirection().apply(pMeasurable).dotProduct(pMeasurable.getDirection());
	}

	@Override
	public String getName()
	{
		return "polymer.parallel.radius";
	}

}
