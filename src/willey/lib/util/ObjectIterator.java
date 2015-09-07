package willey.lib.util;

import java.util.Iterator;

public class ObjectIterator<T> implements Iterator<T>
{
	private final T mObject;
	private final int mLimit;
	private int mCount;

	ObjectIterator(T pObject, int pLimit)
	{
		mObject = pObject;
		mLimit = pLimit;
	}

	@Override
	public boolean hasNext()
	{
		return mCount < mLimit;
	}

	@Override
	public T next()
	{
		mCount++;
		return mObject;
	}
}
