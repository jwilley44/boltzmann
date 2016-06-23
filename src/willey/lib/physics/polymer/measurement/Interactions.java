package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Interactor;
import willey.lib.physics.polymer.interactor.Measurable;
import willey.lib.util.StreamUtil;

class Interactions<M extends Measurable> implements Measurement<M, Long>
{
	@Override
	public Long apply(M pFrom)
	{
		return Long.valueOf(StreamUtil
				.getIncrementedStream(pFrom.getInteractors(), pFrom.getInteractors())
				.filter(pPair -> interacts(pPair.getA(), pPair.getB())).count());
	}

	boolean interacts(Interactor p1, Interactor p2)
	{
		return p1.getDistance(p2) <= p1.interactionDistance(p2);
	}

	@Override
	public String getName()
	{
		return "interactions";
	}
}