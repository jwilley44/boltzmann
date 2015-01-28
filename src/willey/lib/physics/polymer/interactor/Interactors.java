package willey.lib.physics.polymer.interactor;

import java.util.function.Function;
import java.util.stream.Stream;

import willey.lib.util.StreamUtil;

public interface Interactors
{
	Stream<? extends Interactor> stream();
	
	default Stream<? extends Interactor> projectedStream(Interactor pOldInteractor)
	{
		final Lattice vLattice = getLattice();
		Function<Interactor, Interactor> vProjectFunction = (pInteractor) -> pInteractor.reposition(vLattice.projectIntoLattice(pInteractor.position()));
		return stream().filter((pInteractor) -> !pInteractor.equals(pOldInteractor)).map(vProjectFunction);
	}

	Interactor chooseRandom();
	
	MovedInteractor testMoveRandom();

	void replace(Interactor pOldInteractor, Interactor pNewInteractor);

	Lattice getLattice();

	/**
	 * This should be overriden if the interactors do not need a lattice
	 * 
	 * @param pNew
	 *            the new interactor
	 * @return 
	 */
	default Stream<Interactor> getTestPoints(Interactor pNew) {
		Interactor vProjected = pNew.reposition(getLattice()
				.projectIntoLattice(pNew.position()));
		return getLattice()
				.getNewLatticeCoordinatesFunctions()
				.stream()
				.map((pFunction) -> vProjected.reposition(pFunction
						.apply(vProjected.position())));
	}

	default boolean randomMove() {
		MovedInteractor vMoved = testMoveRandom();
		Interactor vOld = vMoved.getOldInteractor();
		Interactor vNew = vMoved.getNewInteractor();
		boolean vValidMove = StreamUtil.nestedStream(
				projectedStream(vOld),
				getTestPoints(vNew)).parallel()
				.noneMatch((pPair) -> pPair.getA().interacts(pPair.getB()));
		if (vValidMove)
		{
			replace(vOld, vNew);
		}
		return vValidMove;
	}
}
