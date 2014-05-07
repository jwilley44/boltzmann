package willey.lib.util;

import java.util.Calendar;

public class Timer
{
	private final long mStartTime;
	
	public Timer()
	{
		mStartTime = Calendar.getInstance().getTimeInMillis();
	}
	
	public long getStartTime()
	{
		return mStartTime;
	}
	
	public long getElapsedTimeMilliseconds()
	{
		return Calendar.getInstance().getTimeInMillis() - mStartTime;
	}
	
	public double getElapsedTimeSeconds()
	{
		return ((double)getElapsedTimeMilliseconds())/1000.0;
	}
	
	public static Timer start()
	{
		return new Timer();
	}
}
