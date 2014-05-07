package willey.lib.util;

public class StandardOutHandler implements Handler<String>
{
	@Override
	public void handle(String pEventResult) throws Exception
	{
		System.out.println(pEventResult);
	}
}
