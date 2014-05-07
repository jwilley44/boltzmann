package willey.lib.math.linearalgebra;

import static willey.lib.math.linearalgebra.CartesianVector.of;
import static willey.lib.math.linearalgebra.CartesianVector.randomUnitVector;
import static willey.lib.math.linearalgebra.CartesianVector.randomVector;
import junit.framework.Assert;

import org.junit.Test;

import willey.lib.test.AbstractTest;


public class LineSegmentTest extends AbstractTest
{
	@Test
	public void getPoint()
	{
		LineSegment v1 = new LineSegment(of(0, 0, 1.0), of(0,0,2.0));
		Assert.assertTrue(v1.getPoint(0.5).coordinatesEqual(of(0,0,1.5)));
	}
	
	@Test
	public void translate()
	{
		LineSegment v1 = new LineSegment(randomVector(), randomVector());
		CartesianVector vTranslate = randomVector();
		LineSegment vMoved = v1.translate(vTranslate);
		Assert.assertTrue(v1.start().add(vTranslate).coordinatesEqual(vMoved.start()));
		Assert.assertTrue(v1.end().add(vTranslate).coordinatesEqual(vMoved.end()));
		Assert.assertTrue(v1.direction().coordinatesEqual(vMoved.direction()));
		Assert.assertTrue(v1.length() == vMoved.length());
	}
	
	@Test
	public void rotate()
	{
		LineSegment v1 = new LineSegment(randomVector(), randomVector());
		LineSegment vRotated = v1.changeDirectionThroughMiddle(randomUnitVector());
		Assert.assertTrue(v1.length() == vRotated.length());
		Assert.assertTrue(v1.getPoint(0.5).coordinatesEqual(vRotated.getPoint(0.5)));
	}
	
	@Test
	public void testClosestPoint()
	{
		LineSegment v1 = new LineSegment(of(0,0,0), of(10,0,0));
		LineSegment v2 = new LineSegment(of(0,0,0), of(0,1,0));
		Assert.assertTrue(v1.closestPoint(v2).coordinatesEqual(of(0,0,0)));
		LineSegment v3 = new LineSegment(of(1,0,1), of (-10,0,0));
		Assert.assertTrue(v1.closestPoint(v3).coordinatesEqual(of(0,0,0)));
		LineSegment v4 = new LineSegment(of(2,2,2), of(2,-2,2));
		Assert.assertTrue(v1.closestPoint(v4).coordinatesEqual(of(2,0,0)));
		LineSegment v5 = new LineSegment(of(5,1,1), of(-1, -5,1));
		Assert.assertTrue(v1.closestPoint(v5).coordinatesEqual(of(4,0,0)));
	}
}
