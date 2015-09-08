package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Polymer;

class PolymerSize<P extends Polymer> implements Measurement<P, Double>
{
	@Override
	public Double apply(P pFrom)
	{
		return Double.valueOf(pFrom.getSize());
	}

	public String getName()
	{
		return "polymer.size";
	}
}