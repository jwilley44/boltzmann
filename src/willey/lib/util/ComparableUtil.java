package willey.lib.util;

import java.util.Comparator;

public class ComparableUtil
{
	public static class DoubleComparator implements Comparator<Double>
	{
		@Override
		public int compare(Double p1, Double p2)
		{
			return Double.compare(p1, p2);
		}
	}
}
