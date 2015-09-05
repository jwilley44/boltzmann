package willey.lib.physics.polymer.measurement;

import static willey.lib.physics.polymer.measurement.MeasurementUtil.correlation;
import willey.lib.physics.polymer.interactor.Polymer;
import willey.lib.util.StreamUtil;

class MonomerDirectionCorrelation<P extends Polymer> implements
		Measurement<P, Double>
{
	@Override
	public Double apply(P pMeasurable)
	{
		int vSize = pMeasurable.getSize();
		double vSum = StreamUtil
				.dualSream(pMeasurable.getDirections().limit(vSize - 1),
						pMeasurable.getDirections().skip(1))
				.mapToDouble(
						(pPair) -> correlation(pPair.getA(), pPair.getB()))
				.sum();
		return Double.valueOf(vSum / (vSize - 2));
	}

	@Override
	public String getName()
	{
		return "MonomerDirectionCorrelation";
	}
}