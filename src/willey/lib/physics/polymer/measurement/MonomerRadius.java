package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Polymer;

class MonomerRadius<P extends Polymer> implements Measurement<P, Double>
{

	@Override
	public Double apply(P pMeasurable)
	{
		return pMeasurable.getMonomers().map((pMonomer) -> Double.valueOf(pMonomer.interactionRadius())).findFirst().get();
	}

	@Override
	public String getName()
	{
		return "monomer.radius";
	}
	
}