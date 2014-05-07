package willey.lib.util;

import java.util.Iterator;

public class CombinedIterator<T> implements Iterator<T>
{
	private final Iterator<? extends T> mFirst;
	private final Iterator<? extends T> mSecond;
	
	private Iterator<? extends T> mCurrent;
	
	public CombinedIterator(Iterator<? extends T> pFirst, Iterator<? extends T> pSecond)
	{
		mFirst = pFirst;
		mSecond = pSecond;
		mCurrent = mFirst;
	}

	@Override
	public boolean hasNext()
	{
		return mFirst.hasNext() || mSecond.hasNext();
	}

	@Override
	public T next()
	{
		if (!mCurrent.hasNext())
		{
			mCurrent = mSecond;
		}
		return mCurrent.next();
	}
}
