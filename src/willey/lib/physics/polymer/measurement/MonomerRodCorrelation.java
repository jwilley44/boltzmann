package willey.lib.physics.polymer.measurement;

import static willey.lib.math.linearalgebra.CartesianVector.averageVector;
import static willey.lib.physics.polymer.measurement.MeasurementUtil.correlation;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.PolymerAndRods;

public class MonomerRodCorrelation<PR extends PolymerAndRods> implements
		Measurement<PR, Double>
{

	@Override
	public Double apply(PR pFrom)
	{
		CartesianVector vAverageRodDirection = MeasurementUtil.averageRodDirection(pFrom);
		CartesianVector vAverageMonomerDirection = pFrom.getDirections().collect(averageVector());
		return Double.valueOf(correlation(vAverageMonomerDirection, vAverageRodDirection));
	}

	@Override
	public String getName()
	{
		return "monomer.rod.correlation";
	}
}
