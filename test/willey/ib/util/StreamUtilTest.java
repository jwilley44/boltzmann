package willey.ib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.util.ConsumerUtil;
import willey.lib.util.Pair;
import willey.lib.util.StreamUtil;

public class StreamUtilTest
{
	@Test
	public void testManyMultiApply()
	{
		List<TestObject> vList = Arrays.asList(get("A"), get("B"), get("C"),
				get("D"));
		int vLimit = 10;
		Function<TestObject, TestObject> vFunction = (pTestObject) -> pTestObject
				.increment();
		Stream<TestObject> vResultStream = StreamUtil.manyMultiApply(vList
				.stream().parallel(), vLimit, vFunction);
		List<TestObject> vResults = ConsumerUtil.toCollection(vResultStream,
				Collections.synchronizedList(new ArrayList<TestObject>()));
		System.out.println(vResults);
		Assert.assertEquals(vLimit * vList.size(), vResults.size());
		Map<String, Integer> vMap = new HashMap<String, Integer>();
		for (TestObject vObject : vList)
		{
			vMap.put(vObject.mLabel, Integer.valueOf(0));
		}
		for (TestObject vObject : vResults)
		{
			int vCount = vMap.get(vObject.mLabel).intValue();
			Assert.assertTrue(vCount < vObject.mCount);
			vMap.put(vObject.mLabel, Integer.valueOf(vObject.mCount));
		}
	}
	
	@Test
	public void testNestedStream()
	{
		List<String> vTest = Arrays.asList("A", "B", "C", "D");
		int vSize = vTest.size();
		Assert.assertEquals(vSize*vSize, StreamUtil.nestedStream(vTest.stream(), vTest.stream()).count());
		Set<Pair<String, String>> vNestedList = new HashSet<Pair<String, String>>();
		for (String v1 : vTest)
		{
			for(String v2 : vTest)
			{
				vNestedList.add(Pair.of(v1, v2));
			}
		}
		Assert.assertTrue(StreamUtil.nestedStream(vTest.stream(), vTest.stream()).allMatch((pPair) -> vNestedList.contains(pPair)));
	}

	@Test
	public void testMultiply()
	{
		List<String> vStrings = Arrays.asList("A", "B", "C");
		int vMultiplier = 2;
		Assert.assertEquals(vMultiplier * vStrings.size(), StreamUtil
				.multiplyStream(vStrings.stream(), vMultiplier).count());
		Assert.assertEquals(vStrings.size(), StreamUtil
				.multiplyStream(vStrings.stream(), vMultiplier).distinct().count());
		Assert.assertEquals(vStrings.size()* vMultiplier, StreamUtil
				.multiplyStream(vStrings.stream(), vMultiplier).map((pString) -> get(pString)).distinct().count());
		
	}

	@Test
	public void testIncrementedStream()
	{
		List<String> vTest = Arrays.asList("A", "B", "C", "D");
		int vSize = vTest.size();
		Stream<Pair<String, String>> vStream = StreamUtil.getIncrementedStream(vTest.stream(), vTest.stream());
		List<Pair<String, String>> vList = ConsumerUtil.toCollection(vStream, new ArrayList<Pair<String, String>>());
		Assert.assertEquals(vSize*(vSize-1)/2, vList.size());
		List<Pair<String, String>> vExpected = new ArrayList<Pair<String,String>>();
		for (int i=0; i < vTest.size(); i++)
		{
			for (int j=i+1; j < vTest.size(); j++)
			{
				vExpected.add(Pair.of(vTest.get(i), vTest.get(j)));
			}
		}
		Assert.assertEquals(vExpected.size(), vList.size());
		Assert.assertArrayEquals(vExpected.toArray(new Pair[vExpected.size()]), vList.toArray(new Pair[vList.size()]));
	}

	private static TestObject get(String pLabel)
	{
		return new TestObject(pLabel);
	}

	private static class TestObject implements Comparable<TestObject>
	{
		private final String mLabel;
		private int mCount = 0;

		TestObject(String pLabel)
		{
			mLabel = pLabel;
		}

		TestObject increment()
		{
			mCount++;
			TestObject vReturn = new TestObject(mLabel);
			vReturn.mCount = this.mCount;
			return vReturn;
		}

		@Override
		public String toString()
		{
			return mLabel + "-" + mCount;
		}

		@Override
		public int compareTo(TestObject pTestObject)
		{
			return pTestObject.mLabel == mLabel ? Integer.compare(mCount,
					pTestObject.mCount) : 0;
		}
	}
}
