package willey.app.physics;

import java.io.File;
import java.io.IOException;

import willey.lib.physics.polymer.experiment.RodsExperiment;
import willey.lib.physics.polymer.interactor.Rods;
import willey.lib.physics.polymer.measurement.Measurements;
import willey.lib.physics.polymer.measurement.Measurer;

public class RodsExperimentApp extends PhysicsExperimentApp
{
	public static void main(String[] pArgs) throws IOException
	{
		PhysicsExperimentApp.runMain(new RodsExperimentApp(), pArgs);
	}
	
	public void run(String pArgs[]) throws Exception
	{
		Measurer.Builder<Rods> vBuilder = Measurer.builder();
		vBuilder
		.add(Measurements.orderParameter())
		.add(Measurements.averageRodDistance())
		.add(Measurements.averageRodRadius())
		.add(Measurements.averageRodLength())
		.add(Measurements.maxRodDistance())
		.add(Measurements.rodRotation())
		.add(Measurements.rodTranslation())
		.add(Measurements.occupiedVolume())
		.add(Measurements.rodCount());
		new RodsExperiment(new File(pArgs[0]), vBuilder.build()).run();
	}
}
