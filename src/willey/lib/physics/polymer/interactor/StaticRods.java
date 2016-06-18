package willey.lib.physics.polymer.interactor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import willey.lib.math.linearalgebra.LineSegment;

public class StaticRods implements Rods
{
	
	private final List<Rod> mRods;
	private final int mStateId;
	private final long mLatticeVolume;
	
	public StaticRods(List<LineSegment> pRods, double pRodRadius, int pStateId, long pLatticeVolume)
	{
		mRods = pRods.stream().map(pLineSegment -> new Rod(pLineSegment, pRodRadius)).collect(Collectors.toList());
		mStateId = pStateId;
		mLatticeVolume = pLatticeVolume;
	}

	@Override
	public Stream<? extends Interactor> getInteractors()
	{
		return mRods.stream();
	}

	@Override
	public Lattice getLattice()
	{
		return null;
	}

	@Override
	public int stateId()
	{
		return mStateId;
	}

	@Override
	public Stream<Rod> getRods()
	{
		return mRods.stream();
	}

	@Override
	public int rodCount()
	{
		return mRods.size();
	}

	@Override
	public double rodsVolume()
	{
		double vTotal = 0;
		for (Rod vRod : mRods)
		{
			vTotal += vRod.volume();
		}
		return vTotal / mLatticeVolume;
	}

	@Override
	public double rodRotation()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public double rodTranslation()
	{
		throw new UnsupportedOperationException();
	}
}
