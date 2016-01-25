package willey.lib.physics.polymer.experiment;

import java.io.File;
import java.util.function.Function;

import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.physics.polymer.interactor.Measurable;
import willey.lib.physics.polymer.measurement.Measurer;
import willey.lib.util.StreamUtil;

public abstract class Experiment<M extends Measurable>
{
	private final Function<ParameterMap, Equilibrator<M>> mParameter2Equilibration;
	private final ParameterCombiner mParameterCombiner;
	private final Measurer<M> mMeasurer;

	protected Experiment(File pParameterFile,
			Function<ParameterMap, Equilibrator<M>> pParameter2Equilibration,
			Measurer<M> pMeasurer) throws Exception
	{
		mParameterCombiner = new ParameterCombiner(pParameterFile);
		mParameter2Equilibration = pParameter2Equilibration;
		mMeasurer = pMeasurer;
	}

	public void run()
	{
		if (mParameterCombiner.continuousRun())
		{
			continuous();
			
		}
		else
		{
			terminating();
		}
	}

	private void continuous()
	{
		StreamUtil.manyMultiApply(
				mParameterCombiner.getParameterCombinations().map(mParameter2Equilibration).parallel(),
				mParameterCombiner.getNumberOfEquilibrations(), 
				(pEquilibrator) -> pEquilibrator.equilibrate())
				.forEach(mMeasurer);
	}

	private void terminating()
	{
		StreamUtil.multiplyStream(mParameterCombiner.getParameterCombinations(), mParameterCombiner.getNumberOfEquilibrations())
				.map(mParameter2Equilibration).parallel()
				.map(pEquilibration -> pEquilibration.convertToTerminating())
				.map((pEquilibration) -> pEquilibration.equilibrate())
				.forEach(mMeasurer);
	}
}
