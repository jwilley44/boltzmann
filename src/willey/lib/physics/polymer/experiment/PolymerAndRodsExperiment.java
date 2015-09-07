package willey.lib.physics.polymer.experiment;

import java.io.File;
import java.util.function.Function;

import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.physics.polymer.interactor.PolymerAndRods;
import willey.lib.physics.polymer.interactor.PolymerAndRodsImpl;
import willey.lib.physics.polymer.measurement.Measurements;
import willey.lib.physics.polymer.measurement.Measurer;

public class PolymerAndRodsExperiment extends Experiment<PolymerAndRods>
{
	private static final Measurer.Builder<PolymerAndRods> kBuilder = Measurer.builder();
	{
		kBuilder
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
		.add(Measurements.hash());
	}

	public PolymerAndRodsExperiment(File pParameterFile)
			throws Exception
	{
		super(pParameterFile, new CreatePolymerAndRodsEquilibration(), kBuilder.build());
	}
	
	public PolymerAndRodsExperiment(File pParameterFile, Measurer<PolymerAndRods> pMeasurer) throws Exception
	{
		super(pParameterFile, new CreatePolymerAndRodsEquilibration(), pMeasurer);
	}

	private static class CreatePolymerAndRodsEquilibration implements
			Function<ParameterMap, Equilibrator<PolymerAndRods>>
	{

		@Override
		public Equilibrator<PolymerAndRods> apply(ParameterMap pParameters)
		{
			return new Equilibrator<PolymerAndRods>(
					PolymerAndRodsImpl.getEquilibration(pParameters),
					TerminationCriteria.getPolymerAndRodsPredicate(pParameters));
		}

	}
}
