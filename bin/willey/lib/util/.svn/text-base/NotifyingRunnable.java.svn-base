package willey.lib.util;

import java.util.concurrent.Callable;

public class NotifyingRunnable<R> implements Runnable
{
	private final Handler<R> mResultHandler;
	private final Callable<R> mCallable;
	
	public NotifyingRunnable(Handler<R> pHandler, Callable<R> pCallable)
	{
		mResultHandler = pHandler;
		mCallable = pCallable;
	}
	
	@Override
	public void run()
	{
		R vResult = null;
		try
		{
			vResult = mCallable.call();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			mResultHandler.handle(vResult);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
