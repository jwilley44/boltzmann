package willey.lib.physics.polymer.interactor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.experiment.Equilibrator.Equilibration;
import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.util.Check;
import willey.lib.util.Pair;
import willey.lib.util.StreamUtil;

public class PolymerImpl implements Polymer
{
	public enum Parameters
	{
		LatticeSize, Radius, Size;
	}
	
	private static final double kMonomerSeparation = 1.0;

	private final int mSize;
	private final double mMonomerRadius;
	private final List<Monomer> mMonomers;
	private final Lattice mLattice;

	public static Equilibration<Polymer> getEquilibration(ParameterMap pParameters)
	{
		PolymerImpl vPolymer = fromParameterMap(pParameters);
		return new PolymerEquilibration(vPolymer);
	}
	
	public static PolymerImpl fromParameterMap(ParameterMap pParameters)
	{
		int vSize = pParameters.getInt(Parameters.Size.name());
		double vRadius = pParameters.getDouble(Parameters.Radius.name());
		Lattice vLattice = Lattice.cubeLattice(pParameters.getInt(Parameters.LatticeSize.name()));
		return new PolymerImpl(vSize, vRadius, vLattice);
	}
	
	public PolymerImpl(int pSize, double pMonomerRadius, Lattice pLattice)
	{
		Check.kIllegalArgument.checkTrue(pSize > 2,
				"Polymer size must be greater than 2");
		Check.kIllegalArgument.checkTrue(pMonomerRadius >= 0
				&& pMonomerRadius <= 1.0,
				"Monomer radius must be in the interval [0, 1.0]");
		mSize = pSize + 1;
		mMonomerRadius = pMonomerRadius;
		mMonomers = createMonomers(pLattice.randomStart());
		mLattice = pLattice;
	}

	@Override
	public int getSize()
	{
		return getMonomerCount() - 1;
	}
	
	private int getMonomerCount()
	{
		return mSize;
	}

	@Override
	public double getEndToEndDistance()
	{
		Monomer vFront = mMonomers.get(0);
		Monomer vEnd = mMonomers.get(getMonomerCount() - 1);
		return vFront.position().distance(vEnd.position());
	}

	@Override
	public Stream<Monomer> getMonomers()
	{
		return mMonomers.stream();
	}
	
	public Stream<? extends Interactor> stream()
	{
		return mMonomers.stream();
	}

	@Override
	public Stream<? extends Interactor> getInteractors()
	{
		return getMonomers();
	}

	void replace(Monomer pOld, Monomer pNew)
	{
		int vIndex = mMonomers.indexOf(pOld);
		pOld.replace(pNew);
		mMonomers.set(vIndex, pNew);
	}

	Monomer chooseRandom()
	{
		return mMonomers.get(MathUtil.kRng.nextInt(getMonomerCount()));
	}

	double monomerRadius()
	{
		return mMonomerRadius;
	}
	
	PolymerImpl getMeasurableState()
	{
		return this;
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

	public Equilibration<Polymer> getEquilibration()
	{
		return new PolymerEquilibration(this);
	}
	
	public MovedInteractor testMoveRandom()
	{
		Monomer vOld = chooseRandom();
		return new MovedInteractor(vOld, vOld.randomMove());
	}

	private List<Monomer> createMonomers(CartesianVector pStartingPosition)
	{
		List<Monomer> vMonomers = new ArrayList<Monomer>();
		FrontMonomer vFrontMonomer = new FrontMonomer(pStartingPosition,
				monomerRadius(), null);
		vMonomers.add(vFrontMonomer);
		vMonomers.add(createNext(vFrontMonomer));

		for (int i = 2; i < getMonomerCount() - 1; i++)
		{
			vMonomers.add(createNext(vMonomers.get(i - 1)));
		}
		EndMonomer vEnd = createEndMonomer(vMonomers.get(vMonomers.size() - 1));
		vMonomers.get(vMonomers.size() - 1).setRightNeighbor(vEnd);
		vMonomers.add(vEnd);
		return vMonomers;
	}

	private MiddleMonomer createNext(Monomer pMonomer)
	{
		return new MiddleMonomer(nextPosition(pMonomer.position()),
				monomerRadius(), pMonomer);
	}

	private EndMonomer createEndMonomer(Monomer pMonomer)
	{
		return new EndMonomer(nextPosition(pMonomer.position()),
				monomerRadius(), pMonomer);
	}

	private CartesianVector nextPosition(CartesianVector pPreviousPosition)
	{
		CartesianVector vDirection = CartesianVector.randomUnitVector();
		return pPreviousPosition.add(vDirection.scale(kMonomerSeparation));
	}

	private static class PolymerEquilibration implements Equilibration<Polymer>
	{
		private final PolymerImpl mPolymer;

		private PolymerEquilibration(PolymerImpl pPolymer)
		{
			mPolymer = pPolymer;
		}
		
		@Override
		public Stream<Interactor> getTestPoints(Interactor pNew)
		{
			List<Interactor> vList = new ArrayList<Interactor>();
			vList.add(pNew.reposition(getLattice()
				.projectIntoLattice(pNew.position())));
			return vList.stream();
		}

		@Override
		public Stream<? extends Interactor> stream()
		{
			return mPolymer.getMonomers();
		}
		
//		@Override
//		public Stream<? extends Interactor> projectedStream(Interactor pOldInteractor)
//		{
//			return stream().filter((pInteractor) -> !pInteractor.equals(pOldInteractor));
//		}

		@Override
		public Interactor chooseRandom()
		{
			return mPolymer.chooseRandom();
		}
		
//		@Override
//		public Stream<Interactor> getTestPoints(Interactor pNew)
//		{
//			return StreamUtil.toStream(pNew);
//		}

		@Override
		public void replace(Interactor pOldInteractor, Interactor pNewInteractor)
		{
			mPolymer.replace((Monomer) pOldInteractor, (Monomer) pNewInteractor);
		}

		@Override
		public Lattice getLattice()
		{
			return mPolymer.mLattice;
		}

		@Override
		public Polymer getMeasurableState()
		{
			return mPolymer;
		}

		@Override
		public MovedInteractor testMoveRandom()
		{
			return mPolymer.testMoveRandom();
		}
	}

	@Override
	public Lattice getLattice()
	{
		return mLattice;
	}
}
