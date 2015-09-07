package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.PolymerAndRods;

public class Equilibrations implements Measurement<PolymerAndRods, Integer>
{

	@Override
	public Integer apply(PolymerAndRods pMeasurable)
	{
		return Integer.valueOf(pMeasurable.equilibrations());
	}

	@Override
	public String getName()
	{
		return "equlibration";
	}

}
