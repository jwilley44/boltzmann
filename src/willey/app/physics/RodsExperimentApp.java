package willey.app.physics;

import java.io.File;
import java.io.IOException;

import willey.lib.physics.polymer.experiment.RodsExperiment;

public class RodsExperimentApp extends PhysicsExperimentApp
{
	public static void main(String[] pArgs) throws IOException
	{
		PhysicsExperimentApp.runMain(new RodsExperimentApp(), pArgs);
	}
	
	public void run(String pArgs[]) throws Exception
	{
		new RodsExperiment(new File(pArgs[0])).run();
	}
}
