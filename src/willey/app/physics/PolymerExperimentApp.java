package willey.app.physics;

import java.io.File;

import willey.lib.physics.polymer.experiment.PolymerExperiment;
import willey.lib.util.Timer;

public class PolymerExperimentApp
{
	public static void main(String pArgs[]) throws Exception
	{
		Timer vTimer = Timer.start();
		new PolymerExperiment(new File(pArgs[0])).run();
		System.err.println(vTimer.getElapsedTimeSeconds());
	}
}
