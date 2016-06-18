package willey.lib.physics.polymer.interactor;

import java.io.Serializable;

interface SerializableInteractors<M extends Measurable, S extends SerializableInteractors<M, S>>
		extends Serializable
{
	public int getStateId();
	
	public M toMeasurable();

	public default byte[] getBytes()
	{
		return Serializers.getBytes(this);
	}
}
