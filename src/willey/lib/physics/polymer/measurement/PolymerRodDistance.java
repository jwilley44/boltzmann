package willey.lib.physics.polymer.measurement;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.PolymerAndRods;

class PolymerRodDistance<PR extends PolymerAndRods> implements
		Measurement<PR, Double>
{
	@Override
	public Double apply(PR pMeasurable)
	{
		return pMeasurable
				.getRods()
				.map(pRod -> pRod.position())
				.collect(CartesianVector.averageVector(MeasurementUtil.polymerCenterOfMass(pMeasurable))).magnitude();
	}

	@Override
	public String getName()
	{
		return "polymer.rod.distance";
	}

}