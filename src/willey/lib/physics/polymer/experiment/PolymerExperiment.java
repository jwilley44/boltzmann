package willey.lib.physics.polymer.experiment;

import java.io.File;
import java.util.function.Function;

import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.physics.polymer.interactor.Polymer;
import willey.lib.physics.polymer.interactor.PolymerImpl;
import willey.lib.physics.polymer.measurement.Measurements;
import willey.lib.physics.polymer.measurement.Measurer;

public class PolymerExperiment extends Experiment<Polymer>
{
	private static final Measurer.Builder<Polymer> kBuilder = Measurer.builder();
	{
		kBuilder
		.add(Measurements.averageMonomerDistance())
		.add(Measurements.polymerFractalization())
		.add(Measurements.polymerRadius())
		.add(Measurements.polymerSize())
		.add(Measurements.interactions())
		.add(Measurements.monomerDirectionCorrelation())
		.add(Measurements.monomerRadius())
		.add(Measurements.energy())
		.add(Measurements.hash());
	}
	
	public PolymerExperiment(File pParameterFile) throws Exception
	{
		super(pParameterFile, new CreatePolymerEquilibration(), kBuilder.build());
	}

	private static class CreatePolymerEquilibration implements
			Function<ParameterMap, Equilibrator<Polymer>>
	{

		@Override
		public Equilibrator<Polymer> apply(ParameterMap pParameters)
		{
			return new Equilibrator<Polymer>(PolymerImpl.getEquilibration(
					pParameters),
					TerminationCriteria.getPolymerPredicate(pParameters));
		}

	}
}
