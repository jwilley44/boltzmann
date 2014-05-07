package willey.lib.util;

import java.util.Collection;

public class CollectionConstructors
{
	public static <E, C extends Collection<E>> C construct(C pCollection, Supplier<E> pSupplier, int pCount)
	{
		for(int i=0; i < pCount; i++)
		{
			pCollection.add(pSupplier.get());
		}
		return pCollection;
	}
}
