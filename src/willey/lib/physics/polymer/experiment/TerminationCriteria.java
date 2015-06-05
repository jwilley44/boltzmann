package willey.lib.physics.polymer.experiment;

import java.util.function.Predicate;

import willey.lib.physics.polymer.experiment.ParameterCombiner.ParameterMap;
import willey.lib.physics.polymer.interactor.Measurable;
import willey.lib.physics.polymer.interactor.Polymer;
import willey.lib.physics.polymer.interactor.PolymerAndRods;
import willey.lib.physics.polymer.interactor.Rods;

public class TerminationCriteria
{
	private enum Parameter
	{
		TerminationCriteria, TerminationValue;
	}

	private interface Criteria<M extends Measurable>
	{
		default <P extends M> Predicate<Equilibrator<P>> getPredicate(
				double pReferenceVariable)
		{
			return (pEquilibrator) -> pEquilibrator.getInteractions() < pReferenceVariable;
		}
	}

	private enum PolymerTermination implements Criteria<Polymer>
	{
		MaxInteractions, PolymerSize
		{
			@Override
			public Predicate<Equilibrator<Polymer>> getPredicate(
					double pReferenceVariable)
			{
				return polymerSizeTermination(pReferenceVariable);
			}
		},
		InteractionsAndPowerTermination
		{
			@Override
			public Predicate<Equilibrator<Polymer>> getPredicate(
					double pReferenceVariable)
			{
				return interactionsAndPowerTermination(pReferenceVariable);
			} 
		};
	}

	private enum RodsTermination implements Criteria<Rods>
	{
		MaxInteractions, RodCount
		{
			@Override
			public Predicate<Equilibrator<Rods>> getPredicate(
					double pReferenceVariable)
			{
				return rodCountTermination(pReferenceVariable);
			};
		}
	}

	private enum PolymerAndRodsTermination implements Criteria<PolymerAndRods>
	{
		MaxInteractions, PolymerSize
		{
			@Override
			public Predicate<Equilibrator<PolymerAndRods>> getPredicate(
					double pReferenceVariable)
			{
				return polymerSizeTermination(pReferenceVariable);
			}
		},
		RodCount
		{
			@Override
			public Predicate<Equilibrator<PolymerAndRods>> getPredicate(
					double pReferenceVariable)
			{
				return rodCountTermination(pReferenceVariable);
			}
		}
	}

	public static Predicate<Equilibrator<Polymer>> getPolymerPredicate(
			ParameterMap pParameters)
	{
		return PolymerTermination
				.valueOf(
						pParameters.getString(Parameter.TerminationCriteria
								.name()))
				.getPredicate(
						pParameters.getDouble(Parameter.TerminationValue.name()));
	}

	public static Predicate<Equilibrator<Rods>> getRodPredicate(ParameterMap pParameters)
	{
		return RodsTermination.valueOf(
				pParameters.getString(Parameter.TerminationCriteria
						.name()))
		.getPredicate(
				pParameters.getDouble(Parameter.TerminationValue.name()));
	}

	public static Predicate<Equilibrator<PolymerAndRods>> getPolymerAndRodsPredicate(ParameterMap pParameters)
	{
		return PolymerAndRodsTermination.valueOf(
				pParameters.getString(Parameter.TerminationCriteria
						.name()))
		.getPredicate(
				pParameters.getDouble(Parameter.TerminationValue.name()));
	}

	private static <P extends Polymer> Predicate<Equilibrator<P>> polymerSizeTermination(
			double pPower)
	{
		return (pEquilibrator) -> pEquilibrator.totalMoves() >= Math.pow(
				pEquilibrator.getState()
						.takeMeasurement(Measurements.kPolymerSize)
						.doubleValue(), pPower);
	}
	
	private static <P extends Polymer> Predicate<Equilibrator<P>> polymerInteractionsTermination(double pPower)
	{
		return (pEquilibrator) -> pEquilibrator.totalMoves() >= Math.pow(
				pEquilibrator.getState()
						.takeMeasurement(Measurements.kPolymerSize)
						.doubleValue(), pPower);
	}
	
	private static <P extends Polymer> Predicate<Equilibrator<P>> interactionsAndPowerTermination(double pPower)
	{
		return new InteractionsAndPowerTermination(pPower);
	}

	private static <R extends Rods> Predicate<Equilibrator<R>> rodCountTermination(
			double pPower)
	{
		return (pEquilibrator) -> pEquilibrator.totalMoves() >= Math.pow(
				pEquilibrator.getState()
						.takeMeasurement(Measurements.kRodCount).doubleValue(),
				pPower);
	}
	
	private static class InteractionsAndPowerTermination<P extends Polymer> implements Predicate<Equilibrator<P>>
	{
		private int vCount = 0;
		private final double mPower;
		
		public InteractionsAndPowerTermination(double pPower)
		{
			mPower = pPower;
		}

		@Override
		public boolean test(Equilibrator<P> pEquilibrator)
		{
			if(pEquilibrator.getInteractions() == 0)
			{
				vCount++;
			}
			return vCount >= Math.pow(
					pEquilibrator.getState()
					.takeMeasurement(Measurements.kPolymerSize)
					.doubleValue(), mPower);
		}
		
	}
}
