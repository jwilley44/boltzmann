package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Measurable;

class Interactions<M extends Measurable> implements
		Measurement<M, Integer>
{
	@Override
	public Integer apply(M pFrom)
	{
		return Integer
				.valueOf(new MeasurementUtil.CountInteractions(pFrom)
						.interactions());
	}

	@Override
	public String getName()
	{
		return "Interactions";
	}
}