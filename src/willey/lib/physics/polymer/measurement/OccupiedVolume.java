package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Rods;

class OccupiedVolume<R extends Rods> implements Measurement<R, Double>
{
	@Override
	public Double apply(R pFrom)
	{
		return Double.valueOf(pFrom.rodsVolume());
	}

	@Override
	public String getName()
	{
		return "occupied.volume";
	}
}