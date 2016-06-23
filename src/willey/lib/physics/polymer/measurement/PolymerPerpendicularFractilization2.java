package willey.lib.physics.polymer.measurement;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.PolymerAndRods;

public class PolymerPerpendicularFractilization2<PR extends PolymerAndRods>
		implements Measurement<PR, List<Double>>
{

	@Override
	public List<Double> apply(PR pMeasurable)
	{
		CartesianVector vRodDirection = MeasurementUtil
				.averageDirection(pMeasurable.getRods()
						.map(pRod -> pRod.direction().unitVector())
						.collect(Collectors.toList()));
		List<CartesianVector> vMonomerPositions = pMeasurable.getMonomers()
				.map(pMonomer -> pMonomer.position())
				.collect(Collectors.toList());
		return ArcLength2Distance.calcDistances(new PerpendicularFunction(
				vRodDirection), vMonomerPositions);
	}

	@Override
	public String getName()
	{
		return "polymer.parallel.fractilization.2";
	}

	private static class PerpendicularFunction implements
			BiFunction<CartesianVector, CartesianVector, Double>
	{
		private final CartesianVector mDirection;

		PerpendicularFunction(CartesianVector pDirection)
		{
			mDirection = pDirection;
		}

		@Override
		public Double apply(CartesianVector pV1, CartesianVector pV2)
		{
			return Double.valueOf(pV1.subtract(pV2).projectOntoPlane(mDirection).magnitude());
		}
	}
}
