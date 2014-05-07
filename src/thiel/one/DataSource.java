package thiel.one;

import java.util.ArrayList;
import java.util.List;

public enum DataSource
{
	Default
	{
		public List<A> getAs()
		{
			List<A> vAs = new ArrayList<A>();
			vAs.add(new A(1));
			return vAs;
		}

		public List<B> getBs()
		{
			List<B> vBs = new ArrayList<B>();
			vBs.add(new B(2));
			return vBs;
		}
	},
	New
	{
		public List<A> getAs()
		{
			List<A> vAs = new ArrayList<A>();
			vAs.add(new A(100));
			return vAs;
		}

		public List<B> getBs()
		{
			List<B> vBs = new ArrayList<B>();
			vBs.add(new B(200));
			return vBs;
		}
	},
	Another
	{
		public List<A> getAs()
		{
			List<A> vAs = new ArrayList<A>();
			vAs.add(new A(100));
			vAs.add(new A(8));
			return vAs;
		}

		public List<B> getBs()
		{
			List<B> vBs = new ArrayList<B>();
			vBs.add(new B(200));
			vBs.add(new B(7));
			return vBs;
		}
	};

	abstract List<A> getAs();

	abstract List<B> getBs();
}
