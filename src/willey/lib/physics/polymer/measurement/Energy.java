package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Measurable;
import willey.lib.util.StreamUtil;

class Energy<M extends Measurable> implements Measurement<M, Double>
{

	@Override
	public Double apply(M pMeasurable)
	{
		return Double.valueOf(StreamUtil
			.getIncrementedStream(pMeasurable.getProjectedStream(), pMeasurable.getProjectedStream())
			.mapToDouble(pPair -> pPair.getA().energy(pPair.getB())).sum());
	}

	@Override
	public String getName()
	{
		return "energy";
	}
}