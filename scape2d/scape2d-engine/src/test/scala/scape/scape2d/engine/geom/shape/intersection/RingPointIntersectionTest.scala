package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class RingPointIntersectionTest {
  @Test
  def testPointOutsideOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    Assert.assertFalse(ring intersects Point(-20, 20));
  }
  
  @Test
  def testPointInTheHoleOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    Assert.assertFalse(ring intersects Point(1, -1));
  }
  
  @Test
  def testPointInsideTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    Assert.assertTrue(ring intersects Point(1, 11));
  }
}