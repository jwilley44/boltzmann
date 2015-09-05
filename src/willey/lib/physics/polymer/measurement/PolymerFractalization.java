package willey.lib.physics.polymer.measurement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.Polymer;
import willey.lib.util.ConsumerUtil;

class PolymerFractalization<P extends Polymer> implements
		Measurement<P, List<Double>>
{
	@Override
	public List<Double> apply(P pMeasurable)
	{
		List<List<Double>> vDistances = new ArrayList<List<Double>>();
		Stream<CartesianVector> vDirectionStream = pMeasurable.getInteractors()
				.map((pInteractor) -> pInteractor.position());
		List<CartesianVector> vPositions = ConsumerUtil.toCollection(vDirectionStream, new ArrayList<CartesianVector>());
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
				vStepDistance.add(vPositions.get(i).distance(
						vPositions.get(j)));
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
		return "PolymerFractilization";
	}
}