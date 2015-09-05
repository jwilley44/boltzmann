package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Rods;
import willey.lib.util.StreamUtil;

class AverageRodDistance<R extends Rods> implements
		Measurement<R, Double>
{

	@Override
	public Double apply(R pFrom)
	{
		double vDenominator = pFrom.rodCount()*pFrom.rodCount();
		return StreamUtil
				.nestedStream(pFrom.getRods(), pFrom.getRods())
				.mapToDouble(
						(pPair) -> pPair.getA().minimumDistance(
								pPair.getB())).sum()
				/ vDenominator;
	}

	@Override
	public String getName()
	{
		return "AverageRodDistance";
	}

}