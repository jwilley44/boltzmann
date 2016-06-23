package willey.lib.physics.polymer.interactor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import willey.lib.math.MathUtil;
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
		return stream().filter((pInteractor) -> !pInteractor.position().coordinatesEqual(pOldInteractor.position())).map(vProjectFunction);
	}
	
	default Stream<? extends Interactor> projectedStream()
	{
		final Lattice vLattice = getLattice();
		Function<Interactor, Interactor> vProjectFunction = (pInteractor) -> pInteractor.reposition(vLattice.projectIntoLattice(pInteractor.position()));
		return stream().map(vProjectFunction);
	}
	
	default double calculateDeltaEnergy(Interactor pOld, Interactor pNew)
	{
		List<Interactor> vOtherInteractors = projectedStream(pOld).collect(Collectors.toList());
		double vNewEnergy = StreamUtil.nestedStream(
				vOtherInteractors.stream(),
				getTestPoints(pNew)).mapToDouble((p) -> p.getA().energy(p.getB())).sum();
		double vOldEnergy = StreamUtil.nestedStream(
						vOtherInteractors.stream(),
						getTestPoints(pOld)).mapToDouble((p) -> p.getA().energy(p.getB())).sum();
		return vNewEnergy - vOldEnergy;
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
		final Interactor vOld = vMoved.getOldInteractor();
		final Interactor vNew = vMoved.getNewInteractor();
//		boolean vValidMove = StreamUtil.nestedStream(getTestPoints(vNew), projectedStream(vMoved.getOldInteractor()))
//				.noneMatch(pPair -> ! acceptMove(vOld, pPair.getA(), pPair.getB()));
		double vDeltaEnergy = calculateDeltaEnergy(vOld, vNew);
		boolean vValidMove = MathUtil.getThreadLocal().nextDouble() <= Math.exp(-vDeltaEnergy);
		if (vValidMove)
		{
			replace(vMoved.getOldInteractor(), vMoved.getNewInteractor());
		}
		return vValidMove;
	}
}
