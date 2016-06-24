package willey.lib.physics.polymer.measurement;

import static java.util.Arrays.asList;
import static willey.lib.math.linearalgebra.CartesianVector.of;
import static willey.lib.math.linearalgebra.CartesianVector.randomUnitVector;
import static willey.lib.math.linearalgebra.CartesianVector.randomVector;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.experiment.ParameterCombiner;
import willey.lib.physics.polymer.interactor.PolymerAndRods;
import willey.lib.physics.polymer.interactor.PolymerAndRodsImpl;

public class MeasurementsTest
{
	@Test
	public void testCorrelation()
	{
		CartesianVector vVector = of(0, 0, 1);
		List<CartesianVector> vVectors = Arrays.asList(vVector, vVector,
				vVector, vVector, vVector);
		double vCorrelation = MeasurementUtil.correlation(vVectors.stream(),
				vVectors.stream(), vVectors.size());
		Assert.assertEquals(1.0, vCorrelation, 1e-4);
	}

	@Test
	public void testOrderParameter()
	{
		CartesianVector vVector = of(0, 0, 5);
		List<CartesianVector> vVectors = Arrays.asList(vVector, vVector,
				vVector, vVector, vVector);
		double vOrderParameter = MeasurementUtil.orderParameter(vVectors);
		Assert.assertEquals(1.0, vOrderParameter, 1e-4);

		vVectors = Arrays.asList(randomUnitVector(), randomUnitVector(),
				randomUnitVector(), randomUnitVector(), randomUnitVector());
		Assert.assertTrue(MeasurementUtil.orderParameter(vVectors) <= 0.5);
	}
	
	@Test
	public void testParallelMagnitude()
	{
		CartesianVector vVector = of(0.5, 0.5, 0);
		CartesianVector vDirection = of(0, 0, 1);
		List<CartesianVector> vDirections = Arrays.asList(vDirection,
				vDirection, vDirection, vDirection);
		Assert.assertEquals(Math.pow(vVector.dotProduct(vDirection), 2), 
				MeasurementUtil.parallelMagnitude(vVector, vDirections),
				1e-4);
		vVector = randomVector();
		Assert.assertEquals(Math.pow(vVector.dotProduct(vDirection), 2), 
				MeasurementUtil.parallelMagnitude(vVector, vDirections),
				1e-4);
		vDirections = Arrays.asList(of(.1, .1, 1), of(-.1, .2, 1), of(-.1, -.1, 1));
	}

	@Test
	public void testPerpendicularMagnitude()
	{
		CartesianVector vVector = of(0.5, 0.5, 0);
		CartesianVector vDirection = of(0, 0, 1);
		List<CartesianVector> vDirections = Arrays.asList(vDirection,
				vDirection, vDirection, vDirection);
		Assert.assertEquals(vVector.magnitude(),
				MeasurementUtil.perpendicularMagnitude(vVector, vDirections),
				1e-4);
		vVector = randomVector();
		Assert.assertEquals(vVector.projectOntoPlane(vDirection).magnitude(),
				MeasurementUtil.perpendicularMagnitude(vVector, vDirections),
				1e-4);
		vDirections = Arrays.asList(randomUnitVector(), randomUnitVector(), randomUnitVector());
	}
	
	@Test
	public void testMomentOfInertia()
	{
		CartesianVector vDirection = CartesianVector.of(0,0,1);
		CartesianVector vAverCartesianVector = Arrays.asList(vDirection, vDirection.scale(-1), vDirection.scale(2)).stream().collect(CartesianVector.averageVector());
		Assert.assertTrue(vDirection.coordinatesEqual(vAverCartesianVector));
	}
	
	@Test
	public void testArcLength()
	{
		CartesianVector v1 = of(1,0,0);
		CartesianVector v2 = of(1,1,0);
		CartesianVector v3 = of(1,1,1);
		CartesianVector v4 = of(2,1,1);
		
		List<Double> vDistances = MeasurementUtil.arcLength2Distance(asList(v1, v2, v3, v4));
		Assert.assertEquals(3, vDistances.size());
		Assert.assertEquals(1.0, vDistances.get(0), 1e-4);
		Assert.assertEquals(Math.sqrt(2.0), vDistances.get(1), 1e-4);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAllMeasurements() throws Exception
	{
		
		Measurer.Builder<PolymerAndRods> vBuilder = Measurer.builder();
		vBuilder
		.add(Measurements.rodRotation())
		.add(Measurements.averageMonomerDistance())
		.add(Measurements.polymerFractalization())
		.add(Measurements.interactions())
		.add(Measurements.polymerRadius())
		.add(Measurements.polymerSize())
		.add(Measurements.monomerRadius())
		.add(Measurements.orderParameter())
		.add(Measurements.averageRodDistance())
		.add(Measurements.averageRodDirection())
		.add(Measurements.polymerRodCorrelation())
		.add(Measurements.occupiedVolume())
		.add(Measurements.rodCount())
		.add(Measurements.polymerCenter())
		.add(Measurements.polymerRodDistance())
		.add(Measurements.polymerParallelRadius())
		.add(Measurements.polymerPerpendicularRadius())
		//.add(Measurements.polymerPerpendicularFractilization())
		//.add(Measurements.polymerParallelFractilization())
		.add(Measurements.polymerPerpendicularFractilization2())
		.add(Measurements.monomerRodCorrelation())
		.add(Measurements.polymerParallelFractilization2())
		.add(Measurements.hash());
		Measurer<PolymerAndRods> vMeasurer = vBuilder.build();
		new ParameterCombiner(new File("/Users/jwilley44/test.param")).getParameterCombinations()
		.map(pMap -> PolymerAndRodsImpl.fromParameterMap(pMap)).forEach(pSystem -> vMeasurer.getMeasurements().stream().forEach(pMeasurement -> pMeasurement.apply(pSystem)));
	}
	
	
}
