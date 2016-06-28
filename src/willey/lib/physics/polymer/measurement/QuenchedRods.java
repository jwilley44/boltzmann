package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.PolymerAndRods;

public class QuenchedRods<PR extends PolymerAndRods> implements Measurement<PR, Boolean>
{

	@Override
	public Boolean apply(PR pMeasurable)
	{
		return Boolean.valueOf(pMeasurable.polymerMoveWeight() == 1.0);
	}

	@Override
	public String getName()
	{
		return "quenched.rods";
	}

}
