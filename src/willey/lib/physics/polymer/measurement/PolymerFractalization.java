package willey.lib.physics.polymer.measurement;

import java.util.List;
import java.util.stream.Collectors;

import willey.lib.physics.polymer.interactor.Polymer;

class PolymerFractalization<P extends Polymer> implements
		Measurement<P, List<Double>>
{
	@Override
	public List<Double> apply(P pMeasurable)
	{
		return MeasurementUtil.arcLength2Distance(pMeasurable.getDirections().collect(Collectors.toList()));
	}

	@Override
	public String getName()
	{
		return "polymer.fractilization";
	}
}