package willey.lib.physics.polymer.interactor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.util.Pair;
import willey.lib.util.StreamUtil;

public class StaticPolymer implements Polymer
{
	private final int mSize;
	private final double mMonomerRadius;
	private final List<Monomer> mMonomers;
	private final int mStateId;
	
	StaticPolymer(List<CartesianVector> pMonomerPositions, double pMonomerRadius, int pStateId)
	{
		mMonomerRadius = pMonomerRadius;
		mSize = pMonomerPositions.size();
		mMonomers = pMonomerPositions.stream().map(pPosition -> new StaticMonomer(pPosition, mMonomerRadius)).collect(Collectors.toList());
		mStateId = pStateId;
	}
	
	@Override
	public Stream<? extends Interactor> getInteractors()
	{
		return mMonomers.stream();
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
	public int getSize()
	{
		return getMonomerCount() - 1;
	}

	@Override
	public double getEndToEndDistance()
	{
		return mMonomers.get(0).position().distance(mMonomers.get(mSize - 1).position());
	}

	@Override
	public double monomerRadius()
	{
		return mMonomerRadius;
	}

	@Override
	public Stream<Monomer> getMonomers()
	{
		return mMonomers.stream();
	}

	@Override
	public CartesianVector getDirection()
	{
		Monomer vFront = mMonomers.get(0);
		Monomer vEnd = mMonomers.get(getMonomerCount() - 1);
		return vEnd.position().subtract(vFront.position());
	}

	@Override
	public Stream<CartesianVector> getDirections()
	{
		Function<Pair<Monomer, Monomer>, CartesianVector> vDirection = (pPair) -> pPair
				.getA().position().subtract(pPair.getB().position());
		return StreamUtil.dualSream(getMonomers().skip(1),
				getMonomers().limit(getMonomerCount() - 1)).map(vDirection);
	}
	
	private int getMonomerCount()
	{
		return mSize;
	}
}
