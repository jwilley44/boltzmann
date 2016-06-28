package willey.lib.physics.polymer.measurement;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.Rods;

class AverageRodDistance<R extends Rods> implements Measurement<R, Double>
{

	@Override
	public Double apply(R pFrom)
	{
		return Double.valueOf(pFrom.getRods().map(pRod -> pRod.position())
				.collect(CartesianVector.averageVector()).magnitude());
	}

	@Override
	public String getName()
	{
		return "average.rod.distance";
	}

}