package willey.lib.physics.polymer.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import willey.lib.math.MathUtil;
import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.math.linearalgebra.CartesianVector.VectorSum;
import willey.lib.physics.polymer.interactor.Interactor;
import willey.lib.physics.polymer.interactor.Measurable;
import willey.lib.physics.polymer.interactor.Monomer;
import willey.lib.physics.polymer.interactor.Polymer;
import willey.lib.physics.polymer.interactor.PolymerAndRods;
import willey.lib.physics.polymer.interactor.Rods;
import willey.lib.util.ConsumerUtil;
import willey.lib.util.Pair;
import willey.lib.util.StreamUtil;

public class Measurements
{
	private Measurements(){}
	
	public static final AverageRodDirection kAverageRodDirection = new AverageRodDirection();
	public static final AverageRodDistance kAverageRodDistance = new AverageRodDistance();
	public static final AverageRodLength kAverageRodLength = new AverageRodLength();
	public static final AverageMonomerDistance kAverageMonomerDistance = new AverageMonomerDistance();
	public static final AverageRodRadius kAverageRodRadius = new AverageRodRadius();
	public static final Hash kHash = new Hash();
	public static final Interactions kInteractions = new Interactions();
	public static final MonomerDirectionCorrelation kMonomerDirectionCorrelation = new MonomerDirectionCorrelation();
	public static final MonomerRadius kMonomerRadius = new MonomerRadius();
	public static final MaxDirectionCorrelation kMaxDirectionCorrelation = new MaxDirectionCorrelation();
	public static final MaxDistance kMaxDistance = new MaxDistance();
	public static final NearestRod kNearestRod = new NearestRod();
	public static final OccupiedVolume kOccupiedVolume = new OccupiedVolume();
	public static final OrderParameter kOrderParameter = new OrderParameter();
	public static final RodCount kRodCount = new RodCount();
	public static final RodPolymerCorrelation kRodPolymerCorrelation = new RodPolymerCorrelation();
	public static final RodCorrelation kRodUniformity = new RodCorrelation();
	public static final RodRotation kRodRotation = new RodRotation();
	public static final RodTranslation kRodTranslation = new RodTranslation();
	public static final PolymerDirectionRadius kPolymerDirectionRadius = new PolymerDirectionRadius();
	public static final PolymerDirection kPolymerDirection = new PolymerDirection();
	public static final PolymerFractalization kPolymerFractalization = new PolymerFractalization();
	public static final PolymerPosition kPolymerPosition = new PolymerPosition();
	public static final PolymerRadius kPolymerRadius = new PolymerRadius();
	public static final PolymerRodDistance kPolymerRodDistance = new PolymerRodDistance();
	public static final PolymerSize kPolymerSize = new PolymerSize();
	public static final MaxRodDistance kMaxRodDistance = new MaxRodDistance();

	public static class RodRotation implements Measurement<Rods, Double>
	{

		@Override
		public Double apply(Rods pMeasurable)
		{
			return pMeasurable.rodRotation();
		}

		@Override
		public String getName()
		{
			return "rod.rotation";
		}
		
	}
	
	public static class RodTranslation implements Measurement<Rods, Double>
	{

		@Override
		public Double apply(Rods pMeasurable)
		{
			return pMeasurable.rodTranslation();
		}

		@Override
		public String getName()
		{
			return "rod.translation";
		}
		
	}
	
	public static class MaxRodDistance implements Measurement<Rods, Double>
	{
		@Override
		public Double apply(Rods pFrom)
		{
			return Double.valueOf(StreamUtil
			.nestedStream(pFrom.getRods(), pFrom.getRods())
			.mapToDouble(
					(pPair) -> pPair.getA().minimumDistance(
							pPair.getB())).max().getAsDouble());
		}
		
		@Override
		public String getName()
		{
			return "max.rod.distance";
		}
	}
	
	public static class Interactions implements
			Measurement<Measurable, Integer>
	{
		@Override
		public Integer apply(Measurable pFrom)
		{
			return Integer
					.valueOf(new CountInteractions(pFrom)
							.interactions());
		}

		@Override
		public String getName()
		{
			return "Interactions";
		}
	}
	
	public static class MonomerRadius implements Measurement<Polymer, Double>
	{

		@Override
		public Double apply(Polymer pMeasurable)
		{
			return Double.valueOf(pMeasurable.getMonomers().mapToDouble((pMonomer) -> pMonomer.interactionRadius()).sum() / pMeasurable.getSize());
		}

		@Override
		public String getName()
		{
			return "MonomerRadius";
		}
		
	}

	public static class PolymerFractalization implements
			Measurement<Polymer, List<Double>>
	{
		@Override
		public List<Double> apply(Polymer pMeasurable)
		{
			List<List<Double>> vDistances = new ArrayList<List<Double>>();
			Stream<CartesianVector> vDirectionStream = pMeasurable.getInteractors()
					.map((pInteractor) -> pInteractor.position());
			List<CartesianVector> vPositions = ConsumerUtil.toCollection(vDirectionStream, new ArrayList<CartesianVector>());
			for (int i = 0; i < pMeasurable.getSize() - 1; i++)
			{
				for (int j = i + 1; j < pMeasurable.getSize(); j++)
				{
					int vStep = j - i;
					List<Double> vStepDistance;
					if (vDistances.size() < vStep)
					{
						vStepDistance = new ArrayList<Double>();
						vDistances.add(vStepDistance);
					} else
					{
						vStepDistance = vDistances.get(vStep - 1);
					}
					vStepDistance.add(vPositions.get(i).distance(
							vPositions.get(j)));
				}
			}

			List<Double> vAverageDistances = new ArrayList<Double>();
			for (List<Double> vStepDistance : vDistances)
			{
				vAverageDistances.add(Double.valueOf(MathUtil
						.sum(vStepDistance) / vStepDistance.size()));
			}
			return vAverageDistances;
		}

		@Override
		public String getName()
		{
			return "PolymerFractilization";
		}
	}

	private static class PolymerDirection implements
			Measurement<Polymer, CartesianVector>
	{
		@Override
		public CartesianVector apply(Polymer pMeasurable)
		{
			return pMeasurable.getDirection().unitVector();
		}

		@Override
		public String getName()
		{
			return "PolymerDirection";
		}
	}

	public static class PolymerPosition implements
			Measurement<Polymer, CartesianVector>
	{
		private PolymerPosition()
		{
		}

		@Override
		public CartesianVector apply(Polymer pMeasurable)
		{
			VectorSum vConsumer = new VectorSum();
			pMeasurable.getMonomers().map((pMonomer) -> pMonomer.position())
					.forEach(vConsumer);
			return vConsumer.getSum().scale(1.0 / pMeasurable.getSize());
		}

		@Override
		public String getName()
		{
			return "PolymerPosition";
		}
	}

	public static class PolymerRodDistance implements
			Measurement<PolymerAndRods, Double>
	{
		private PolymerRodDistance()
		{
		}

		@Override
		public Double apply(PolymerAndRods pMeasurable)
		{
			double vSum = pMeasurable
					.getMonomers()
					.map((pMonomer) -> pMeasurable
							.getRods()
							.map((pRod) -> Double.valueOf(pRod
									.minimumDistance(pMonomer.position())))
							.min(Double::compare))
					.mapToDouble((pOptional) -> pOptional.get().doubleValue())
					.sum();
			return Double.valueOf(vSum / pMeasurable.getSize());
		}

		@Override
		public String getName()
		{
			return "PolymerRodDistance";
		}

	}

	private static class NearestRod implements Measurement<Rods, Double>
	{
		@Override
		public Double apply(Rods pFrom)
		{
			double vSum = pFrom
					.getRods()
					.map((pRod1) -> pFrom
							.getRods().filter((pRod2) -> !pRod2.equals(pRod1))
							.map((pRod2) -> Double.valueOf(pRod1
									.minimumDistance(pRod2)))
							.min(Double::compare))
					.mapToDouble((pOptional) -> pOptional.get().doubleValue())
					.sum();
			return Double.valueOf(vSum / pFrom.rodCount());
		}

		@Override
		public String getName()
		{
			return "NearestRod";
		}
	}

	public static class MonomerDirectionCorrelation implements
			Measurement<Polymer, Double>
	{
		private MonomerDirectionCorrelation()
		{
		}

		@Override
		public Double apply(Polymer pMeasurable)
		{
			int vSize = pMeasurable.getSize();
			double vSum = StreamUtil
					.dualSream(pMeasurable.getDirections().limit(vSize - 1),
							pMeasurable.getDirections().skip(1))
					.mapToDouble(
							(pPair) -> correlation(pPair.getA(), pPair.getB()))
					.sum();
			return Double.valueOf(vSum / (vSize - 2));
		}

		@Override
		public String getName()
		{
			return "MonomerDirectionCorrelation";
		}
	}
	
	public static class AverageRodDirection implements
			Measurement<Rods, CartesianVector>
	{
		@Override
		public CartesianVector apply(Rods pMeasurable)
		{
			VectorSum vSum = new VectorSum();
			pMeasurable.getRods()
					.map((pRod) -> orient(pRod.direction().unitVector()))
					.forEach(vSum);
			return vSum.getSum().scale(1.0 / (double) pMeasurable.rodCount())
					.unitVector();
		}

		private CartesianVector orient(CartesianVector pVector)
		{
			CartesianVector vVector = pVector;
			double vSign = Math.signum(vVector.z());
			if (vSign != 0)
			{
				vVector = vVector.scale(vSign);
			} else
			{
				vSign = Math.signum(vVector.x());
				if (vSign != 0)
				{
					vVector = vVector.scale(vSign);
				} else
				{
					vVector = vVector.scale(Math.signum(vVector.y()));
				}
			}
			return vVector;
		}

		@Override
		public String getName()
		{
			return "average.rod.direction";
		}
	}

	private static class PolymerDirectionRadius implements
			Measurement<PolymerAndRods, Double>
	{
		@Override
		public Double apply(PolymerAndRods pFrom)
		{
			Function<Monomer, CartesianVector> vPostion = (pMonomer) -> pMonomer.position();
			return StreamUtil.getIncrementedStream(
					pFrom.getMonomers().map(vPostion),
					pFrom.getMonomers().map(vPostion))
					.map((pPair) -> {
						CartesianVector vDirection = pPair.getA().subtract(
								pPair.getB());
						Double vCorrelation = Double.valueOf(rodCorrelation(
								pFrom, vDirection));
						return Pair.of(Double.valueOf(vDirection.magnitude()),
								vCorrelation);
					})
					.max((p1, p2) -> p1.getB().compareTo(p2.getB())).get().getA();
		}

		private double rodCorrelation(Rods pRods,
				CartesianVector pMonomerDirection)
		{
			double vCosTheta = pRods.getRods().mapToDouble((pRod) -> {
				double vCos = pRod.direction().cosTheta(pMonomerDirection);
				return vCos * vCos;
			}).sum();
			return Double.valueOf(vCosTheta / pRods.rodCount());
		}

		@Override
		public String getName()
		{
			return "polymer.direction.radius";
		}
	}

	private static class OccupiedVolume implements Measurement<Rods, Double>
	{
		@Override
		public Double apply(Rods pFrom)
		{
			return Double.valueOf(pFrom.rodsVolume());
		}

		@Override
		public String getName()
		{
			return "occupied.volume";
		}
	}

	private static class MaxDistance implements Measurement<Polymer, Double>
	{
		@Override
		public Double apply(Polymer pFrom)
		{
			return StreamUtil
					.nestedStream(pFrom.getMonomers(), pFrom.getMonomers())
					.map((pPair) -> Double.valueOf(pPair.getA().position()
							.distance(pPair.getB().position())))
					.max((d1, d2) -> Double.compare(d1, d2)).get();
		}

		public String getName()
		{
			return "max.distance";
		}
	}

	public static class MaxDirectionCorrelation implements
			Measurement<PolymerAndRods, Double>
	{
		private MaxDirectionCorrelation()
		{
		}

		@Override
		public Double apply(PolymerAndRods pMeasurable)
		{
			CartesianVector vRodDirection = kAverageRodDirection
					.apply(pMeasurable);
			CartesianVector vMaxDirection = StreamUtil
					.nestedStream(pMeasurable.getMonomers(),
							pMeasurable.getMonomers())
					.map((pPair) -> {
						CartesianVector vDirection = pPair.getB().position()
								.subtract(pPair.getA().position());
						Double vDistance = Double.valueOf(vDirection
								.magnitude());
						return Pair.of(vDirection, vDistance);
					})
					.max((p1, p2) -> Double.compare(p1.getB().doubleValue(), p2
							.getB().doubleValue())).get().getA();
			return Double.valueOf(correlation(vRodDirection, vMaxDirection));
		}

		@Override
		public String getName()
		{
			return "max.direction.correlation";
		}
	}

	public static class Hash implements Measurement<Measurable, Integer>
	{
		@Override
		public Integer apply(Measurable pMeasurable)
		{
			return Integer.valueOf(pMeasurable.hashCode());
		}

		@Override
		public String getName()
		{
			return "hash";
		}

	}

	private static class AverageMonomerDistance implements
			Measurement<Polymer, Double>
	{
		@Override
		public Double apply(Polymer pFrom)
		{
			double vSum = StreamUtil.getIncrementedStream(pFrom.getMonomers(), pFrom.getMonomers())
					.mapToDouble(
							(pPair) -> pPair.getA().position()
									.distance(pPair.getB().position())).sum();
			return Double.valueOf(vSum / pFrom.getSize() * pFrom.getSize() / 2
					- pFrom.getSize());
		}

		public String getName()
		{
			return "monomer.distance";
		}
	}

	static double correlation(Stream<CartesianVector> pVectors1, Stream<CartesianVector> pVectors2, 
			int pCount)
	{
		double vDenominator = pCount*(pCount-1)/2;
		return StreamUtil.getIncrementedStream(pVectors1, pVectors2)
				.mapToDouble((pPair) -> correlation(pPair.getA(), pPair.getB()))
				.sum()/ vDenominator;
	}

	private static double correlation(CartesianVector pA, CartesianVector pB)
	{
		double vCos = pA.cosTheta(pB);
		return Math.sqrt(vCos * vCos);
	}
	
	public static class OrderParameter implements Measurement<Rods, Double>
	{
		@Override
		public Double apply(Rods pMeasurable)
		{
			CartesianVector vDirector = kAverageRodDirection.apply(pMeasurable);
			return pMeasurable.getRods()
			.mapToDouble((pRod) -> 1.5*correlation(vDirector, pRod.direction()) - 0.5)
			.sum()/pMeasurable.rodCount();
		}
		
		@Override
		public String getName()
		{
			return "order.parameter";
		}
	}

	public static class RodCorrelation implements Measurement<Rods, Double>
	{
		@Override
		public Double apply(Rods pFrom)
		{
			return correlation(
					pFrom.getRods().map((pRodA) -> pRodA.direction()), 
					pFrom.getRods().map((pRodB) -> pRodB.direction()),
					pFrom.rodCount());
		}

		public String getName()
		{
			return "RodCorrelation";
		}
	}

	private static class AverageRodDistance implements
			Measurement<Rods, Double>
	{

		@Override
		public Double apply(Rods pFrom)
		{
			double vDenominator = pFrom.rodCount()*pFrom.rodCount();
			return StreamUtil
					.nestedStream(pFrom.getRods(), pFrom.getRods())
					.mapToDouble(
							(pPair) -> pPair.getA().minimumDistance(
									pPair.getB())).sum()
					/ vDenominator;
		}

		@Override
		public String getName()
		{
			return "AverageRodDistance";
		}

	}

	private static class PolymerSize implements Measurement<Polymer, Double>
	{
		@Override
		public Double apply(Polymer pFrom)
		{
			return Double.valueOf(pFrom.getSize());
		}

		public String getName()
		{
			return "PolymerSize";
		}
	}

	private static class PolymerRadius implements Measurement<Polymer, Double>
	{
		@Override
		public Double apply(Polymer pFrom)
		{
			return Double.valueOf(pFrom.getEndToEndDistance());
		}

		public String getName()
		{
			return "PolymerRadius";
		}
	}

	public static class AverageRodLength implements Measurement<Rods, Double>
	{
		@Override
		public Double apply(Rods pFrom)
		{
			return pFrom.getRods().mapToDouble((pRod) -> pRod.length()).sum() /(pFrom.rodCount());
		}

		@Override
		public String getName()
		{
			return "RodLength";
		}
	}

	private static class AverageRodRadius implements Measurement<Rods, Double>
	{

		@Override
		public Double apply(Rods pFrom)
		{
			return pFrom.getRods().mapToDouble((pRod) -> pRod.radius()).sum() /(pFrom.rodCount());
		}

		@Override
		public String getName()
		{
			return "RodRadius";
		}

	}

	private static class RodPolymerCorrelation implements
			Measurement<PolymerAndRods, Double>
	{
		@Override
		public Double apply(PolymerAndRods pFrom)
		{
			CartesianVector vAverageRodDirection = kAverageRodDirection
					.apply(pFrom);
			double vCorrelation = pFrom.getDirections().mapToDouble((pDirection) -> correlation(vAverageRodDirection, pDirection)).sum();
			return Double.valueOf(vCorrelation / (pFrom.getSize() - 1));
		}

		@Override
		public String getName()
		{
			return "RodPolymerCorrelation";
		}
	}

	private static class RodCount implements Measurement<Rods, Integer>
	{
		@Override
		public Integer apply(Rods pFrom)
		{
			return Integer.valueOf(pFrom.rodCount());
		}

		@Override
		public String getName()
		{
			return "RodCount";
		}
	}

	private static class CountInteractions implements Consumer<Interactor>
	{
		private int mIndex = 1;
		private int mInteractions = 0;
		private final Measurable mInteractors;

		CountInteractions(Measurable pInteractors)
		{
			mInteractors = pInteractors;
			mInteractors.getInteractors().forEach(this);
		}

		@Override
		public void accept(final Interactor pInteractor)
		{
			mInteractions = mInteractors.getInteractors().skip(mIndex).anyMatch(
					(p) -> pInteractor.interacts(p)) ? mInteractions + 1
					: mInteractions;
			mIndex++;
		}

		public int interactions()
		{
			return mInteractions;
		}
	}
}
