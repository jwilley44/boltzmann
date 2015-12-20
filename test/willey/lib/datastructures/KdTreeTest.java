package willey.lib.datastructures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import willey.lib.datastructures.KdTree.PointSelector;
import willey.lib.datastructures.KdTree.Side;

public class KdTreeTest
{
	private static class Point
	{
		private final int mX;
		private final int mY;

		Point(int pX, int pY)
		{
			mX = pX;
			mY = pY;
		}

		int get(int pIndex)
		{
			return pIndex % 2 == 0 ? mX : mY;
		}

		@Override
		public String toString()
		{
			return "(" + mX + "," + mY + ")";
		}
	}

	private static class TestPointSelector implements PointSelector<Point>
	{
		@Override
		public Side chooseSide(Point pPoint, Point pInsertPoint, int pAxis)
		{
			double vPlane = pPoint.get(pAxis);
			double vPoint = pInsertPoint.get(pAxis);
			return Side.convert(vPoint < vPlane ? -1 : vPoint > vPlane ? 1 : 0);
		}

		@Override
		public int chooseAxis(int pDepth)
		{
			return pDepth % 2;
		}

		@Override
		public boolean contains(Point pTest, Point pEnd1, Point pEnd2, int pAxis)
		{
			double vMin = Math.min(pEnd1.get(pAxis), pEnd2.get(pAxis));
			double vMax = Math.max(pEnd1.get(pAxis), pEnd2.get(pAxis));
			double vTest = pTest.get(pAxis);
			return vTest >= vMin && vTest <= vMax;
		}

		@Override
		public int dimension()
		{
			return 2;
		}
	}

	@Test
	public void testConstruction() throws FileNotFoundException
	{
		List<Point> vPoints = new ArrayList<KdTreeTest.Point>();
		vPoints.add(from(5, 3));
		vPoints.add(from(2, 1));
		vPoints.add(from(3, 6));
		vPoints.add(from(1, 7));
		vPoints.add(from(2, 5));
		vPoints.add(from(4, 8));
		vPoints.add(from(4, 7));
		KdTree<Point> vKdTree = new KdTree<KdTreeTest.Point>(
				new TestPointSelector(), vPoints);
		PrintWriter vWriter = new PrintWriter(new File("kdtree.dot"));
		vWriter.write(vKdTree.toDot());
		vWriter.close();
	}

	@Test
	public void find()
	{
		List<Point> vPoints = new ArrayList<KdTreeTest.Point>();
		vPoints.add(from(5, 3));
		vPoints.add(from(2, 1));
		vPoints.add(from(3, 6));
		vPoints.add(from(1, 7));
		vPoints.add(from(2, 5));
		vPoints.add(from(4, 8));
		vPoints.add(from(4, 7));
		KdTree<Point> vKdTree = new KdTree<KdTreeTest.Point>(
				new TestPointSelector(), vPoints);
		Assert.assertEquals(vPoints.get(5), vKdTree.find(from(5,9)));
		Assert.assertEquals(vPoints.get(3), vKdTree.find(from(1,5)));
	}

	private static Point from(int pX, int pY)
	{
		return new Point(pX, pY);
	}
}
