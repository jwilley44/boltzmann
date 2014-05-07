package willey.lib.util;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlerAndExecutor implements Handler<Boolean>
{
	private final ExecutorService mService;
	private final Iterable<Callable<Boolean>> mCallables;
	
	private volatile Boolean mResult = Boolean.TRUE;
	
	private HandlerAndExecutor(Iterable<Callable<Boolean>> pRunnables)
	{
		this(pRunnables, Executors.newCachedThreadPool());
	}
	
	private HandlerAndExecutor(Iterable<Callable<Boolean>> pRunnables, int pMaxThreads)
	{
		this(pRunnables, Executors.newFixedThreadPool(pMaxThreads));
	}
	
	private HandlerAndExecutor(Iterable<Callable<Boolean>> pRunnables, ExecutorService pService)
	{
		mService = pService;
		mCallables = pRunnables;
	}
	
	public static boolean executeAll(Iterable<Callable<Boolean>> pCallables, int pMaxThreads) throws Exception
	{
		HandlerAndExecutor vExecutor = new HandlerAndExecutor(pCallables, pMaxThreads);
		vExecutor.executeAll();
		return vExecutor.mResult.booleanValue();
	}
	
	public static boolean executeAll(Iterable<Callable<Boolean>> pCallables) throws Exception
	{
		HandlerAndExecutor vExecutor = new HandlerAndExecutor(pCallables);
		vExecutor.executeAll();
		return vExecutor.mResult.booleanValue();
	}
	
	private void executeAll() throws InterruptedException
	{
		Iterator<Callable<Boolean>> vCallables = mCallables.iterator();
		while (vCallables.hasNext() && !mService.isShutdown())
		{
			submitCallable(vCallables.next());
		}
		mService.shutdown();
		while (!mService.isTerminated())
		{
		}
	}
	
	synchronized public void handle(Boolean pResult) throws Exception
	{
		if (pResult == null || !pResult.booleanValue())
		{
			mService.shutdownNow();
			mResult = Boolean.FALSE;
		}
	}
	
	synchronized private void submitCallable(Callable<Boolean> pCallable)
	{
		if (!mService.isShutdown())
		{
			mService.execute(new NotifyingRunnable<Boolean>(this, pCallable));
		}
	}
}
