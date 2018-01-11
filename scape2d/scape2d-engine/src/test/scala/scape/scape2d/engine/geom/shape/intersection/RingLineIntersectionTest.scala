package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Ring
import org.junit.Assert
import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point

class RingLineIntersectionTest {
  @Test
  def testLineFullyOutsideOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val ray = Line(Point(100, 13.1), Point(0, 13.1));
    Assert.assertFalse(ring intersects ray);
  }
  
  @Test
  def testLineSlicesTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val ray = Line(Point(15, 15), Point(20, 20));
    Assert.assertTrue(ring intersects ray);
  }
}