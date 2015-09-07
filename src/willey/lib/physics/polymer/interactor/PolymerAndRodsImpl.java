package willey.lib.physics.polymer.interactor;

import java.util.stream.Stream;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.experiment.Equilibrator.Equilibration;
import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.util.StreamUtil;

public class PolymerAndRodsImpl implements PolymerAndRods
{
	private int mEquilibrations = 0;
	
	public int equilibrations()
	{
		return ++mEquilibrations;
	}
	
	public enum Parameter
	{
		PolymerWeight;
	}

	private final PolymerImpl mPolymer;
	private final RodsImpl mRods;

	public static Equilibration<PolymerAndRods> getEquilibration(
			ParameterMap pParameters)
	{
		PolymerAndRodsImpl vPolymerAndRods = fromParameterMap(pParameters);
		return new PolymerAndRodsEquilibration(vPolymerAndRods,
				pParameters.getDouble("PolymerWeight"));
	}

	public static PolymerAndRodsImpl fromParameterMap(ParameterMap pParameters)
	{
		return new PolymerAndRodsImpl(
				PolymerImpl.fromParameterMap(pParameters),
				RodsImpl.fromParameterMap(pParameters));
	}

	public PolymerAndRodsImpl(PolymerImpl pPolymer, RodsImpl pRods)
	{
		mPolymer = pPolymer;
		mRods = pRods;
	}

	@Override
	public Stream<? extends Interactor> getInteractors()
	{
		return StreamUtil.combine(mPolymer.getInteractors(),
				mRods.getInteractors());
	}
	
	@Override
	public double rodRotation()
	{
		return mRods.rodRotation();
	}
	
	@Override
	public double rodTranslation()
	{
		return mRods.rodTranslation();
	}

	@Override
	public Stream<Rod> getRods()
	{
		return mRods.getRods();
	}

	@Override
	public int rodCount()
	{
		return mRods.rodCount();
	}

	@Override
	public double rodsVolume()
	{
		return mRods.rodsVolume();
	}

	@Override
	public int getSize()
	{
		return mPolymer.getSize();
	}

	@Override
	public double getEndToEndDistance()
	{
		return mPolymer.getEndToEndDistance();
	}

	@Override
	public Stream<Monomer> getMonomers()
	{
		return mPolymer.getMonomers();
	}

	@Override
	public CartesianVector getDirection()
	{
		return mPolymer.getDirection();
	}

	@Override
	public Stream<CartesianVector> getDirections()
	{
		return mPolymer.getDirections();
	}

	Monomer chooseRandomMonomer()
	{
		return mPolymer.chooseRandom();
	}

	Rod chooseRandomRod()
	{
		return mRods.chooseRandom();
	}

	void replaceMonomer(Monomer pOldMonomer, Monomer pNewMonomer)
	{
		mPolymer.replace(pOldMonomer, pNewMonomer);
	}

	void replaceRod(Rod pOldRod, Rod pNewRod)
	{
		mRods.replace(pOldRod, pNewRod);
	}
	
	PolymerAndRodsImpl getMeasurableState()
	{
		
		return new PolymerAndRodsImpl(mPolymer.getMeasurableState(), mRods.getMeasurableState());
	}

	private static class PolymerAndRodsEquilibration implements
			Equilibration<PolymerAndRods>
	{
		private final PolymerAndRodsImpl mPolymerAndRods;
		private final double mPolymerWeight;

		private boolean mRandomWasMonomer;

		public PolymerAndRodsEquilibration(PolymerAndRodsImpl pPolymerAndRods,
				double pPolymerWeight)
		{
			mPolymerAndRods = pPolymerAndRods;
			mPolymerWeight = pPolymerWeight;
		}

		@Override
		public Stream<? extends Interactor> stream()
		{
			return StreamUtil.combine(mPolymerAndRods.mRods.stream(), mPolymerAndRods.mPolymer.getInteractors());
		}

		@Override
		public Interactor chooseRandom()
		{
			mRandomWasMonomer = MathUtil.kRng.nextDouble() <= mPolymerWeight;
			return mRandomWasMonomer ? mPolymerAndRods.chooseRandomMonomer()
					: mPolymerAndRods.chooseRandomRod();
		}

		@Override
		public void replace(Interactor pOldInteractor, Interactor pNewInteractor)
		{
			if (mRandomWasMonomer)
			{
				mPolymerAndRods.replaceMonomer((Monomer) pOldInteractor,
						(Monomer) pNewInteractor);
			} else
			{
				mPolymerAndRods.replaceRod((Rod) pOldInteractor,
						(Rod) pNewInteractor);
			}
		}

		@Override
		public Lattice getLattice()
		{
			return mPolymerAndRods.mRods.getLattice();
		}

		@Override
		public PolymerAndRods getMeasurableState()
		{
			return mPolymerAndRods.getMeasurableState();
		}

		@Override
		public MovedInteractor testMoveRandom()
		{
			mRandomWasMonomer = MathUtil.kRng.nextDouble() <= mPolymerWeight;
			return mRandomWasMonomer ? mPolymerAndRods.mPolymer.testMoveRandom()
					: mPolymerAndRods.mRods.testMoveRandom();
		}
	}

	@Override
	public Lattice getLattice()
	{
		return mRods.getLattice();
	}
}
