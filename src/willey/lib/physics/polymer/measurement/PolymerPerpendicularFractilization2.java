package willey.lib.physics.polymer.measurement;

import static willey.lib.math.linearalgebra.CartesianVector.averageVector;
import java.util.List;
import java.util.stream.Collectors;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.PolymerAndRods;

public class PolymerPerpendicularFractilization2<PR extends PolymerAndRods>
		implements Measurement<PR, List<Double>>
{

	@Override
	public List<Double> apply(PR pMeasurable)
	{
		CartesianVector vRodDirection = pMeasurable.getRods()
						.map(pRod -> pRod.direction().unitVector())
						.collect(averageVector());
		List<CartesianVector> vMonomerPositions = pMeasurable.getMonomers()
				.map(pMonomer -> pMonomer.position())
				.collect(Collectors.toList());
		return MeasurementUtil.arcLenth2PependicularDistance(vRodDirection, vMonomerPositions);
	}

	@Override
	public String getName()
	{
		return "polymer.perpendicular.fractilization";
	}
}
