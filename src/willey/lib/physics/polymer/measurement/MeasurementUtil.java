package willey.lib.physics.polymer.measurement;

import java.util.function.Consumer;
import java.util.stream.Stream;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.Interactor;
import willey.lib.physics.polymer.interactor.Measurable;
import willey.lib.util.StreamUtil;

public class MeasurementUtil
{
	static class CountInteractions implements Consumer<Interactor>
	{
		private int mIndex = 1;
		private int mInteractions = 0;
		private final Measurable mInteractors;
	
		CountInteractions(Measurable pInteractors)
		{
			mInteractors = pInteractors;
			mInteractors.getInteractors().forEach(this);
		}
	
		@Override
		public void accept(final Interactor pInteractor)
		{
			mInteractions = mInteractors.getInteractors().skip(mIndex).anyMatch(
					(p) -> pInteractor.interactionDistance(p) > 0) ? mInteractions + 1
					: mInteractions;
			mIndex++;
		}
	
		public int interactions()
		{
			return mInteractions;
		}
	}

	static double correlation(Stream<CartesianVector> pVectors1, Stream<CartesianVector> pVectors2, 
			int pCount)
	{
		double vDenominator = pCount*(pCount-1)/2;
		return StreamUtil.getIncrementedStream(pVectors1, pVectors2)
				.mapToDouble((pPair) -> correlation(pPair.getA(), pPair.getB()))
				.sum()/ vDenominator;
	}

	static double correlation(CartesianVector pA, CartesianVector pB)
	{
		double vCos = pA.cosTheta(pB);
		return Math.sqrt(vCos * vCos);
	}
}
