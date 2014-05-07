package willey.lib.util;

import java.util.Iterator;
import java.util.function.Supplier;

class SupplierIterator<T> implements Iterator<T>
{
	private final Supplier<T> mSupplier;
	private final int mLimit;
	private int mCount;

	SupplierIterator(Supplier<T> pSupplier, int pLimit)
	{
		mSupplier = pSupplier;
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
		return mSupplier.get();
	}
}