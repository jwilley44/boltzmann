package willey.lib.physics.polymer.experiment;

import java.util.function.Function;

import willey.lib.physics.polymer.interactor.Measurable;

public interface Measurement<M extends Measurable, T> extends Function<M, T>
{
	public T apply(M pMeasurable);

	String getName();
}
