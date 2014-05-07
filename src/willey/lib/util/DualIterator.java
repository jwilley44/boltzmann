package willey.lib.util;

import java.util.Iterator;

class DualIterator<A, B> implements Iterator<Pair<A, B>>
{
	private final Iterator<A> mA;
	private final Iterator<B> mB;

	public DualIterator(Iterator<A> pA, Iterator<B> pB)
	{
		mA = pA;
		mB = pB;
	}

	@Override
	public boolean hasNext()
	{
		return mA.hasNext() && mB.hasNext();
	}

	@Override
	public Pair<A, B> next()
	{
		return Pair.of(mA.next(), mB.next());
	}
}
