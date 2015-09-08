package willey.lib.physics.polymer.interactor;

import java.util.function.Function;
import java.util.stream.Stream;

import willey.lib.util.StreamUtil;

public interface Interactors
{
	Stream<? extends Interactor> stream();
	
	Interactor chooseRandom();
	
	MovedInteractor testMoveRandom();

	void replace(Interactor pOldInteractor, Interactor pNewInteractor);

	Lattice getLattice();
	
	default Stream<? extends Interactor> projectedStream(Interactor pOldInteractor)
	{
		final Lattice vLattice = getLattice();
		Function<Interactor, Interactor> vProjectFunction = (pInteractor) -> pInteractor.reposition(vLattice.projectIntoLattice(pInteractor.position()));
		return stream().filter((pInteractor) -> !pInteractor.equals(pOldInteractor)).map(vProjectFunction);
	}
	
	default Stream<? extends Interactor> projectedStream()
	{
		final Lattice vLattice = getLattice();
		Function<Interactor, Interactor> vProjectFunction = (pInteractor) -> pInteractor.reposition(vLattice.projectIntoLattice(pInteractor.position()));
		return stream().map(vProjectFunction);
	}
	
	default double calculateDeltaEnergy(Interactor pOld, Interactor pNew)
	{
		return StreamUtil.nestedStream(
				projectedStream(pOld),
				getTestPoints(pNew)).mapToDouble((p) -> p.getA().energy(p.getB())).sum() - StreamUtil.nestedStream(
						projectedStream(pOld),
						getTestPoints(pOld)).mapToDouble((p) -> p.getA().energy(p.getB())).sum();
	}
	
	default double calculateEnergy()
	{
		return StreamUtil
				.getIncrementedStream(projectedStream(), projectedStream())
				.mapToDouble(pPair -> pPair.getA().energy(pPair.getB())).sum();
	}
	
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
				getTestPoints(vNew))
				.parallel()
				.noneMatch((pPair) -> pPair.getA().interacts(pPair.getB()));
//		double vDelta = calculateDeltaEnergy(vOld, vNew);
//		boolean vValidMove = vDelta <= 0 ? true : MathUtil.kRng.nextDouble() < Math.exp(-vDelta);
		if (vValidMove)
		{
			replace(vOld, vNew);
		}
		return vValidMove;
	}
}
