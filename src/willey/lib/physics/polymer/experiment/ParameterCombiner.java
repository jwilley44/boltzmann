package willey.lib.physics.polymer.experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import willey.lib.util.Pair;

public class ParameterCombiner
{
	private static final String kNameValueSep = "=";
	private static final String kValueSep = ",";
	
	private enum ParameterType
	{
		D((pString) -> Double.valueOf(pString.trim()))
		{
			@Override
			Stream<?> getValueStream(java.lang.String[] pValues)
			{
				return Arrays.asList(pValues).stream().map((pString) -> Double.valueOf(pString.trim()));
			}
		},
		I((pString) -> Integer.valueOf(pString.trim()))
		{
			@Override
			Stream<?> getValueStream(java.lang.String[] pValues)
			{
				return Arrays.asList(pValues).stream().map((pString) -> Integer.valueOf(pString.trim()));
			}
		},
		S((pString) -> pString.trim())
		{
			@Override
			Stream<?> getValueStream(java.lang.String[] pValues)
			{
				return Arrays.asList(pValues).stream();
			}
		};
		
		private final Function<String, Object> mConverter;
		
		ParameterType(Function<String, Object> pConverter)
		{
			mConverter = pConverter;
		}
		
		abstract Stream<?> getValueStream(String[] pValues);
	}
	
	private final Map<String, Pair<List<String>, Function<String, Object>>> mParameters = new HashMap<String, Pair<List<String>, Function<String, Object>>>();

	public ParameterCombiner(File pParameterFile) throws IOException
	{
		BufferedReader vReader = new BufferedReader(new FileReader(pParameterFile));
		vReader.lines().forEach((pLine) -> parseAndSet(pLine));
		vReader.close();
	}
	
	public Stream<ParameterMap> getParameterCombinations()
	{
		List<Map<String, Object>> vParameterSets = new ArrayList<Map<String, Object>>();
		SetUp vSetUp = new SetUp();
		mParameters.entrySet().stream().forEach(vSetUp);
		Map<String, Function<String, Object>> vFunctionMap = new HashMap<String, Function<String, Object>>();
		mParameters.entrySet().stream().forEach((pEntry) -> vFunctionMap.put(pEntry.getKey(), pEntry.getValue().getB()));
		for (int i=0; i < vSetUp.mCombinations; i++)
		{
			Combinations vCombination = new Combinations(vFunctionMap, i);
			vSetUp.mValues.stream().forEach(vCombination);
			vParameterSets.add(vCombination.mValueMap);
		}
		return vParameterSets.stream().map((pMap) -> new ParameterMap(pMap));
	}
	
	public int getNumberOfEquilibrations()
	{
		Pair<List<String>, Function<String, Object>> vPair = mParameters.get("Equilibrations");
		return ((Integer)vPair.getB().apply(vPair.getA().get(0))).intValue();
	}
	
	public boolean continuousRun()
	{
		return mParameters.get("Type").getA().get(0).equals("Continuous");
	}
	
	private void parseAndSet(String pLine)
	{
			String[] vLine = pLine.split(kNameValueSep);
			ParameterType vType = ParameterType.valueOf(vLine[0]);
			String vName = vLine[1];
			String[] vValues = vLine[2].split(kValueSep);
			mParameters.put(vName, Pair.of(Arrays.asList(vValues), vType.mConverter));
	}
	
	private static class SetUp implements Consumer<Entry<String, Pair<List<String>, Function<String, Object>>>>
	{
		private int mCombinations = 1;
		List<Pair<String, List<String>>> mValues = new ArrayList<Pair<String,List<String>>>();
		
		@Override
		public void accept(
				Entry<String, Pair<List<String>, Function<String, Object>>> pEntry)
		{
			List<String> vValues = pEntry.getValue().getA();
			mValues.add(Pair.of(pEntry.getKey(), vValues));
			mCombinations *= vValues.size();
		}
		
	}
	
	private static class Combinations implements Consumer<Pair<String, List<String>>>
	{
		private final Map<String, Object> mValueMap = new HashMap<String, Object>();
		private final Map<String, Function<String, Object>> mFunctionMap;
		
		private final int mI;
		private int mJ;
		
		Combinations(Map<String, Function<String, Object>> pFunctionMap, int pCounter)
		{
			mFunctionMap = pFunctionMap;
			mI = pCounter;
			mJ = 1;
		}
		
		@Override
		public void accept(Pair<String, List<String>> pPair)
		{
			int vIndex = mI/mJ%pPair.getB().size();
			mValueMap.put(pPair.getA(), mFunctionMap.get(pPair.getA()).apply(pPair.getB().get(vIndex)));
			mJ *= pPair.getB().size();
		}
	}
	
	
	public static class ParameterMap
	{
		private final Map<String, Object> mMap;
		
		private ParameterMap(Map<String, Object> pMap)
		{
			mMap = pMap;
		}
		
		public Object get(String pName)
		{
			return mMap.get(pName);
		}
		
		public int getInt(String pName)
		{
			return ((Integer)mMap.get(pName)).intValue();
		}
		
		public double getDouble(String pName)
		{
			return ((Double)mMap.get(pName)).doubleValue();
		}
		
		public String getString(String pName)
		{
			return get(pName).toString();
		}
		
		@Override
		public int hashCode()
		{
			return mMap.hashCode();
		}
		
		@Override
		public boolean equals(Object pObject)
		{
			boolean vEquals = pObject instanceof ParameterMap;
			if (vEquals)
			{
				vEquals = mMap.equals(((ParameterMap)pObject).mMap);
			}
			return vEquals;
		}
	}
}
