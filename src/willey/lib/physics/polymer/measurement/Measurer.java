package willey.lib.physics.polymer.measurement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import willey.lib.physics.polymer.experiment.Equilibrator.EquilibrationResult;
import willey.lib.physics.polymer.interactor.Measurable;

public class Measurer<M extends Measurable> implements Consumer<EquilibrationResult<M>>
{
	public static class Builder<M extends Measurable> 
	{
		private Builder(){}
		
		@SuppressWarnings("rawtypes")
		private final List<Measurement> mMeasurements = new ArrayList<>();
		
		public Builder<M> add(Measurement<M, ?> pMeasurement)
		{
			mMeasurements.add(pMeasurement);
			return this;
		}
		
		public Measurer<M> build(Consumer<String> pResultsWriter) throws Exception
		{
			return new Measurer<M>(mMeasurements, pResultsWriter);
		}
		
		public Measurer<M> build() throws Exception
		{
			return build(pString -> System.out.println(pString));
		}
	}
	
	public static <M extends Measurable> Builder<M> builder()
	{
		return new Builder<M>();
	}
	
	@SuppressWarnings("rawtypes")
	private final List<Measurement> mMeasurements;
	private final Consumer<String> mConsumer;

	@SuppressWarnings("unchecked")
	@Override
	public void accept(EquilibrationResult<M> pResult)
	{
		String vSuffix = "\t" + pResult.equilibration() + "\t" + pResult.equilibrationTime() + "\t" + pResult.getValidMoves();
		mConsumer.accept(mMeasurements
				.stream()
				.map(pMeasurement -> pMeasurement.apply(pResult.getInteractors()).toString())
				.collect(Collectors.joining("\t", "", vSuffix)));
	}
	
	@SuppressWarnings("rawtypes")
	List<Measurement> getMeasurements()
	{
		return mMeasurements;
	}
	
	@SuppressWarnings("rawtypes") 
	private Measurer(List<Measurement> pMeasurements, Consumer<String> pResultWriter) throws Exception
	{
		mMeasurements = pMeasurements;
		mConsumer = pResultWriter;
		writeHeader();
	}
	
	private void writeHeader()
	{
		mConsumer.accept(mMeasurements.stream()
				.map(pMeasurement -> pMeasurement.getName())
				.collect(Collectors.joining("\t", "", "\tequilibration\tequilibration.time\tvalid.moves")));
	}
}
