package willey.lib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public class FunctionUtil
{
	private FunctionUtil(){}
	
	public static <T> Function<T, Stream<T>> multiply(final int pCount)
	{
		return (pFrom) -> StreamUtil.toStream(() -> pFrom, pCount);
	}
	
	static <A, B> Function<A, Stream<Pair<A, B>>> getIncrementFunction(Stream<B> pStream)
	{
		return getIncrementFunction(pStream, (pInt) -> Integer.valueOf(pInt + 1));
	}
	
	static <A, B> Function<A, Stream<Pair<A, B>>> getIncrementFunction(Stream<B> pStream, IntFunction<Integer> pIncrementFunction)
	{
		return new IncrementedStreamFunction<A, B>(pStream, pIncrementFunction);
	}
	
	/**
	 * Sequentially apply a function to an object F returning a stream
	 * of T
	 * 	
	 * @param pCount
	 * @param pFunction
	 * @return
	 */
	public static <F, T> Function<F, Stream<T>> getMultiApplyFunction(int pCount, Function<F, T> pFunction)
	{
		return new MultiApplyFunction<F, T>(pCount, pFunction);
	}
	
	private static class IncrementedStreamFunction<A, B> implements Function<A, Stream<Pair<A, B>>>
	{
		private int mIndex;
		private final IntFunction<Integer> mIncrementFunction;
		private final List<B> mBStream;
		
		public IncrementedStreamFunction(Stream<B> pBStream, IntFunction<Integer> pIncrementFunction)
		{
			mIncrementFunction = pIncrementFunction;
			mIndex = mIncrementFunction.apply(0).intValue();
			mBStream = ConsumerUtil.toCollection(pBStream, new ArrayList<B>());
		}

		@Override
		public Stream<Pair<A,B>> apply(A pA)
		{
			Stream<Pair<A, B>> vStream = mBStream.stream().skip(mIndex).map((pB) -> Pair.of(pA, pB));
			mIndex = mIncrementFunction.apply(mIndex).intValue();
			return vStream;
		}
	}
	
	private static class MultiApplyFunction<F,T> implements Function<F, Stream<T>>
	{
		
		private final int mCount;
		private final Function<F, T> mApplyFunction;
		
		private MultiApplyFunction(int pCount, Function<F, T> pApplyFunction)
		{
			mCount = pCount;
			mApplyFunction = pApplyFunction;
		}
		
		@Override
		public Stream<T> apply(F pFrom)
		{
			return StreamUtil.toStream(() -> pFrom, mCount).sequential().map(mApplyFunction);
		}
		
	}
}
 