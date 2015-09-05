package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.PolymerAndRods;

class PolymerRodDistance<PR extends PolymerAndRods> implements
		Measurement<PR, Double>
{
	@Override
	public Double apply(PR pMeasurable)
	{
		double vSum = pMeasurable
				.getMonomers()
				.map((pMonomer) -> pMeasurable
						.getRods()
						.map((pRod) -> Double.valueOf(pRod
								.minimumDistance(pMonomer.position())))
						.min(Double::compare))
				.mapToDouble((pOptional) -> pOptional.get().doubleValue())
				.sum();
		return Double.valueOf(vSum / pMeasurable.getSize());
	}

	@Override
	public String getName()
	{
		return "PolymerRodDistance";
	}

}