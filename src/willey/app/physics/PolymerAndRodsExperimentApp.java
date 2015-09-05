package willey.app.physics;

import java.io.File;

import willey.lib.physics.polymer.experiment.PolymerAndRodsExperiment;
import willey.lib.util.Timer;

public class PolymerAndRodsExperimentApp
{
	public static void main(String pArgs[]) throws Exception
	{
		Timer vTimer = Timer.start();
		new PolymerAndRodsExperiment(new File(pArgs[0])).run();
		System.err.println(vTimer.getElapsedTimeSeconds());
	}
}
