package willey.lib.util;

import java.util.Iterator;
import java.util.function.BiFunction;

public class ZipIterator<A, B, C> implements Iterator<C>
{
	private final Iterator<A> mA;
	private final Iterator<B> mB;
	private final BiFunction<A, B, C> mCombiner;

	public ZipIterator(Iterator<A> pA, Iterator<B> pB, BiFunction<A, B, C> pCombiner)
	{
		mA = pA;
		mB = pB;
		mCombiner = pCombiner;
	}

	@Override
	public boolean hasNext()
	{
		return mA.hasNext() && mB.hasNext();
	}

	@Override
	public C next()
	{
		return mCombiner.apply(mA.next(), mB.next());
	}
}
