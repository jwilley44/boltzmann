package willey.lib.physics.polymer.experiment;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.physics.polymer.interactor.PolymerAndRods;
import willey.lib.physics.polymer.interactor.PolymerAndRodsImpl;

public class PolymerAndRodsExperiment extends Experiment<PolymerAndRods>
{

	public PolymerAndRodsExperiment(File pParameterFile)
			throws IOException
	{
		super(pParameterFile, new CreatePolymerAndRodsEquilibration(), Arrays
				.asList(Measurements.kAverageMonomerDistance,
						Measurements.kPolymerFractalization,
						Measurements.kInteractions,
						Measurements.kPolymerRadius, Measurements.kPolymerSize,
						Measurements.kMonomerRadius,
						Measurements.kRodUniformity,
						Measurements.kAverageRodDistance,
						Measurements.kAverageRodRadius,
						Measurements.kAverageRodLength,
						Measurements.kAverageRodDirection,
						Measurements.kRodPolymerCorrelation,
						Measurements.kOccupiedVolume, Measurements.kRodCount, Measurements.kHash));
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
