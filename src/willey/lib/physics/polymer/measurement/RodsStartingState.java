package willey.lib.physics.polymer.measurement;

import willey.lib.physics.polymer.interactor.Rods;

public class RodsStartingState<R extends Rods> implements Measurement<R, String>
{

	@Override
	public String apply(R pMeasurable)
	{
		return pMeasurable.startingState();
	}

	@Override
	public String getName()
	{
		return "starting.state";
	}

}
