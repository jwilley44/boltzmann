package thiel.one;

import java.util.List;

public class App
{
	public void doSomethingWithAs()
	{
		List<A> as = DataManager.getAs();
		System.out.print(as);
	}

	public void doSomethingWithBs()
	{
		List<B> bs = DataManager.getBs();
		System.out.print(bs);
	}
}
