package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Measurable;

class Hash<M extends Measurable> implements Measurement<M, Integer>
{
	@Override
	public Integer apply(Measurable pMeasurable)
	{
		return Integer.valueOf(pMeasurable.hashCode());
	}

	@Override
	public String getName()
	{
		return "hash";
	}

}