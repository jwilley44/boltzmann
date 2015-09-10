package willey.app.physics;

import java.io.File;

import willey.lib.physics.polymer.experiment.PolymerExperiment;

public class PolymerExperimentApp extends PhysicsExperimentApp
{
	public static void main(String pArgs[]) throws Exception
	{
		PhysicsExperimentApp.runMain(new PolymerExperimentApp(), pArgs);
	}

	@Override
	void run(String[] pArgs) throws Exception
	{
		new PolymerExperiment(new File(pArgs[0])).run();
	}
}
