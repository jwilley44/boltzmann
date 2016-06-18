package willey.lib.physics.polymer.interactor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Serializers
{
	private Serializers()
	{
	}

	public static SerializablePolymer serializePolymer(Polymer pPolymer)
	{
		return new SerializablePolymer(pPolymer.getMonomers()
				.map(pMonomer -> pMonomer.position())
				.collect(Collectors.toList()), pPolymer.monomerRadius(),
				pPolymer.stateId());
	}

	public static SerializableRods serializeRods(Rods pRods)
	{
		double vRodRadius = pRods.getRods().findFirst().get().radius();
		return new SerializableRods(pRods.getRods()
				.map(pRod -> pRod.getLineSegment())
				.collect(Collectors.toList()), vRodRadius, pRods.getLattice()
				.volume(), pRods.stateId());
	}

	public static SerializablePolymerAndRods serializePolymerAndRods(
			PolymerAndRods pPolymerAndRods)
	{
		return new SerializablePolymerAndRods(
				serializePolymer(pPolymerAndRods),
				serializeRods(pPolymerAndRods), pPolymerAndRods.stateId());
	}
	
	public static Polymer deserializePolymer(String pByteString) throws Exception
	{
		return fromBytes(getByteArray(pByteString));
	}
	
	public static Rods deserializeRods(String pByteString) throws Exception
	{
		return fromBytes(getByteArray(pByteString));
	}
	
	public static PolymerAndRods deserializePolymerAndRods(String pByteString) throws Exception
	{
		return fromBytes(getByteArray(pByteString));
	}
	
	public static Polymer deserializePolymer(byte[] pPolymerBytes) throws Exception
	{
		return fromBytes(pPolymerBytes);
	}
	
	public static Rods deserializeRods(byte[] pRodsBytes) throws Exception
	{
		return fromBytes(pRodsBytes);
	}
	
	public static PolymerAndRods deserializePolymerAndRods(byte[] pPolymerAndRodsBytes) throws Exception
	{
		return fromBytes(pPolymerAndRodsBytes);
	}

	private static byte[] getByteArray(String pBytes)
	{
		String[] vStrings = pBytes.replaceAll("\\[|\\]", "").split(", ");
		byte[] vBytes = new byte[vStrings.length];
		for (int i=0; i < vStrings.length; i++)
		{
			vBytes[i] = Byte.parseByte(vStrings[i]);
		}
		return vBytes;
	}
	
	static byte[] getBytes(SerializableInteractors<?, ?> pSerializable)
	{
		byte[] vBytes = new byte[0];
		ByteArrayOutputStream vByteStream = new ByteArrayOutputStream();
		ObjectOutputStream vObjStream = null;
		GZIPOutputStream vZippedBytes = null;
		try
		{
			vZippedBytes = new GZIPOutputStream(vByteStream);
			vObjStream = new ObjectOutputStream(vZippedBytes);
			vObjStream.writeObject(pSerializable);
			vObjStream.close();
			vZippedBytes.close();
			vBytes = vByteStream.toByteArray();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				vByteStream.close();
			} catch (Exception e)
			{
				// ignore exception
			}
		}
		return vBytes;
	}
	
	@SuppressWarnings("unchecked")
	private static <M extends Measurable, S extends SerializableInteractors<M, S>> M fromBytes(byte[] pBytes) throws Exception
	{
		ByteArrayInputStream vByteStream = new ByteArrayInputStream(pBytes);
		ObjectInput vObject = null;
		GZIPInputStream vZipped = null;
		S vReturn;
		try
		{
			vZipped = new GZIPInputStream(vByteStream);
			vObject = new ObjectInputStream(vZipped);
			vReturn = (S)vObject.readObject();
		} 
		finally
		{
			try
			{
				vByteStream.close();
			} catch (Exception e)
			{
				// ignore close exception
			}
			try
			{
				vByteStream.close();
				vZipped.close();
				if (vObject != null)
				{
					vObject.close();
				}
			} catch (Exception e)
			{
				// ignore close exception
			}
		}
		return vReturn.toMeasurable();
	}
}
