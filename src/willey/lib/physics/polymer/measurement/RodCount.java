package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Rods;

class RodCount<R extends Rods> implements Measurement<R, Integer>
{
	@Override
	public Integer apply(R pFrom)
	{
		return Integer.valueOf(pFrom.rodCount());
	}

	@Override
	public String getName()
	{
		return "rod.count";
	}
}