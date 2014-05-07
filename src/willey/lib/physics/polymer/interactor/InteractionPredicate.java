package willey.lib.physics.polymer.interactor;

import java.util.function.Predicate;

public class InteractionPredicate<I extends Interactor> implements Predicate<I>
{
	private final I mInteractor;

	public InteractionPredicate(I pInteractor)
	{
		mInteractor = pInteractor;
	}

	/**
	 * @return true if the argument interacts with the given interactor
	 */
	@Override
	public boolean test(I pArgument)
	{
		return pArgument.interacts(mInteractor);
	}
}
