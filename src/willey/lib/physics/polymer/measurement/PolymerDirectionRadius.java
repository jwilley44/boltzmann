package willey.lib.physics.polymer.measurement;

import java.util.function.Function;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.Monomer;
import willey.lib.physics.polymer.interactor.PolymerAndRods;
import willey.lib.physics.polymer.interactor.Rods;
import willey.lib.util.Pair;
import willey.lib.util.StreamUtil;

class PolymerDirectionRadius<PR extends PolymerAndRods> implements
		Measurement<PR, Double>
{
	@Override
	public Double apply(PR pFrom)
	{
		Function<Monomer, CartesianVector> vPostion = (pMonomer) -> pMonomer.position();
		return StreamUtil.getIncrementedStream(
				pFrom.getMonomers().map(vPostion),
				pFrom.getMonomers().map(vPostion))
				.map((pPair) -> {
					CartesianVector vDirection = pPair.getA().subtract(
							pPair.getB());
					Double vCorrelation = Double.valueOf(rodCorrelation(
							pFrom, vDirection));
					return Pair.of(Double.valueOf(vDirection.magnitude()),
							vCorrelation);
				})
				.max((p1, p2) -> p1.getB().compareTo(p2.getB())).get().getA();
	}

	private double rodCorrelation(Rods pRods,
			CartesianVector pMonomerDirection)
	{
		double vCosTheta = pRods.getRods().mapToDouble((pRod) -> {
			double vCos = pRod.direction().cosTheta(pMonomerDirection);
			return vCos * vCos;
		}).sum();
		return Double.valueOf(vCosTheta / pRods.rodCount());
	}

	@Override
	public String getName()
	{
		return "polymer.direction.radius";
	}
}