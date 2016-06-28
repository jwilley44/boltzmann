package willey.lib.physics.polymer.interactor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.SegmentUtil;
import willey.lib.math.linearalgebra.SegmentUtil.Segment;
import willey.lib.util.ConsumerUtil;

public class InteractorsTest
{
//	@Test
//	public void testProjectedStream()
//	{
//		int vCount = 10;
//		Stream<CartesianVector> vPositions = StreamUtil.toStream(() -> CartesianVector.randomUnitVector().scale(MathUtil.kRng.nextDouble()*vCount), vCount);
//		List<CartesianVector> vList = new ArrayList<CartesianVector>();
//		ConsumerUtil.toCollection(vPositions, vList);
//		TestInteractors vInteractors = new TestInteractors(vCount, vList.stream());
//		long vProjected = vInteractors.projectedStream(vInteractors.chooseRandom()).count();
//		Assert.assertEquals(vCount - 1, vProjected);
//	}
//	
//	@Test
//	public void testInteractions()
//	{
//		int vCount = 10;
//		Stream<CartesianVector> vPositions = StreamUtil.toStream(() -> CartesianVector.randomUnitVector().scale(MathUtil.kRng.nextDouble()*vCount), vCount);
//		List<CartesianVector> vList = new ArrayList<CartesianVector>();
//		ConsumerUtil.toCollection(vPositions, vList);
//		TestInteractors vInteractors = new TestInteractors(vCount, vList.stream());
//		vInteractors.add(vList.stream());
//		int vInteractions = vInteractors.countInteractions();
//		while (vInteractions > 0)
//		{
//			vInteractors.randomMove();
//			int vNewInteractions = vInteractors.countInteractions();
//			Assert.assertTrue(vNewInteractions <= vInteractions);
//			vInteractions = vNewInteractions;
//		}
//	}

	@SuppressWarnings("unused")
	private static class TestInteractors implements Interactors
	{

		private final Lattice mLattice;
		private final List<Interactor> mInteractors = new ArrayList<Interactor>();

		TestInteractors(int pDimension, Stream<CartesianVector> pPositions)
		{
			mLattice = Lattice.cubeLattice(pDimension);
			add(pPositions);
		}
		
		public int countInteractions()
		{
			int vInteractions = 0;
			for (int i=0; i < mInteractors.size(); i++)
			{
				for (int j=i+1; j < mInteractors.size(); j++)
				{
					vInteractions = get(i).interacts(get(j)) ? vInteractions + 1 : vInteractions;
				}
			}
			return vInteractions;
		}
		
		private Interactor get(int pIndex)
		{
			return mInteractors.get(pIndex).reposition(mLattice.projectIntoLattice(mInteractors.get(pIndex).position()));
		}

		public void add(Stream<CartesianVector> pPositions)
		{
			ConsumerUtil.toCollection(pPositions
					.map((pPosition) -> new TestInteractor(mLattice
							.projectIntoLattice(pPosition))), mInteractors);
		}

		@Override
		public Stream<? extends Interactor> stream()
		{
			return mInteractors.stream();
		}

		@Override
		public Interactor chooseRandom()
		{
			return mInteractors.get(MathUtil.getThreadLocal().nextInt(mInteractors.size()));
		}

		@Override
		public void replace(Interactor pOldInteractor, Interactor pNewInteractor)
		{
			int vIndex = mInteractors.indexOf(pOldInteractor);
			mInteractors.remove(vIndex);
			mInteractors.add(vIndex, pNewInteractor);
		}

		@Override
		public Lattice getLattice()
		{
			return mLattice;
		}
		
		@Override
		public String toString()
		{
			return mInteractors.toString();
		}

		@Override
		public MovedInteractor testMoveRandom()
		{
			Interactor vOld = chooseRandom();
			return new MovedInteractor(vOld, vOld.reposition(vOld.position().add(CartesianVector.randomUnitVector())));
		}
	}

	private static class TestInteractor implements Interactor
	{
		private final CartesianVector mPosition;
		private final Segment mLineSegment;

		public TestInteractor(CartesianVector pPosition)
		{
			mPosition = pPosition;
			mLineSegment = SegmentUtil.get(mPosition, mPosition);
		}

		@Override
		public CartesianVector position()
		{
			return mPosition;
		}

		@Override
		public Interactor reposition(CartesianVector pNewPosition)
		{
			return new TestInteractor(pNewPosition);
		}

		@Override
		public double interactionRadius()
		{
			return 0.5;
		}

		@Override
		public Segment getLineSegment()
		{
			return mLineSegment;
		}
		
		@Override
		public String toString()
		{
			return mPosition.toString();
		}
	}

}
