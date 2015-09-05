package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Polymer;
import willey.lib.util.StreamUtil;

class AverageMonomerDistance<P extends Polymer> implements
		Measurement<P, Double>
{
	@Override
	public Double apply(P pFrom)
	{
		double vSum = StreamUtil.getIncrementedStream(pFrom.getMonomers(), pFrom.getMonomers())
				.mapToDouble(
						(pPair) -> pPair.getA().position()
								.distance(pPair.getB().position())).sum();
		return Double.valueOf(vSum / pFrom.getSize() * pFrom.getSize() / 2
				- pFrom.getSize());
	}

	public String getName()
	{
		return "monomer.distance";
	}
}