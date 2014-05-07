package willey.lib.physics.polymer.interactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import willey.lib.math.MathUtil;
import willey.lib.physics.polymer.experiment.Equilibrator.Equilibration;
import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.physics.polymer.interactor.RodsUtil.Orientation;
import willey.lib.physics.polymer.interactor.RodsUtil.Position;
import willey.lib.physics.polymer.lattice.Lattice;

public class RodsImpl implements Rods
{
	public enum Parameter
	{
		LatticeSize,
		RodCount, 
		RodLength, 
		RodOrientation, 
		RodPosition, 
		RodRadius, 
		RodRotation, 
		RodTranslation
	}

	private final List<Rod> mRods;
	private final int mRodCount;
	private final Lattice mLattice;
	private final long mVolume;

	public static Equilibration<Rods> getEquilibration(ParameterMap pParameters)
	{
		RodsImpl vRods = fromParameterMap(pParameters);
		return new RodEquilibration(vRods);
	}
	
	public static RodsImpl fromParameterMap(ParameterMap pParameters)
	{
		int vCount = pParameters.getInt(Parameter.RodCount.name());
		double vLength = pParameters.getDouble(Parameter.RodLength.name());
		double vRadius = pParameters.getDouble(Parameter.RodRadius.name());
		double vTranslation = pParameters.getDouble(Parameter.RodTranslation
				.name());
		double vRotation = pParameters.getDouble(Parameter.RodRotation.name());
		Lattice vLattice = Lattice.cubeLattice(pParameters
				.getInt(Parameter.LatticeSize.name()));
		Orientation vOrientation = Orientation.valueOf(pParameters
				.getString(Parameter.RodOrientation.name()));
		Position vPosition = Position.valueOf(pParameters
				.getString(Parameter.RodPosition.name()));
		return new RodsImpl(vCount, vLength, vRadius, vTranslation, vRotation,
				vLattice, vOrientation, vPosition);
	}
	
	public RodsImpl(int pCount, double pLength, double pRadius,
			double pTranslation, double pRotation, Lattice pLattice,
			Orientation pOrientation, Position pPosition)
	{
		mRods = RodsUtil.getRods(pCount, pLength, pRadius, pTranslation,
				pRotation, pLattice, pOrientation, pPosition);
		mLattice = pLattice;
		mVolume = mLattice.volume();
		mRodCount = mRods.size();
	}

	@Override
	public Stream<Rod> getRods()
	{
		return mRods.stream();
	}

	public synchronized Collection<Interactor> excludeAndGet(
			Interactor pInteractor)
	{
		List<Interactor> vRods = new ArrayList<Interactor>(mRods);
		vRods.remove(pInteractor);
		return vRods;
	}

	public Rod chooseRandom()
	{
		int vIndex = MathUtil.kRng.nextInt(rodCount());
		return mRods.get(vIndex);
	}

	@Override
	public int rodCount()
	{
		return mRodCount;
	}

	public void replace(Rod pOldRod, Rod pNewRod)
	{
		int vIndex = mRods.indexOf(pOldRod);
		mRods.remove(vIndex);
		mRods.add(vIndex, pNewRod);
	}

	@Override
	public double rodsVolume()
	{
		double vTotal = 0;
		for (Rod vRod : mRods)
		{
			vTotal += vRod.volume();
		}
		return vTotal / mVolume;
	}

	public Stream<Rod> stream()
	{
		return mRods.stream();
	}

	@Override
	public Stream<? extends Interactor> getInteractors()
	{
		return getRods();
	}

	Lattice getLattice()
	{
		return mLattice;
	}

	private static class RodEquilibration implements Equilibration<Rods>
	{
		private final RodsImpl mRods;

		private RodEquilibration(RodsImpl pRods)
		{
			mRods = pRods;
		}

		@Override
		public Stream<? extends Interactor> stream()
		{
			return mRods.getInteractors();
		}

		@Override
		public Interactor chooseRandom()
		{
			return mRods.chooseRandom();
		}

		@Override
		public void replace(Interactor pOldInteractor, Interactor pNewInteractor)
		{
			mRods.replace((Rod) pOldInteractor, (Rod) pNewInteractor);
		}

		@Override
		public Lattice getLattice()
		{
			return mRods.mLattice;
		}

		@Override
		public Rods getMeasurableState()
		{
			return mRods;
		}
	}
}
