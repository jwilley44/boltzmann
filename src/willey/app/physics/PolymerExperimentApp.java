package willey.app.physics;

import java.io.File;

import willey.lib.physics.polymer.experiment.PolymerExperiment;
import willey.lib.physics.polymer.interactor.Polymer;
import willey.lib.physics.polymer.measurement.Measurements;
import willey.lib.physics.polymer.measurement.Measurer;

public class PolymerExperimentApp extends PhysicsExperimentApp
{
	public static void main(String pArgs[]) throws Exception
	{
		PhysicsExperimentApp.runMain(new PolymerExperimentApp(), pArgs);
	}

	@Override
	void run(String[] pArgs) throws Exception
	{
		Measurer.Builder<Polymer> vBuilder = Measurer.builder();
			vBuilder
			.add(Measurements.averageMonomerDistance())
			.add(Measurements.polymerFractalization())
			.add(Measurements.polymerRadius())
			.add(Measurements.polymerSize())
			.add(Measurements.interactions())
			.add(Measurements.monomerDirectionCorrelation())
			.add(Measurements.monomerRadius())
			.add(Measurements.hash());
		new PolymerExperiment(new File(pArgs[0]), vBuilder.build()).run();
	}
}
