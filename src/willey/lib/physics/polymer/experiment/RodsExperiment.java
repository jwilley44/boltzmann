package willey.lib.physics.polymer.experiment;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.physics.polymer.interactor.Rods;
import willey.lib.physics.polymer.interactor.RodsImpl;

public class RodsExperiment extends Experiment<Rods>
{

	public RodsExperiment(File pParameterFile) throws IOException
	{
		super(pParameterFile, new CreateRodEquilibration(), Arrays.asList(
				Measurements.kRodUniformity, Measurements.kAverageRodDistance,
				Measurements.kAverageRodRadius, Measurements.kAverageRodLength, Measurements.kMaxRodDistance,
				Measurements.kRodRotation, Measurements.kRodTranslation,
				Measurements.kOccupiedVolume, Measurements.kRodCount, Measurements.kOrderParameter));
	}

	private static class CreateRodEquilibration implements
			Function<ParameterMap, Equilibrator<Rods>>
	{

		@Override
		public Equilibrator<Rods> apply(ParameterMap pParameters)
		{
			return new Equilibrator<>(RodsImpl.getEquilibration(pParameters),
					TerminationCriteria.getRodPredicate(pParameters));
		}

	}

}
