package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Rods;
import willey.lib.util.StreamUtil;

class MaxRodDistance<R extends Rods> implements Measurement<R, Double>
{
	@Override
	public Double apply(R pFrom)
	{
		return Double.valueOf(StreamUtil
		.nestedStream(pFrom.getRods(), pFrom.getRods())
		.mapToDouble(
				(pPair) -> pPair.getA().minimumDistance(
						pPair.getB())).max().getAsDouble());
	}
	
	@Override
	public String getName()
	{
		return "max.rod.distance";
	}
}