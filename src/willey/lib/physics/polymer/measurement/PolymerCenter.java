package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Polymer;

class PolymerCenter<P extends Polymer> implements Measurement<P, Double>
{
	public Double apply(P pMeasurable)
	{
		return Double.valueOf(pMeasurable.getLattice().centerStart()
				.distance(MeasurementUtil.polymerCenterOfMass(pMeasurable)));
	}

	@Override
	public String getName()
	{
		return "polymer.center";
	}
}
