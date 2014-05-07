package willey.lib.util;

import java.util.Iterator;

public class SingleIterator<T> implements Iterator<T>
{
	private boolean mHasNext = true;
	private final T mObject;
	
	SingleIterator(T pObject)
	{
		mObject = pObject;
	}
	
	@Override
	public boolean hasNext()
	{
		return mHasNext;
	}

	@Override
	public T next()
	{
		mHasNext = false;
		return mObject;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("Cannot remove");
	}
}
