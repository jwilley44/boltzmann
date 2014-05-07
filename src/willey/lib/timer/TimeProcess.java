package willey.lib.timer;

import java.util.ArrayList;
import java.util.List;

import willey.lib.util.Handler;
import willey.lib.util.StringJoiner;
import willey.lib.util.Timer;

public class TimeProcess
{
	public static void timeProcess(TimedProcess pProcess, int pTimeTrials,
			Handler<String> pHandler)
	{
		for (int i = 0; i < pTimeTrials; i++)
		{
			recordElapsedSeconds(pProcess, pHandler);
		}
	}

	public static void timeMultipleProcesses(Iterable<TimedProcess> pProcesses,
			int pTimeTrials, Handler<String> pHandler) throws Exception
	{
		for (int i = 0; i < pTimeTrials; i++)
		{
			AgregateHandler vHandler = new AgregateHandler();
			for (TimedProcess vProcess : pProcesses)
			{
				recordElapsedSeconds(vProcess, vHandler);
			}
			pHandler.handle(vHandler.getString());
		}
	}
	
	private static class AgregateHandler implements Handler<String>
	{
		private final StringJoiner mJoiner = new StringJoiner("\t");
		private final List<String> mResults = new ArrayList<String>();
		
		@Override
		public void handle(String pEventResult) throws Exception
		{
			mResults.add(pEventResult);
		}
		
		private String getString()
		{
			return mResults.stream().collect(mJoiner);
		}
	}

	private static void recordElapsedSeconds(TimedProcess pProcess,
			Handler<String> pHandler)
	{
		try
		{
			Timer vTimer = Timer.start();
			pProcess.runProcess();
			vTimer.getElapsedTimeSeconds();
			pHandler.handle(String.valueOf(vTimer.getElapsedTimeSeconds()));
		}
		catch (Exception e)
		{
			System.err.println("Process ended unexpectedly");
			System.err.println(e.getMessage());
		}
		pProcess.reset();
	}
}
