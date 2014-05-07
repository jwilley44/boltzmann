package willey.lib.timer;

public interface TimedProcess
{
	public void runProcess() throws Exception;
	
	public void reset();
	
	public String header();
}
