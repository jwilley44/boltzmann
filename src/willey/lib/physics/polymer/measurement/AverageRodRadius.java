package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Rods;

class AverageRodRadius<R extends Rods> implements Measurement<R, Double>
{

	@Override
	public Double apply(R pFrom)
	{
		return pFrom.getRods().mapToDouble((pRod) -> pRod.radius()).sum() /(pFrom.rodCount());
	}

	@Override
	public String getName()
	{
		return "RodRadius";
	}

}