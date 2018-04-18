package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class CirclesIntersectionTest {
  @Test
  def testCirclesDontIntersect = {
    val c1 = Circle(Point(0, 0), 5);
    val c2 = Circle(Point(10, 10), 5);
    Assert.assertFalse(c1.intersects(c2));
  }
  
  @Test
  def testCirclesDoIntersect = {
    val c1 = Circle(Point(0, 0), 7.5);
    val c2 = Circle(Point(10, 10), 7.5);
    Assert.assertTrue(c1.intersects(c2));
  }
}