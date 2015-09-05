package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Polymer;
import willey.lib.util.StreamUtil;

class MaxDistance<P extends Polymer> implements Measurement<P, Double>
{
	@Override
	public Double apply(P pFrom)
	{
		return StreamUtil
				.nestedStream(pFrom.getMonomers(), pFrom.getMonomers())
				.map((pPair) -> Double.valueOf(pPair.getA().position()
						.distance(pPair.getB().position())))
				.max((d1, d2) -> Double.compare(d1, d2)).get();
	}

	public String getName()
	{
		return "max.distance";
	}
}