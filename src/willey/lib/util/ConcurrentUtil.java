package willey.lib.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentUtil
{
	private ConcurrentUtil()
	{
	}

	public static <R extends Runnable> void executeAll(int pMaxThreads,
			Iterable<R> pRunnables)
	{
		ExecutorService vExecutor = Executors.newFixedThreadPool(pMaxThreads);
		for (R vRunnable : pRunnables)
		{
			vExecutor.execute(vRunnable);
		}
		vExecutor.shutdown();
	}
}
