package willey.lib.physics.polymer.interactor;

import java.util.function.Function;
import java.util.stream.Stream;

import willey.lib.physics.polymer.lattice.Lattice;
import willey.lib.util.StreamUtil;

public interface Interactors
{
	default Stream<? extends Interactor> excludeAndGet(Interactor pOldInteractor)
	{
		return stream().filter((pInteractor) -> !pOldInteractor.equals(pOldInteractor));
	}
	
	Stream<? extends Interactor> stream();
	
	default Stream<? extends Interactor> projectedStream(Interactor pOldInteractor)
	{
		final Lattice vLattice = getLattice();
		Function<Interactor, Interactor> vProjectFunction = (pInteractor) -> pInteractor.reposition(vLattice.projectIntoLattice(pInteractor.position()));
		return stream().filter((pInteractor) -> !pInteractor.equals(pOldInteractor)).map(vProjectFunction);
	}

	Interactor chooseRandom();

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
		Interactor vOld = chooseRandom();
		Interactor vNew = vOld.randomMove();
		boolean vValidMove = StreamUtil.nestedStream(
				projectedStream(vOld),
				getTestPoints(vNew))
				.noneMatch((pPair) -> pPair.getA().interacts(pPair.getB()));
		if (vValidMove)
		{
			replace(vOld, vNew);
		}
		return vValidMove;
	}
}
