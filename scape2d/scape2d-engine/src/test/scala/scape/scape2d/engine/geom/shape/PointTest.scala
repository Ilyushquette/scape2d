package scape.scape2d.engine.geom.shape

import org.junit.Test

import junit.framework.Assert
import scape.scape2d.engine.geom.Components

class PointTest {
  @Test
  def testEqualsSameCoords = Assert.assertEquals(Point(3, -12), Point(3, -12));
  
  @Test
  def testEqualsDifferentCoords = Assert.assertNotSame(Point(1, 7), Point(1, 8));
  
  @Test
  def testDisplace = {
    val position = Point(1, 4);
    val displaced = position.displace(Components(4, 1));
    Assert.assertEquals(Point(5, 5), displaced);
  }
  
  @Test
  def testDistanceTo = {
    val perspective = Point(5, 10);
    Assert.assertEquals(11.18034, perspective.distanceTo(Point.origin), 0.00001);
  }
  
  @Test
  def testDistanceToReversedOrderSameResults = {
    val perspective = Point.origin;
    Assert.assertEquals(11.18034, perspective.distanceTo(Point(5, 10)), 0.00001);
  }
  
  @Test
  def testAngleTo = {
    val perspective = Point(10, 7);
    Assert.assertEquals(225, perspective.angleTo(Point(5, 2)), 0.00001);
  }
  
  @Test
  def testPointSubtraction = {
    val p = Point(1, 0);
    val q = Point(4, -3);
    val vector = p - q;
    Assert.assertEquals(vector.magnitude, 4.24264, 0.00001);
    Assert.assertEquals(vector.angle, 135, 0.00001);
  }
}