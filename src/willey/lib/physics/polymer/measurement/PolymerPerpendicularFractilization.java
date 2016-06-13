package willey.lib.physics.polymer.measurement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.PolymerAndRods;

public class PolymerPerpendicularFractilization<PR extends PolymerAndRods> implements
Measurement<PR, List<Double>>
{

	@Override
	public List<Double> apply(PR pMeasurable)
	{
		List<List<Double>> vDistances = new ArrayList<List<Double>>();
		List<CartesianVector> vPositions =  pMeasurable.getInteractors()
				.map((pInteractor) -> pInteractor.position()).collect(Collectors.toList());
		CartesianVector vAverageRodDirection = Measurements.averageRodDirection().apply(pMeasurable);
		for (int i = 0; i < pMeasurable.getSize() - 1; i++)
		{
			for (int j = i + 1; j < pMeasurable.getSize(); j++)
			{
				int vStep = j - i;
				List<Double> vStepDistance;
				if (vDistances.size() < vStep)
				{
					vStepDistance = new ArrayList<Double>();
					vDistances.add(vStepDistance);
				} else
				{
					vStepDistance = vDistances.get(vStep - 1);
				}
				vStepDistance.add(vPositions.get(i).subtract(
						vPositions.get(j)).projectOntoPlane(vAverageRodDirection).magnitude());
			}
		}

		List<Double> vAverageDistances = new ArrayList<Double>();
		for (List<Double> vStepDistance : vDistances)
		{
			vAverageDistances.add(Double.valueOf(MathUtil
					.sum(vStepDistance) / vStepDistance.size()));
		}
		return vAverageDistances;
	}

	@Override
	public String getName()
	{
		return "polymer.perpendicular.fractilization";
	}

}
