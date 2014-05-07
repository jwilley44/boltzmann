package willey.lib.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class StringJoiner implements Collector<Object, StringBuilder, String>
{
	public static String join(String pSeparator, Object...pElements)
	{
		return StreamUtil.toStream(pElements).collect(new StringJoiner(pSeparator));
	}
	
	private final String mSeparator;
	private final String mAdditional;
	
	public StringJoiner(String pSeparator)
	{
		mSeparator = pSeparator;
		mAdditional = "";
	}
	
	public StringJoiner(String pSeparator, String...pAdditionalStrings)
	{
		mAdditional = StreamUtil.toStream(pAdditionalStrings).collect(new StringJoiner(pSeparator));
		mSeparator = pSeparator;
	}
	
	@Override
	public Supplier<StringBuilder> supplier()
	{
		return () -> new StringBuilder();
	}

	@Override
	public BiConsumer<StringBuilder, Object> accumulator()
	{
		return (pBuilder, pObject) -> pBuilder.append(pObject + mSeparator); 
	}

	@Override
	public BinaryOperator<StringBuilder> combiner()
	{
		return (p1, p2) -> {p1.append(p2.toString()); return p1;};
	}

	@Override
	public Function<StringBuilder, String> finisher()
	{
		return (pBuilder) -> pBuilder.append(mSeparator + mAdditional).toString().substring(0, pBuilder.lastIndexOf(mSeparator));
	}

	@Override
	public Set<java.util.stream.Collector.Characteristics> characteristics()
	{
		return new HashSet<>(Arrays.asList(Collector.Characteristics.CONCURRENT));
	}
}
