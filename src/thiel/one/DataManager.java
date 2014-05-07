package thiel.one;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataManager
{
	private static final DataManager kManager = new DataManager();
	
	private DataSource mDataSource;
	private final Lock mLock = new ReentrantLock();
	
	private DataManager()
	{
		mDataSource = DataSource.Default;
	}
	
	public static void switchDataSource(DataSource pNewDataSource)
	{
		kManager.mLock.lock();
		kManager.mDataSource = pNewDataSource;
		System.err.println("Warning: data source has been changed to " + pNewDataSource);
		kManager.mLock.unlock();
	}
	
	public static List<A> getAs()
	{
		waitForSwitch();
		return kManager.mDataSource.getAs();
	}

	public static List<B> getBs()
	{
		waitForSwitch();
		return kManager.mDataSource.getBs();
	}
	
	private static void waitForSwitch()
	{
		while (!kManager.mLock.tryLock()){};
	}
}
