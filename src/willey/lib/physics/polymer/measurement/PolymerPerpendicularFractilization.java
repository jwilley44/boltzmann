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
		List<CartesianVector> vRodDirections = pMeasurable.getRods().map(pRod -> pRod.direction().unitVector()).collect(Collectors.toList());
		List<CartesianVector> vMonomerPositions = pMeasurable.getMonomers().map(pMonomer -> pMonomer.position()).collect(Collectors.toList());
		List<List<Double>> vDistances = new ArrayList<List<Double>>();
		for (int i=0; i < pMeasurable.getSize() - 1; i++)
		{
			for (int j=i; j < pMeasurable.getSize(); j++)
			{
				int vStep = j - i;
				if (j == i)
				{
					vDistances.add(new ArrayList<Double>());
				} else
				{
					List<Double> vStepDistance = vDistances.get(vStep - 1);
					vStepDistance.add(MeasurementUtil.perpendicularMagnitude(vMonomerPositions.get(i).subtract(
						vMonomerPositions.get(j)), vRodDirections));
				} 
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
