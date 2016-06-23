package willey.lib.math.linearalgebra;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.test.AbstractTest;

public class MomentOfInertiaTensorTest extends AbstractTest
{
	@Test
	public void testTensor()
	{
		CartesianVector vDirection = CartesianVector.of(0,0,1);
		MomentOfInertiaTensor vMomentOfInertiaTensor = new MomentOfInertiaTensor(vDirection, vDirection, vDirection);
		CartesianVector vAverageDirection = vMomentOfInertiaTensor.dominantDirection();
		Assert.assertTrue(vDirection.coordinatesEqual(vAverageDirection) || vDirection.scale(-1.0).coordinatesEqual(vAverageDirection));
	}
	
}
