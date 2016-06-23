package willey.lib.physics.polymer.measurement;

import java.util.stream.Collectors;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.Rods;

class AverageRodDirection<R extends Rods> implements
		Measurement<R, CartesianVector>
{
	@Override
	public CartesianVector apply(R pMeasurable)
	{
		return MeasurementUtil.averageDirection(
				pMeasurable.getRods()
				.map(pRod -> pRod.direction().unitVector())
				.collect(Collectors.toList()));
	}

	@Override
	public String getName()
	{
		return "average.rod.direction";
	}
}