package willey.lib.physics.polymer.experiment;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.physics.polymer.interactor.Polymer;
import willey.lib.physics.polymer.interactor.PolymerImpl;

public class PolymerExperiment extends Experiment<Polymer>
{
	public PolymerExperiment(File pParameterFile) throws IOException
	{
		super(pParameterFile, new CreatePolymerEquilibration(), Arrays.asList(
				Measurements.kAverageMonomerDistance,
				Measurements.kPolymerFractalization,
				Measurements.kPolymerRadius, Measurements.kPolymerSize,
				Measurements.kInteractions,
				Measurements.kMonomerDirectionCorrelation,
				Measurements.kMonomerRadius,
				Measurements.kEnergy,
				Measurements.kHash));
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
