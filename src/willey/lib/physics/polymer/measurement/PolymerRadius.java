package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Polymer;

class PolymerRadius<P extends Polymer> implements Measurement<P, Double>
{
	@Override
	public Double apply(P pFrom)
	{
		return Double.valueOf(pFrom.getEndToEndDistance());
	}

	public String getName()
	{
		return "PolymerRadius";
	}
}