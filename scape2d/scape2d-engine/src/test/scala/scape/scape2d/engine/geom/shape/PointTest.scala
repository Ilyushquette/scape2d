package scape.scape2d.engine.geom.shape

import org.junit.Test
import junit.framework.Assert
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector

class PointTest {
  @Test
  def testEqualsSameCoords = Assert.assertEquals(Point(3, -12), Point(3, -12));
  
  @Test
  def testEqualsDifferentCoords = Assert.assertNotSame(Point(1, 7), Point(1, 8));
  
  @Test
  def testComponentsAddition = {
    val position = Point(1, 4);
    val displaced = position + Components(4, 1);
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
    Assert.assertEquals(3.92699, perspective angleTo Point(5, 2), 0.00001);
  }
  
  @Test
  def testAngleToDeg = {
    val perspective = Point(10, 7);
    Assert.assertEquals(225, perspective angleToDeg Point(5, 2), 0.00001);
  }
  
  @Test
  def testPointSubtraction = {
    val p = Point(1, 0);
    val q = Point(4, -3);
    Assert.assertEquals(Vector(4.2426406871, 2.3561944901), p - q);
  }
}