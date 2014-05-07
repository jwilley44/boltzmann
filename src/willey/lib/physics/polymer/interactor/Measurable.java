package willey.lib.physics.polymer.interactor;

import java.util.stream.Stream;

public interface Measurable
{
	Stream<? extends Interactor> getInteractors();
}
