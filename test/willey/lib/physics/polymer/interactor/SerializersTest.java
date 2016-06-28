package willey.lib.physics.polymer.interactor;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.math.linearalgebra.CartesianVector;
import willey.lib.physics.polymer.interactor.RodsUtil.Orientation;
import willey.lib.physics.polymer.interactor.RodsUtil.Position;
import willey.lib.test.AbstractTest;

public class SerializersTest extends AbstractTest
{
	@Test
	public void testSerializePolymerAndRods() throws Exception
	{
		PolymerImpl vPolymerImpl = new PolymerImpl(10, 0.5,
				Lattice.cubeLattice(10));
		RodsImpl vRods = new RodsImpl(10, 10, 0.1, 0.1, 0.1,
				Lattice.cubeLattice(10), Orientation.Random, Position.Random);
		
		PolymerAndRodsImpl vPolymerAndRodsImpl = new PolymerAndRodsImpl(vPolymerImpl, vRods, 0.8);
		
		SerializablePolymerAndRods vSerializable = Serializers.serializePolymerAndRods(vPolymerAndRodsImpl);
		byte[] vPolymerAndRodBytes = vSerializable.getBytes();
		
		PolymerAndRods vDeserialized = Serializers.deserializePolymerAndRods(vPolymerAndRodBytes);
	
		testPolymer(vPolymerAndRodsImpl, vDeserialized);
		testRods(vPolymerAndRodsImpl, vDeserialized);
		
	}
	
	@Test
	public void testSerializePolymer() throws Exception
	{
		PolymerImpl vPolymerImpl = new PolymerImpl(10, 0.5,
				Lattice.cubeLattice(10));
		SerializablePolymer vSerializablePolymer = Serializers.serializePolymer(vPolymerImpl);
		byte[] vPolymerBytes = vSerializablePolymer.getBytes();
		Polymer vDeserialized = Serializers.deserializePolymer(vPolymerBytes);
		
		testPolymer(vPolymerImpl, vDeserialized);
	}
	
	@Test
	public void testSerializeRods() throws Exception
	{
		RodsImpl vRods = new RodsImpl(10, 10, 0.1, 0.1, 0.1,
				Lattice.cubeLattice(10), Orientation.Random, Position.Random);
		SerializableRods vSerializableRods = Serializers.serializeRods(vRods);
		byte[] vRodsBytes = vSerializableRods.getBytes();
		Rods vDeserialized = Serializers.deserializeRods(vRodsBytes);
		
		testRods(vRods, vDeserialized);
	}
	
	private void testPolymer(Polymer pExpected, Polymer pActual)
	{
		Assert.assertEquals(pExpected.getSize(), pActual.getSize());
		Assert.assertEquals(pExpected.stateId(), pActual.stateId());
		List<CartesianVector> vExpected = pExpected.getMonomers().map(pMonomer -> pMonomer.position()).collect(Collectors.toList());
		List<CartesianVector> vActual = pActual.getMonomers().map(pMonomer -> pMonomer.position()).collect(Collectors.toList());
		for (int i=0; i < vExpected.size(); i++)
		{
			Assert.assertTrue(vExpected.get(i).coordinatesEqual(vActual.get(i)));
		}
	}
	
	private void testRods(Rods pExpected, Rods pActual)
	{
		Assert.assertEquals(pExpected.rodCount(), pActual.rodCount());
		assertEquals(pExpected.rodsVolume(), pActual.rodsVolume(), 1e-4);
		List<Rod> vExpected = pExpected.getRods().collect(Collectors.toList());
//		List<Rod> vActual = pActual.getRods().collect(Collectors.toList());
		
		for (int i=0; i < vExpected.size(); i++)
		{
			//TODO fix me
//			Assert.assertTrue(vExpected.get(i).getLineSegment().segmentsEqual(vActual.get(i).getLineSegment()));
		}
	}
}
