package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Rods;

class RodTranslation<R extends Rods> implements Measurement<R, Double>
{

	@Override
	public Double apply(R pMeasurable)
	{
		return pMeasurable.rodTranslation();
	}

	@Override
	public String getName()
	{
		return "rod.translation";
	}
	
}