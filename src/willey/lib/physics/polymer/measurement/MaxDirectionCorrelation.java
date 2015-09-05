package willey.lib.physics.polymer.measurement;

import static willey.lib.physics.polymer.measurement.MeasurementUtil.correlation;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.PolymerAndRods;
import willey.lib.util.Pair;
import willey.lib.util.StreamUtil;

class MaxDirectionCorrelation<PR extends PolymerAndRods> implements
		Measurement<PR, Double>
{
	private static final Measurement<PolymerAndRods, CartesianVector> kAverageRodDirection = Measurements.averageRodDirection();
	
	@Override
	public Double apply(PR pMeasurable)
	{
		CartesianVector vRodDirection = kAverageRodDirection
				.apply(pMeasurable);
		CartesianVector vMaxDirection = StreamUtil
				.nestedStream(pMeasurable.getMonomers(),
						pMeasurable.getMonomers())
				.map((pPair) -> {
					CartesianVector vDirection = pPair.getB().position()
							.subtract(pPair.getA().position());
					Double vDistance = Double.valueOf(vDirection
							.magnitude());
					return Pair.of(vDirection, vDistance);
				})
				.max((p1, p2) -> Double.compare(p1.getB().doubleValue(), p2
						.getB().doubleValue())).get().getA();
		return Double.valueOf(correlation(vRodDirection, vMaxDirection));
	}

	@Override
	public String getName()
	{
		return "max.direction.correlation";
	}
}