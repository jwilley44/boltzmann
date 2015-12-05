package willey.lib.datastructures;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.math.MathUtil;
import willey.lib.math.geometry.Metric;
import willey.lib.util.CollectionConstructors;
import willey.lib.util.Supplier;
import willey.lib.util.Timer;

public class CoverTreeTest
{
	private static final Metric<Double> kTestMetric = new TestMetric();

	@Test
	public void testCoverTree() throws Exception
	{
		CoverTree2<Double> vCoverTree = new CoverTree2<Double>(kTestMetric,
				Double.valueOf(2.1), 2);
		vCoverTree.insert(Double.valueOf(3.2));
		vCoverTree.insert(Double.valueOf(2.9));
		vCoverTree.insert(Double.valueOf(4.1));
		vCoverTree.insert(Double.valueOf(3.7));
		System.out.println(vCoverTree.getString());
		Assert.assertEquals(Double.valueOf(3.7),
				vCoverTree.getNearesetNeighbor((Double.valueOf(3.6))).getA());
	}
	
	@Test
	public void testInsert()
	{
		CoverTree2<Double> vCoverTree = new CoverTree2<Double>(kTestMetric, Double.valueOf(MathUtil.nextRandomBetween(0, 20)), 1.5);
		int vSize = 1000;
		for(int i=1; i < vSize ; i++)
		{
			vCoverTree.insert(Double.valueOf(MathUtil.nextRandomBetween(0, 20)));
		}
		Assert.assertEquals(vSize, vCoverTree.size());
	}
	
	@SuppressWarnings("unused")
	private static final double[] kDoubles = new double[] { 7.36093474937793,
		12.144945517950134, 1.4974055540703524, 11.409550250214993,
		11.501652411167358, 12.578750903378511, 19.15958403198488,
		17.77730138381552, 0.7427444192482802, 17.15300889682413,
		17.357410849679326, 15.746582201981983, 8.049303834960286,
		18.443613214394723, 1.1699531619876646, 6.614444218097962,
		13.333778100450262, 18.378130995146392, 15.969755892361778,
		12.821876215264833, 2.564585846339964, 11.439801949909665,
		14.834421039085715, 8.559969577925608, 1.9408914734500349,
		19.20372738919682, 6.238280196502835, 16.826980589960222,
		3.7186368698443473, 15.451462492184216, 13.189322045298681,
		8.090714664919522, 10.214304784873827, 14.957882313810915,
		18.980441719541133, 6.1208069396711995, 5.579321219121107,
		11.016852971191717, 1.8529906776622607, 5.090626490411556,
		1.1293889049321648, 16.869892878926326, 11.124494485574171,
		8.708698646611335, 9.329467271326637, 14.424659583868396,
		10.711343884264096, 16.924681250876688, 3.9765073661400363,
		19.99776565105473, 12.481128875943917, 8.665102865330459,
		19.688469075634686, 1.289074026812993, 1.4982369566539577,
		0.08921484935565882, 12.499538099271959, 2.392280943028071,
		17.98020100357709, 17.015470526945762, 5.177886473929045,
		14.369256171641368, 6.661221518818148, 7.454300674710359,
		15.823704319438006, 7.763257515221045, 13.263173788058584,
		15.593012446346643, 16.371661302890274, 0.39880459066120366,
		19.903326180786788, 2.1137881752952015, 8.813335745022428,
		8.913638247005256, 6.521897191293684, 7.385930172604198,
		1.2715804342141568, 5.690864171548693, 3.791490930261392,
		13.858634315573465, 13.500185281034128, 14.207022776114401,
		4.4743494856794275, 11.890754304413747, 1.9486048612995988,
		5.284490299448477, 4.822274191203617, 9.289151734063053,
		8.222930843272515, 4.334272880082342, 15.367968057722138,
		8.98740727167354, 1.5906945460623767, 5.397902570469024,
		11.335462281668104, 11.581797197478787, 6.118480451312265,
		11.644816746444972, 6.901192960125928, 19.85162375206283 };
	@Test
	public void testContains()
	{
		int vSize =  5;
		List<Double> vPoints = CollectionConstructors.construct(new ArrayList<Double>(), new DoubleSupplier(), vSize);
		Assert.assertEquals(vSize, vPoints.size());
		CoverTree2<Double> vCoverTree = new CoverTree2<Double>(kTestMetric,
				vPoints.get(0), 1.5);
		for (Double vPoint : vPoints.subList(1, vPoints.size()))
		{
			vCoverTree.insert(vPoint);
		}
   		System.out.println(vCoverTree.getString());
		Assert.assertEquals(vSize, vCoverTree.size());
		for(Double vPoint : vPoints)
		{
			if (!vPoint.equals(vCoverTree.getNearesetNeighbor(vPoint)))
			{
				vCoverTree.getNearesetNeighbor(vPoint);
			}
			Assert.assertEquals(vPoint, vCoverTree.getNearesetNeighbor(vPoint).getA());
		}
	}

	@Test
	public void testRemove() throws Exception
	{
		CoverTree2<Double> vCoverTree = new CoverTree2<Double>(kTestMetric,
				Double.valueOf(2.1), 1.5);
		vCoverTree.insert(Double.valueOf(3.2));
		vCoverTree.insert(Double.valueOf(2.9));
		vCoverTree.insert(Double.valueOf(4.1));
		vCoverTree.insert(Double.valueOf(3.7));
		Assert.assertEquals(Double.valueOf(3.7),
				vCoverTree.getNearesetNeighbor(Double.valueOf(3.6)).getA());
		vCoverTree.remove(Double.valueOf(3.7));
		Assert.assertEquals(Double.valueOf(4.1),
				vCoverTree.getNearesetNeighbor(Double.valueOf(3.8)).getA());
		for (int i=0; i < 100; i++)
		{
			Double vDouble = Double.valueOf(MathUtil.getThreadLocal().nextDouble()*MathUtil.getThreadLocal().nextInt(10));
			vCoverTree.insert(vDouble);
			vCoverTree.remove(vDouble);
		}
	}

	@Test
	public void testRemoveRoot() throws Exception
	{
		CoverTree2<Double> vCoverTree = new CoverTree2<Double>(kTestMetric,
				Double.valueOf(2.1), 1.5);
		vCoverTree.insert(Double.valueOf(3.2));
		vCoverTree.insert(Double.valueOf(2.9));
		vCoverTree.insert(Double.valueOf(4.1));
		vCoverTree.insert(Double.valueOf(3.7));
		vCoverTree.insert(Double.valueOf(2.0));
		vCoverTree.remove(Double.valueOf(2.1));
		Assert.assertEquals(Double.valueOf(2.0), vCoverTree.getNearesetNeighbor(Double.valueOf(2.2)).getA());
	}

	@Test
	public void testFindNNTime() throws IOException
	{
		File vConstructionTimes = new File("coverTree.findNN.times.tsv");
		FileWriter vWriter = new FileWriter(vConstructionTimes);
		vWriter.write("size\tconst.time\tfind.NN.time\n");
		for (int i = 2; i <= 12; i++)
		{
			Double vRoot = MathUtil.nextRandomBetween(0, 1000);
			
			CoverTree2<Double> vTree = new CoverTree2<Double>(kTestMetric,
					vRoot, 1.5);
			Timer vCons = Timer.start();
			for (int j = 0; j < Math.pow(2, i); j++)
			{

				vTree.insert(Double.valueOf(MathUtil.nextRandomBetween(0,
						1000)));
			}
			double vConsTime = vCons.getElapsedTimeSeconds();
			
			int vCount = 100000;
			Timer vTimer = Timer.start();
			for (int k = 0; k < vCount; k++)
			{
				vTree.getNearesetNeighbor(Double.valueOf(MathUtil.nextRandomBetween(0, 1000)));
			}
			double vTime = vTimer.getElapsedTimeSeconds() / vCount;
			
			vWriter.write(Math.pow(2, i) +"\t" + vConsTime +"\t" + vTime + "\n");
		}
		vWriter.close();
	}
	
	@Test
	public void testRemoveTime() throws IOException
	{
		File vConstructionTimes = new File("coverTree.remove.times.tsv");
		FileWriter vWriter = new FileWriter(vConstructionTimes);
		vWriter.write("size\tremoval.time\n");
		for (int i = 2; i <= 5; i++)
		{
			int vSize = (int)Math.pow(2, i);
			List<Double> vPoints = CollectionConstructors.construct(new ArrayList<Double>(), new DoubleSupplier(), vSize);
			CoverTree2<Double> vCoverTree = new CoverTree2<Double>(kTestMetric, vPoints.get(0), 1.5);
			for (Double vPoint : vPoints.subList(1, vSize))
			{
				vCoverTree.insert(vPoint);
			}
			
			Collections.shuffle(vPoints);
			Timer vTimer = Timer.start();
			int vCount = 100;
			for (int j=0; j < vCount; j++)
			{
				for(Double vPoint : vPoints)
				{
					vCoverTree.remove(vPoint);
					vCoverTree.insert(vPoint);
				}
			}
			double vTime = vTimer.getElapsedTimeSeconds();
			vWriter.write(Math.pow(2, i) + " \t" + vTime + "\n");
		}
		vWriter.close();
	}

	private static class TestMetric implements Metric<Double>
	{
		@Override
		public double compute(Double pPoint1, Double pPoint2)
		{
			return Math.abs(pPoint1 - pPoint2);
		}

	}
	
	private static class DoubleSupplier implements Supplier<Double>
	{
		@Override
		public Double get()
		{
			return Double.valueOf(MathUtil.nextRandomBetween(0, 20));
		}
	}
}
