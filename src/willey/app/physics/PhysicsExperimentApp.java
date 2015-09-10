package willey.app.physics;

import java.io.IOException;

import willey.lib.util.Timer;
import willey.lib.util.Version;

abstract public class PhysicsExperimentApp
{
	public static void runMain(PhysicsExperimentApp pApp, String[] pArgs) throws IOException
	{
		System.err.println(Version.get());
		Timer vTimer = Timer.start();
		try
		{
			pApp.run(pArgs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.err.println("Elapsed Time: " + vTimer.getElapsedTimeSeconds());
	}
	
	protected PhysicsExperimentApp()
	{
		
	}
	
	abstract void run(String[] pArgs) throws Exception;
}
