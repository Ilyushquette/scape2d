package scape.scape2d.engine.geom

import org.junit.Test
import junit.framework.Assert

class Point2DTest {
  @Test
  def testEqualsSameCoords = Assert.assertEquals(new Point2D(3, -12), new Point2D(3, -12));
  
  @Test
  def testEqualsDifferentCoords = Assert.assertNotSame(new Point2D(1, 7), new Point2D(1, 8));
  
  @Test
  def testDisplace = {
    val position = new Point2D(1, 4);
    position.displace(Components2D(4, 1));
    Assert.assertEquals(new Point2D(5, 5), position);
  }
  
  @Test
  def testLocate = {
    val point = new Point2D(780, 320.5);
    val target = new Point2D(1001.3, -2);
    point.locate(target);
    Assert.assertEquals(target, point);
  }
  
  @Test
  def testDistanceTo = {
    val perspective = new Point2D(5, 10);
    Assert.assertEquals(11.18034, perspective.distanceTo(Point2D.origin), 0.00001);
  }
  
  @Test
  def testDistanceToReversedOrderSameResults = {
    val perspective = Point2D.origin;
    Assert.assertEquals(11.18034, perspective.distanceTo(new Point2D(5, 10)), 0.00001);
  }
  
  @Test
  def testPointSubtraction = {
    val p = new Point2D(1, 0);
    val q = new Point2D(4, -3);
    val vector = p - q;
    Assert.assertEquals(vector.magnitude, 4.24264, 0.00001);
    Assert.assertEquals(vector.angle, 135, 0.00001);
  }
}