package willey.lib.physics.polymer.measurement;

import java.util.Arrays;

import willey.lib.physics.polymer.interactor.PolymerAndRods;
import willey.lib.physics.polymer.interactor.Serializers;

public class PolymerAndRodsSerializer implements Measurement<PolymerAndRods, String>
{

	@Override
	public String apply(PolymerAndRods pMeasurable)
	{
		byte[] vBytes = Serializers.serializePolymerAndRods(pMeasurable).getBytes();
		return Arrays.toString(vBytes);
	}

	@Override
	public String getName()
	{
		return "polymer.rods.serialized";
	}

}
