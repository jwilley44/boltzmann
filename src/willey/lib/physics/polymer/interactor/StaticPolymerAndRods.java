package willey.lib.physics.polymer.interactor;

import java.util.stream.Stream;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.util.StreamUtil;

public class StaticPolymerAndRods implements PolymerAndRods
{
	private final Polymer mPolymer;
	private final Rods mRods;
	private final int mStateId;
	
	public StaticPolymerAndRods(Polymer pPolymer, Rods pRods, int pStateId)
	{
		mPolymer = pPolymer;
		mRods = pRods;
		mStateId = pStateId;
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
	public double monomerRadius()
	{
		return mPolymer.monomerRadius();
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

	@Override
	public Stream<? extends Interactor> getInteractors()
	{
		return StreamUtil.combine(mPolymer.getInteractors(),
				mRods.getInteractors());
	}

	@Override
	public Lattice getLattice()
	{
		return mRods.getLattice();
	}

	@Override
	public int stateId()
	{
		return mStateId;
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
	public double rodRotation()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public double rodTranslation()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String startingState()
	{
		return "Unknown";
	}

	@Override
	public double polymerMoveWeight()
	{
		return -1.0;
	}

}
