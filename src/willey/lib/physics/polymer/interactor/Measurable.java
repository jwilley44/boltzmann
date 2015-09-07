package willey.lib.physics.polymer.interactor;

import java.util.function.Function;
import java.util.stream.Stream;

public interface Measurable
{
	Stream<? extends Interactor> getInteractors();
	
	Lattice getLattice();
	
	int stateId();
	
	default Stream<? extends Interactor> getProjectedStream()
	{
		final Lattice vLattice = getLattice();
		Function<Interactor, Interactor> vProjectFunction = (pInteractor) -> pInteractor.reposition(vLattice.projectIntoLattice(pInteractor.position()));
		return getInteractors().map(vProjectFunction);
	}
}
