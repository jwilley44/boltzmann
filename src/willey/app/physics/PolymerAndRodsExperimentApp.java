package willey.app.physics;

import java.io.File;

import willey.lib.physics.polymer.experiment.Experiment;
import willey.lib.physics.polymer.experiment.PolymerAndRodsExperiment;
import willey.lib.physics.polymer.interactor.PolymerAndRods;
import willey.lib.physics.polymer.measurement.Measurements;
import willey.lib.physics.polymer.measurement.Measurer;
import willey.lib.util.Timer;

public class PolymerAndRodsExperimentApp
{
	public static void main(String pArgs[]) throws Exception
	{
		Timer vTimer = Timer.start();
		Measurer.Builder<PolymerAndRods> vBuilder = Measurer.builder();
		vBuilder.add(Measurements.equilibrations());
		vBuilder.add(Measurements.hash());
		Experiment<PolymerAndRods> vExperiment = new PolymerAndRodsExperiment(new File(pArgs[0]), vBuilder.build());
		vExperiment.run();
		System.err.println(vTimer.getElapsedTimeSeconds());
	}
}
