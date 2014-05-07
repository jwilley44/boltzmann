package willey.app.physics;

import java.io.File;
import java.io.IOException;

import willey.lib.physics.polymer.experiment.RodsExperiment;

public class RodsExperimentApp
{
	public static void main(String pArgs[]) throws IOException
	{
		new RodsExperiment(new File(pArgs[0])).run();
	}
}
