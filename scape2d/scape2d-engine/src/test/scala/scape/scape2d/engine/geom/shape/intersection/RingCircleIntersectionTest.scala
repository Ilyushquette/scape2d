package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class RingCircleIntersectionTest {
  @Test
  def testCircleOutsideOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val circle = Circle(Point(0, 20), 1);
    Assert.assertFalse(ring intersects circle);
  }
  
  @Test
  def testCircleInTheHoleOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val circle = Circle(Point.origin, 1);
    Assert.assertFalse(ring intersects circle);
  }
  
  @Test
  def testRingInsideTheCircleDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val circle = Circle(Point(0, 20), 100);
    Assert.assertTrue(ring intersects circle);
  }
  
  @Test
  def testCircleOverlapsWithOuterCircleOfTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val circle = Circle(Point(14, 0), 2);
    Assert.assertTrue(ring intersects circle);
  }
}