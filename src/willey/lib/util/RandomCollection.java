package willey.lib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import willey.lib.math.MathUtil;

public class RandomCollection<E> implements Collection<E>
{
	private final List<E> mElements = new ArrayList<E>();
	
	public E getNextRandom()
	{
		return mElements.get(MathUtil.getThreadLocal().nextInt(mElements.size()));
	}
	
	@Override
	public boolean add(E e)
	{
		return mElements.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		return mElements.addAll(c);
	}

	@Override
	public void clear()
	{
		mElements.clear();
	}

	@Override
	public boolean contains(Object o)
	{
		return mElements.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return mElements.containsAll(c);
	}

	@Override
	public boolean isEmpty()
	{
		return mElements.isEmpty();
	}

	@Override
	public Iterator<E> iterator()
	{
		List<E> vElements = new ArrayList<E>(mElements);
		Collections.shuffle(vElements, MathUtil.getThreadLocal());
		return  vElements.iterator();
	}

	@Override
	public boolean remove(Object o)
	{
		return mElements.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return mElements.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return mElements.retainAll(c);
	}

	@Override
	public int size()
	{
		return mElements.size();
	}

	@Override
	public Object[] toArray()
	{
		return mElements.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return mElements.toArray(a);
	}
}	
