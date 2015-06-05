package willey.lib.physics.polymer.interactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.LineSegment;
import willey.lib.physics.polymer.experiment.Equilibrator.Equilibration;
import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.physics.polymer.interactor.RodsUtil.Orientation;
import willey.lib.physics.polymer.interactor.RodsUtil.Position;

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
	private final double mTranslation;
	private final double mRotation;

	public static Equilibration<Rods> getEquilibration(ParameterMap pParameters)
	{
		RodsImpl vRods = fromParameterMap(pParameters);
		return new RodEquilibration(vRods);
	}
	
	Equilibration<Rods> getEquilibration()
	{
		return new RodEquilibration(this);
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
		mRotation = pRotation;
		mTranslation = pTranslation;
	}
	
	RodsImpl(List<Rod> pRods, Lattice pLattice, double pTranslation, double pRotation)
	{
		mRods = pRods;
		mLattice = pLattice;
		mRodCount = mRods.size();
		mVolume = pLattice.volume();
		mTranslation = pTranslation;
		mRotation = pRotation;
	}

	@Override
	public Stream<Rod> getRods()
	{
		return mRods.stream().map((pRod) -> pRod.reposition(getLattice().projectIntoLattice(pRod.position())));
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
	
	public MovedInteractor testMoveRandom()
	{
		Rod vOld = chooseRandom();
		Rod vNew = randomMove(vOld);
		return new MovedInteractor(vOld,  vNew);
	}
	
	private Rod randomMove(Rod pRod)
	{
		LineSegment vNewLineSegment = pRod.getLineSegment().translate(
				CartesianVector.randomUnitVector().scale(mTranslation)).rotate(
				mRotation);
		return pRod.move(vNewLineSegment);
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
	
	@Override
	public double rodRotation()
	{
		return mRotation;
	}
	
	@Override
	public double rodTranslation()
	{
		return mTranslation;
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

	public Lattice getLattice()
	{
		return mLattice;
	}
	
	RodsImpl getMeasurableState()
	{
		
		List<Rod> vRods = mRods.stream().map((pRod) -> pRod.reposition(getLattice().projectIntoLattice(pRod.position()))).collect(Collectors.toList());
		return new RodsImpl(vRods, mLattice, mTranslation, mRotation);
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
			return mRods.stream();
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
			return mRods.getMeasurableState();
		}

		@Override
		public MovedInteractor testMoveRandom()
		{
			return mRods.testMoveRandom();
		}
	}
}
