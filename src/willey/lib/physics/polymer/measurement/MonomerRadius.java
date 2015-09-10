package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Polymer;

class MonomerRadius<P extends Polymer> implements Measurement<P, Double>
{

	@Override
	public Double apply(P pMeasurable)
	{
		return Double.valueOf(pMeasurable.getMonomers().mapToDouble((pMonomer) -> pMonomer.interactionRadius()).sum() / pMeasurable.getSize());
	}

	@Override
	public String getName()
	{
		return "monomer.radius";
	}
	
}