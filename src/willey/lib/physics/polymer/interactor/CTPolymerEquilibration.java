package willey.lib.physics.polymer.interactor;

import java.util.stream.Stream;

import willey.lib.datastructures.CoverTree;
import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianMetric;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.experiment.Equilibrator.Equilibration;
import willey.lib.physics.polymer.measurement.Measurements;

public class CTPolymerEquilibration implements Equilibration<Polymer>
{
	private final Polymer mPolymer;
	private final CoverTree<CartesianVector> mCoverTree;
	private final double mInteractionDistance;
	
	public CTPolymerEquilibration(Polymer pPolymer)
	{
		mPolymer = pPolymer;
		mCoverTree = new CoverTree<CartesianVector>(new CartesianMetric(), Measurements.polymerPosition().apply(mPolymer), 1.5);
		mInteractionDistance = mPolymer.monomerRadius();
	}
	
	@Override
	public Stream<? extends Interactor> stream()
	{
		return mPolymer.getInteractors();
	}

	@Override
	public Monomer chooseRandom()
	{
		//TODO fix this
		return null;
	}

	@Override
	public MovedInteractor testMoveRandom()
	{
		Monomer vOld = chooseRandom();
		return new MovedInteractor(vOld, vOld.randomMove());
	}

	@Override
	public void replace(Interactor pOldInteractor, Interactor pNewInteractor)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Lattice getLattice()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polymer getMeasurableState()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean randomMove()
	{
		MovedInteractor vMoved = testMoveRandom();
		Interactor vOld = vMoved.getOldInteractor();
		Interactor vNew = vMoved.getNewInteractor();
		mCoverTree.remove(vOld.position());
		boolean vValidMove = getTestPoints(vNew).noneMatch(
				pPoint -> mCoverTree.getNearesetNeighbor(pPoint.position())
						.getB().doubleValue() < mInteractionDistance);
		if (vValidMove)
		{
			replace(vOld, vNew);
			mCoverTree.remove(vOld.position());
			mCoverTree.insert(vNew.position());
		}
		else
		{
			mCoverTree.insert(vOld.position());
		}
		return vValidMove;
	}
}
