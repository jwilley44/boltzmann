package willey.lib.physics.polymer.interactor;

public class MovedInteractor
{
	private final Interactor mOldInteractor;
	private final Interactor mNewInteractor;
	
	public <I extends Interactor> MovedInteractor(I pOldInteractor, I pNewInteractor)
	{
		mOldInteractor = pOldInteractor;
		mNewInteractor = pNewInteractor;
	}
	
	public Interactor getOldInteractor()
	{
		return mOldInteractor;
	}
	
	public Interactor getNewInteractor()
	{
		return mNewInteractor;
	}
}
