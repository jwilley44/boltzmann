package willey.lib.physics.polymer.measurement;

import java.util.List;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.Measurable;
import willey.lib.physics.polymer.interactor.Polymer;
import willey.lib.physics.polymer.interactor.PolymerAndRods;
import willey.lib.physics.polymer.interactor.Rods;

public class Measurements
{
	private Measurements(){}

	public static <M extends Measurable> Measurement<M, Double> energy()
	{
		return new Energy<M>();
	}
	
	public static <M extends Measurable> Measurement<M, Long> interactions()
	{
		return new Interactions<M>();
	}
	
	public static <M extends Measurable> Hash<M> hash()
	{
		return new Hash<M>();
	}
	
	public static <R extends Rods> Measurement<R, CartesianVector> averageRodDirection()
	{
		return new AverageRodDirection<R>();
	}
	
	public static <P extends Polymer> Measurement<P, Double> polymerCenter()
	{
		return new PolymerCenter<P>();
	}
	
	public static <P extends Polymer> Measurement<P, Double> polymerSize()
	{
		return new PolymerSize<P>();
	}
	
	public static <R extends Rods> Measurement<R, Double> rodRotation()
	{
		return new RodRotation<R>();
	}
	
	public static <R extends Rods> Measurement<R, Double> rodTranslation()
	{
		return new RodTranslation<R>();
	}
	
	public static <R extends Rods> Measurement<R, Double> maxRodDistance()
	{
		return new MaxRodDistance<R>();
	}
	
	public static <P extends Polymer> Measurement<P, Double> monomerRadius()
	{
		return new MonomerRadius<P>();
	}
	
	public static <PR extends PolymerAndRods> Measurement<PR, Double> monomerRodCorrelation()
	{
		return new MonomerRodCorrelation<PR>();
	}
	
	public static <P extends Polymer> Measurement<P, List<Double>> polymerFractalization()
	{
		return new PolymerFractalization<P>();
	}
	
	public static <P extends Polymer> Measurement<P, CartesianVector> polymerDirection()
	{
		return new PolymerDirection<P>();
	}
	
	public static <P extends Polymer> Measurement<P, Double> monomerDirectionCorrelation()
	{
		return new MonomerDirectionCorrelation<P>();
	}
	
	public static <P extends Polymer> Measurement<P, CartesianVector> polymerPosition()
	{
		return new PolymerPosition<P>();
	}
	
	public static <PR extends PolymerAndRods> Measurement<PR, Double> polymerRodDistance()
	{
		return new PolymerRodDistance<PR>();
	}
	
	public static <R extends Rods> Measurement<R, Double> nearestRod()
	{
		return new NearestRod<R>();
	}

	public static <PR extends PolymerAndRods> Measurement<PR, Double> polymerDirectionRadius()
	{
		return new PolymerDirectionRadius<PR>();
	}
	
	public static <R extends Rods> Measurement<R, Double> occupiedVolume()
	{
		return new OccupiedVolume<R>();
	}
	
	public static <P extends Polymer> Measurement<P, Double> maxDistance()
	{
		return new MaxDistance<P>();
	}
	
	public static <PR extends PolymerAndRods> Measurement<PR, Double> maxDirectionCorrelation()
	{
		return new MaxDirectionCorrelation<PR>();
	}
	
	public static <P extends Polymer> Measurement<P, Double> averageMonomerDistance()
	{
		return new AverageMonomerDistance<P>();
	}
	
	public static <R extends Rods> Measurement<R, Double> orderParameter()
	{
		return new OrderParameter<R>();
	}
	
	public static <R extends Rods> Measurement<R, Double> rodCorrelation()
	{
		return new RodCorrelation<R>();
	}
	
	public static <R extends Rods> Measurement<R, Double> averageRodDistance()
	{
		return new AverageRodDistance<R>();
	}
	
	public static <P extends Polymer> Measurement<P, Double> polymerRadius()
	{
		return new PolymerRadius<P>();
	}
	
	public static <PR extends PolymerAndRods> Measurement<PR, Double> polymerParallelRadius()
	{
		return new PolymerParallelRadius<PR>();
	}
	
	public static <PR extends PolymerAndRods> Measurement<PR, Double> polymerPerpendicularRadius()
	{
		return new PolymerPerpendicularRadius<PR>();
	}
	
	public static <PR extends PolymerAndRods> Measurement<PR, List<Double>> polymerPerpendicularFractilization2()
	{
		return new PolymerPerpendicularFractilization2<PR>();
	}
	
	public static <PR extends PolymerAndRods> Measurement<PR, List<Double>> polymerParallelFractilization2()
	{
		return new PolymerParallelFractilization2<PR>();
	}
	
	public static <R extends Rods> Measurement<R, Double> averageRodLength()
	{
		return new AverageRodLength<R>();
	}
	
	public static <R extends Rods> Measurement<R, Double> averageRodRadius()
	{
		return new AverageRodRadius<R>();
	}
	
	public static <PR extends PolymerAndRods> Measurement<PR, Double> polymerRodCorrelation()
	{
		return new RodPolymerCorrelation<PR>();
	}
	
	public static <R extends Rods> Measurement<R, Integer> rodCount()
	{
		return new RodCount<R>();
	}
}
