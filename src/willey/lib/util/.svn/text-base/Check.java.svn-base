package willey.lib.util;

public class Check
{
	public static final CheckException<IllegalStateException> kIllegalState = new IllegalStateCheck();
	public static final CheckException<IllegalArgumentException> kIllegalArgument = new IllegalArgumentCheck();
	
	private Check(){}
	
	public static abstract class CheckException<E extends Exception> 
	{
		public void checkTrue(boolean pBoolean, String pMessage)throws E
		{
			if (!pBoolean)
			{
				throwException(pMessage);
			}
		}
		
		public void checkNotNull(Object pObject, String pMessage)throws E
		{
			if (pObject == null)
			{
				throwException(pMessage);
			}
		}
		
		abstract void throwException(String pMessage)throws E;
	}
	
	private static class IllegalArgumentCheck extends CheckException<IllegalArgumentException>
	{
		@Override
		void throwException(String pMessage) throws IllegalArgumentException
		{
			throw new IllegalArgumentException(pMessage);
		}
	}
	
	private static class IllegalStateCheck extends CheckException<IllegalStateException>
	{
		@Override
		void throwException(String pMessage) throws IllegalStateException
		{
			throw new IllegalStateException(pMessage);
		}
	}
}
