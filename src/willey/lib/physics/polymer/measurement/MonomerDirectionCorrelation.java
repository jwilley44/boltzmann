package willey.lib.physics.polymer.measurement;

import java.util.stream.Collectors;

import willey.lib.physics.polymer.interactor.Polymer;

class MonomerDirectionCorrelation<P extends Polymer> implements
		Measurement<P, Double>
{
	@Override
	public Double apply(P pMeasurable)
	{
		return Double.valueOf(MeasurementUtil.orderParameter(pMeasurable
				.getDirections().collect(Collectors.toList())));
	}

	@Override
	public String getName()
	{
		return "monomer.direction.correlation";
	}
}