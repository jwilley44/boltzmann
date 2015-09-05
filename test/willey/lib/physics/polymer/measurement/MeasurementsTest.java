package willey.lib.physics.polymer.measurement;

import static willey.lib.math.linearalgebra.CartesianVector.of;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.math.linearalgebra.CartesianVector;

public class MeasurementsTest
{
	@Test
	public void testCorrelation()
	{
		CartesianVector vVector = of(0,0,1);
		List<CartesianVector> vVectors = Arrays.asList(vVector, vVector, vVector, vVector, vVector);
		double vCorrelation = MeasurementUtil.correlation(vVectors.stream(), vVectors.stream(), vVectors.size());
		Assert.assertEquals(1.0, vCorrelation, 1e-4);
	}
}
