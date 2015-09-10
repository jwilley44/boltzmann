package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Rods;

class NearestRod<R extends Rods> implements Measurement<R, Double>
{
	@Override
	public Double apply(R pFrom)
	{
		double vSum = pFrom
				.getRods()
				.map((pRod1) -> pFrom
						.getRods().filter((pRod2) -> !pRod2.equals(pRod1))
						.map((pRod2) -> Double.valueOf(pRod1
								.minimumDistance(pRod2)))
						.min(Double::compare))
				.mapToDouble((pOptional) -> pOptional.get().doubleValue())
				.sum();
		return Double.valueOf(vSum / pFrom.rodCount());
	}

	@Override
	public String getName()
	{
		return "nearest.rod";
	}
}