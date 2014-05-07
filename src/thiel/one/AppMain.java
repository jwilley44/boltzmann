package thiel.one;

public class AppMain
{
	public static void main(String pArgs[])
	{
		for (String vDataSourceName : pArgs)
		{
			DataSource vDataSource = DataSource.valueOf(vDataSourceName);
			if (vDataSource != null)
			{
				DataManager.switchDataSource(vDataSource);
				App vApp = new App();
				vApp.doSomethingWithAs();
				vApp.doSomethingWithBs();
				System.out.println("\n");
			}
			else
			{
				System.out.println(vDataSourceName + " is not a valid data source.");
			}
		}
	}
}
