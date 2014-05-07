package willey.lib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil
{
	public static <A, B> Stream<Pair<A, B>> dualSream(Stream<A> pA, Stream<B> pB)
	{
		Check.kIllegalArgument.checkTrue(!pA.equals(pB), "pA and pB cannot be the same instance.");
		return toStream(new DualIterator<A, B>(pA.iterator(), pB.iterator()),
				pA.isParallel() && pB.isParallel());
	}
	
	public static <A> Stream<A> multiplyStream(Stream<A> pStream, int pMultiplier)
	{
		return pStream.flatMap(FunctionUtil.multiply(pMultiplier));
	}

	public static <A, B> Stream<Pair<A, B>> nestedStream(Stream<A> pAStream,
			Stream<B> pBStream)
	{
		Check.kIllegalArgument.checkTrue(!pAStream.equals(pBStream), "pA and pB cannot be the same instance.");
		List<B> vBStream = ConsumerUtil.toCollection(pBStream, new ArrayList<B>());
		return pAStream.flatMap((pA) -> vBStream.stream().map((pB) -> Pair.of(pA, pB)));
	}

	public static <T> Stream<T> combine(Stream<? extends T> p1,
			Stream<? extends T> p2)
	{
		Check.kIllegalArgument.checkTrue(!p1.equals(p2), "p1 and p2 cannot be the same instance.");
		return toStream(new CombinedIterator<T>(p1.iterator(), p2.iterator()),
				p1.isParallel() && p2.isParallel());
	}

	public static <T> Stream<T> toStream(Iterator<T> pIterator,
			boolean pIsParallel)
	{
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(pIterator, 0), pIsParallel);
	}
	
	public static <A, B> Stream<Pair<A, B>> getIncrementedStream(Stream<A> pStream, Stream<B> pStream2Increment)
	{
		Check.kIllegalArgument.checkTrue(!pStream.equals(pStream2Increment), "pStream and pStream2Increment cannot be the same instance.");
		return pStream.flatMap(FunctionUtil.getIncrementFunction(pStream2Increment));
	}
	
	public static <A, B> Stream<Pair<A, B>> getIncrementedStream(Stream<A> pStream, Stream<B> pStream2Increment, IntFunction<Integer> pIncrementFunction)
	{
		Check.kIllegalArgument.checkTrue(!pStream.equals(pStream2Increment), "pStream and pStream2Increment cannot be the same instance.");
		return pStream.flatMap(FunctionUtil.getIncrementFunction(pStream2Increment, pIncrementFunction));
	}
	
	@SafeVarargs
	public static <T> Stream<T> toStream(T...pElements)
	{
		return Arrays.asList(pElements).stream();
	}
	
	public static <T> Stream<T> toStream(Supplier<T> pSupplier, int pLimit)
	{
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
				new SupplierIterator<T>(pSupplier, pLimit), 0), true);
	}
	
	public static <F,T> Stream<T> manyMultiApply(Stream<F> pStream, int pApply, Function<F, T> pFunction)
	{
		return pStream.flatMap(FunctionUtil.getMultiApplyFunction(pApply, pFunction));
	}
}
