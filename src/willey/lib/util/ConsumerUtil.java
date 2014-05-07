package willey.lib.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ConsumerUtil
{
	public static <T, C extends Collection<T>> C toCollection(Stream<T> pStream, C pCollection)
	{
		ToCollectionConsumer<T, C> vConsumer = new ToCollectionConsumer<T, C>(pCollection);
		pStream.forEach(vConsumer);
		return vConsumer.getCollection();
	}
	
	static class ToCollectionConsumer<T, C extends Collection<T>> implements Consumer<T>
	{
		private final C mCollection;
		
		public ToCollectionConsumer(C pCollection)
		{
			mCollection = pCollection;
		}
		
		@Override
		public void accept(T t)
		{
			mCollection.add(t);
		}
		
		public C getCollection()
		{
			return mCollection;
		}
	}
}
