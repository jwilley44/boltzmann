package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Rods;

class AverageRodLength<R extends Rods> implements Measurement<R, Double>
{
	@Override
	public Double apply(R pFrom)
	{
		return pFrom.getRods().mapToDouble((pRod) -> pRod.length()).sum() /(pFrom.rodCount());
	}

	@Override
	public String getName()
	{
		return "RodLength";
	}
}