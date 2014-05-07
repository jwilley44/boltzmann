package willey.lib.math;

import java.util.Iterator;

public class Sequence<T> implements Iterable<T>
{
	private final SequenceElement<T> mFirst;
	private final SequenceElement<T> mLast;
	
	public Sequence(SequenceElement<T> pFirst, SequenceElement<T> pLast)
	{
		mFirst = pFirst;
		mLast = pLast;
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return new SequenceIterator<T>(this);
	}
	
	private static class SequenceIterator<T> implements Iterator<T>
	{
		private final Sequence<T> mSequence;
		private SequenceElement<T> mCurrent;
		
		private SequenceIterator(Sequence<T> pSequence)
		{
			mSequence = pSequence;
			mCurrent = mSequence.mFirst;
		}

		@Override
		public boolean hasNext()
		{
			return mSequence.mLast.greaterThan(mCurrent.getElement());
		}

		@Override
		public T next()
		{
			T vCurrent = mCurrent.getElement();
			mCurrent = mCurrent.next();
			return vCurrent;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("Cannot remove element");
		}
	}
}
