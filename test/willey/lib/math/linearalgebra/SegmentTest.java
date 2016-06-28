package willey.lib.math.linearalgebra;

import static willey.lib.math.linearalgebra.CartesianVector.of;
import static willey.lib.math.linearalgebra.CartesianVector.randomVector;
import static willey.lib.math.linearalgebra.SegmentUtil.get;

import org.junit.Test;

import willey.lib.math.linearalgebra.SegmentUtil.Segment;
import willey.lib.test.AbstractTest;

public class SegmentTest extends AbstractTest
{
	@Test
	public void testPointDistance()
	{
		CartesianVector vP1 = randomVector();
		Segment vS1 = SegmentUtil.get(vP1, vP1);
		
		CartesianVector vP2 = randomVector();
		Segment vS2 = SegmentUtil.get(vP2, vP2);
		
		assertEquals(vP1.distance(vP2), vS1.distance(vS2), 1e-4);
		assertEquals(vS1.distance(vS2), vS2.distance(vS1), 1e-4);
	}
	
	@Test
	public void testPoint2RayDistance()
	{
		Segment vS1 = SegmentUtil.get(of(0,0,0), of(0, 0, 10));
		CartesianVector vMove = of(1,0,0);
		CartesianVector vP1 = vS1.getPoint(0.5).add(vMove);
		Segment vS2 = SegmentUtil.get(vP1, vP1);
		assertEquals(vS2.distance(vS1), vS1.distance(vS2), 1e-4);
		assertEquals(vMove.magnitude(), vS1.distance(vS2), 1e-4);
	}
	
	@Test
	public void testRay2RayDistance()
	{
//		Segment vS1 = SegmentUtil.get(of(4, 0, 0), of(-4, 0, 0));
//		Segment vS2 = SegmentUtil.get(of(0,4,1), of(0,-4,1));
		
		Segment vS1 = SegmentUtil.get(of(4, 0,0), of(-4,0,0));
		Segment vS2 = SegmentUtil.get(of(4, 4, 1), of(-4, -4, 1));
		assertSegments(vS1, vS2, 1.0);
	}
	
	@Test
	public void testRay2RayDistance2()
	{
		assertSegments(get(of(4, 0, 0), of(-4, 0, 0)), get(of(0,4,1), of(0,-4,1)), 1.0);
		assertSegments(get(of(4, 0,0), of(-4,0,0)), get(of(4, 4,1), of(-5,-5,1)), 1.0);
		assertSegments(get(of(5,0,0), of(-5,0,0)), get(of(0, 5, 6), of(0,-5,-4)), Math.sqrt(0.5));
		assertSegments(get(of(1.7792358966648145, -0.9130045792520912, -0.027244490893851836),
				of(11.779235896664815, -0.9130045792520912, -0.027244490893851836)), get(of(0, 0, 0), of(10,0,0)), 0.91341098);
	}
	
	private void assertSegments(Segment p1, Segment p2, double pExpected)
	{
		assertEquals(pExpected, p1.distance(p2), 1e-4);
		assertEquals(pExpected, p2.distance(p1), 1e-4);
	}
}
