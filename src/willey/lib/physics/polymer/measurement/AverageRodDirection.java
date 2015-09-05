package willey.lib.physics.polymer.measurement;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.CartesianVector.VectorSum;
import willey.lib.physics.polymer.interactor.Rods;

class AverageRodDirection<R extends Rods> implements
		Measurement<R, CartesianVector>
{
	@Override
	public CartesianVector apply(R pMeasurable)
	{
		VectorSum vSum = new VectorSum();
		pMeasurable.getRods()
				.map((pRod) -> orient(pRod.direction().unitVector()))
				.forEach(vSum);
		return vSum.getSum().scale(1.0 / (double) pMeasurable.rodCount())
				.unitVector();
	}

	private CartesianVector orient(CartesianVector pVector)
	{
		CartesianVector vVector = pVector;
		double vSign = Math.signum(vVector.z());
		if (vSign != 0)
		{
			vVector = vVector.scale(vSign);
		} else
		{
			vSign = Math.signum(vVector.x());
			if (vSign != 0)
			{
				vVector = vVector.scale(vSign);
			} else
			{
				vVector = vVector.scale(Math.signum(vVector.y()));
			}
		}
		return vVector;
	}

	@Override
	public String getName()
	{
		return "average.rod.direction";
	}
}