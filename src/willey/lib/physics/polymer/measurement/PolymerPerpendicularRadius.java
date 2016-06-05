package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.PolymerAndRods;

public class PolymerPerpendicularRadius<PR extends PolymerAndRods> implements
Measurement<PR, Double>
{

	@Override
	public Double apply(PR pMeasurable)
	{
		return pMeasurable.getDirection().projectOntoPlane(Measurements.averageRodDirection().apply(pMeasurable)).magnitude();
	}

	@Override
	public String getName()
	{
		return "polymer.perpendicular.radius";
	}

}
