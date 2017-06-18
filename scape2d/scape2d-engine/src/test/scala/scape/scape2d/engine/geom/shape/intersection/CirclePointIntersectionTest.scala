package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import org.junit.Assert
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point

class CirclePointIntersectionTest {
  @Test
  def testCirclePointDontIntersect = {
    val circle = Circle(Point(3, 3), 3);
    Assert.assertFalse(circle.intersects(Point(7, 7)));
  }
  
  @Test
  def testCirclePointDoIntersect = {
    val circle = Circle(Point(3, 3), 10);
    Assert.assertTrue(circle.intersects(Point(7, 7)));
  }
}