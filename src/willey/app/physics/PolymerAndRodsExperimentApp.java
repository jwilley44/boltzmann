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
		vBuilder
		.add(Measurements.rodRotation())
		.add(Measurements.averageMonomerDistance())
		.add(Measurements.polymerFractalization())
		.add(Measurements.interactions())
		.add(Measurements.polymerRadius())
		.add(Measurements.polymerSize())
		.add(Measurements.monomerRadius())
		.add(Measurements.orderParameter())
		.add(Measurements.averageRodDistance())
		.add(Measurements.averageRodRadius())
		.add(Measurements.averageRodLength())
		.add(Measurements.averageRodDirection())
		.add(Measurements.polymerRodCorrelation())
		.add(Measurements.occupiedVolume())
		.add(Measurements.rodCount())
		.add(Measurements.polymerCenter())
		.add(Measurements.polymerRodDistance())
		.add(Measurements.hash());
		Experiment<PolymerAndRods> vExperiment = new PolymerAndRodsExperiment(new File(pArgs[0]), vBuilder.build());
		vExperiment.run();
		System.err.println(vTimer.getElapsedTimeSeconds());
	}
}
