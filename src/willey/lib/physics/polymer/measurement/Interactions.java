package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Measurable;
import willey.lib.util.StreamUtil;

class Interactions<M extends Measurable> implements Measurement<M, Long>
{
	@Override
	public Long apply(M pFrom)
	{
		return Long.valueOf(StreamUtil
				.getIncrementedStream(pFrom.getInteractors(), pFrom.getInteractors())
				.filter(pPair -> pPair.getA().interacts(pPair.getB())).count());
	}

	@Override
	public String getName()
	{
		return "interactions";
	}
}