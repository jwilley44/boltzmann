package willey.lib.physics.polymer.experiment;

import java.util.function.Predicate;

import willey.lib.physics.polymer.interactor.Interactors;
import willey.lib.physics.polymer.interactor.Measurable;
import willey.lib.physics.polymer.measurement.Measurements;


public class Equilibrator<M extends Measurable>
{
	private int mTotalMoves = 0;
	private int mValidMoves = 0;
	
	private int mAgregatedTotal = 0;
	private int mAgregatedValid = 0;

	private final Equilibration<? extends M> mEqulibration;
	private final Predicate<Equilibrator<M>> mTerminationCriteria;

	public Equilibrator(Equilibration<? extends M> pEqulibration,
			Predicate<Equilibrator<M>> pTerminationCriteria)
	{
		mEqulibration = pEqulibration;
		mTerminationCriteria = pTerminationCriteria;
	}

	public int totalMoves()
	{
		return mTotalMoves;
	}

	public int validMoves()
	{
		return mValidMoves;
	}

	private void addToTotal()
	{
		mTotalMoves++;
	}

	private void addValidMove()
	{
		mValidMoves++;
	}

	private boolean move() throws Exception
	{
		return mEqulibration.randomMove();
	}

	public double getInteractions()
	{
		return Measurements.interactions().apply(mEqulibration.getMeasurableState()).doubleValue();
	}

	public boolean isEquilibrated() throws Exception
	{
		return mTerminationCriteria.test(this);
	}

	public EquilibrationResult<M> getState()
	{
		return new EquilibrationResult<M>(mEqulibration.getMeasurableState(), mValidMoves,
				mTotalMoves);
	}
	
	private void finish()
	{
		mAgregatedTotal += mTotalMoves;
		mTotalMoves = 0;
		mAgregatedValid += mValidMoves;
		mValidMoves = 0;
	}

	public EquilibrationResult<M> equilibrate()
	{
		EquilibrationResult<M> vResult = null;
		try
		{
			while (!isEquilibrated())
			{
				if (move())
				{
					addValidMove();
				}
				addToTotal();
			}
			finish();
			vResult = new EquilibrationResult<M>(mEqulibration.getMeasurableState(), mAgregatedValid,
					mAgregatedTotal);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		return vResult;
	}

	public static class EquilibrationResult<M extends Measurable>
	{
		private final M mInteractors;
		private final int mValidMoves;
		private final int mTotalMoves;

		private EquilibrationResult(M pInteractors, int pValidMoves,
				int pTotalMoves)
		{
			mInteractors = pInteractors;
			mValidMoves = pValidMoves;
			mTotalMoves = pTotalMoves;
		}

		public int equilibrationTime()
		{
			return mTotalMoves;
		}

		public int getValidMoves()
		{
			return mValidMoves;
		}

		public M getInteractors()
		{
			return mInteractors;
		}
	}
	
	public interface Equilibration<M extends Measurable> extends Interactors
	{
		M getMeasurableState();
	}
}
