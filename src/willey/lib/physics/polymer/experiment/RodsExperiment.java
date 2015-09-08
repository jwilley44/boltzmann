package willey.lib.physics.polymer.experiment;

import java.io.File;
import java.util.function.Function;

import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.physics.polymer.interactor.Rods;
import willey.lib.physics.polymer.interactor.RodsImpl;
import willey.lib.physics.polymer.measurement.Measurements;
import willey.lib.physics.polymer.measurement.Measurer;

public class RodsExperiment extends Experiment<Rods>
{
	private static final Measurer.Builder<Rods> kBuilder = Measurer.builder();
	{
		kBuilder
		.add(Measurements.orderParameter())
		.add(Measurements.averageRodDistance())
		.add(Measurements.averageRodRadius())
		.add(Measurements.averageRodLength())
		.add(Measurements.maxRodDistance())
		.add(Measurements.rodRotation())
		.add(Measurements.rodTranslation())
		.add(Measurements.occupiedVolume())
		.add(Measurements.rodCount());
	}
	
	public RodsExperiment(File pParameterFile) throws Exception
	{
		super(pParameterFile, new CreateRodEquilibration(), kBuilder.build());
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
