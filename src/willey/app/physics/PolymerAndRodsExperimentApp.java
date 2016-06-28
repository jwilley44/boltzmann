package willey.app.physics;

import java.io.File;
import java.io.IOException;

import willey.lib.physics.polymer.experiment.Experiment;
import willey.lib.physics.polymer.experiment.PolymerAndRodsExperiment;
import willey.lib.physics.polymer.interactor.PolymerAndRods;
import willey.lib.physics.polymer.measurement.Measurements;
import willey.lib.physics.polymer.measurement.Measurer;

public class PolymerAndRodsExperimentApp extends PhysicsExperimentApp
{
	public static void main(String[] pArgs) throws IOException
	{
		PhysicsExperimentApp.runMain(new PolymerAndRodsExperimentApp(), pArgs);
	}
	
	public void run(String pArgs[]) throws Exception
	{
		Measurer.Builder<PolymerAndRods> vBuilder = Measurer.builder();
		vBuilder
		.add(Measurements.rodRotation())
		.add(Measurements.averageMonomerDistance())
		.add(Measurements.polymerFractalization())
		.add(Measurements.interactions())
		.add(Measurements.polymerRadius())
		.add(Measurements.polymerSize())
		.add(Measurements.monomerRadius())
		.add(Measurements.monomerDirectionCorrelation())
		.add(Measurements.orderParameter())
		.add(Measurements.averageRodDistance())
		.add(Measurements.averageRodDirection())
		.add(Measurements.polymerRodCorrelation())
		.add(Measurements.occupiedVolume())
		.add(Measurements.rodCount())
		.add(Measurements.polymerCenter())
		.add(Measurements.polymerRodDistance())
		.add(Measurements.polymerParallelRadius())
		.add(Measurements.polymerPerpendicularRadius())
		.add(Measurements.polymerPerpendicularFractilization())
		.add(Measurements.monomerRodCorrelation())
		.add(Measurements.polymerParallelFractilization())
		.add(Measurements.rodsStartingState())
		.add(Measurements.quenchedRods())
		.add(Measurements.hash());
		Experiment<PolymerAndRods> vExperiment = new PolymerAndRodsExperiment(new File(pArgs[0]), vBuilder.build());
		vExperiment.run();
	}
}
