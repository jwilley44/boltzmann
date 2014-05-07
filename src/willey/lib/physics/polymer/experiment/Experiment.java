package willey.lib.physics.polymer.experiment;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import willey.lib.physics.polymer.experiment.Equilibrator.EquilibrationResult;
import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.physics.polymer.interactor.Measurable;
import willey.lib.util.StreamUtil;
import willey.lib.util.StringJoiner;

public abstract class Experiment<M extends Measurable>
{
	private final Function<ParameterMap, Equilibrator<M>> mParameter2Equilibration;
	private final ParameterCombiner mParameterCombiner;
	private final List<Measurement<? super M, ?>> mMeasurements;
	private final Consumer<String> mWriter = (pLine) -> System.out
			.println(pLine);

	protected Experiment(
			File pParameterFile,
			Function<ParameterMap, Equilibrator<M>> pParameter2Equilibration,
			List<Measurement<? super M, ?>> pMeasurements)
			throws IOException
	{
		mParameterCombiner = new ParameterCombiner(pParameterFile);
		mParameter2Equilibration = pParameter2Equilibration;
		mMeasurements = pMeasurements;
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
		writeHeader();
		StreamUtil.manyMultiApply(
				mParameterCombiner.getParameterCombinations().map(mParameter2Equilibration).parallel(),
				mParameterCombiner.getNumberOfEquilibrations(), 
				(pEquilibrator) -> pEquilibrator.equilibrate())
			.map(new GetResults<M>(mMeasurements)).forEach(mWriter);
	}

	private void terminating()
	{
		writeHeader();
		StreamUtil.multiplyStream(mParameterCombiner.getParameterCombinations(), mParameterCombiner.getNumberOfEquilibrations())
				.map(mParameter2Equilibration).parallel()
				.map((pEquilibration) -> pEquilibration.equilibrate())
				.map(new GetResults<M>(mMeasurements)).forEach(mWriter);
	}

	private void writeHeader()
	{
		mWriter.accept(mMeasurements.stream()
				.map((pMeasurement) -> pMeasurement.getName())
				.collect(new StringJoiner("\t", "EquilibrationTime", "ValidMoves")));
	}
	
	private static class GetResults<M extends Measurable> implements
			Function<EquilibrationResult<M>, String>
	{
		private final List<Measurement<? super M, ?>> mMeasures;

		public GetResults(List<Measurement<? super M, ?>> pMeasures)
		{
			mMeasures = pMeasures;
		}

		@Override
		public String apply(EquilibrationResult<M> pResult)
		{
			int vTime = pResult.equilibrationTime();
			int vValidMoves = pResult.getValidMoves();
			return mMeasures
					.stream()
					.map((pMeasurement) -> pResult
							.takeMeasurement(pMeasurement))
					.collect(new StringJoiner("\t", String.valueOf(vTime), String.valueOf(vValidMoves)));
		}
	}
}
