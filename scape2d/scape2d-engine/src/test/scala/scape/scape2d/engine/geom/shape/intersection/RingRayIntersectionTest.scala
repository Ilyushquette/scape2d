package scape.scape2d.engine.geom.shape.intersection

import java.lang.Math.PI
import org.junit.Test
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.geom.shape.Ray
import org.junit.Assert
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point

class RingRayIntersectionTest {
  @Test
  def testRayFullyOutsideOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val ray = Ray(Point(100, 13.1), PI);
    Assert.assertFalse(ring intersects ray);
  }
  
  @Test
  def testRaySlicesTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val ray = Ray(Point(100, 12.9), PI);
    Assert.assertTrue(ring intersects ray);
  }
  
  @Test
  def testRayFromTheHoleOfTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val ray = Ray(Point(1, 0), PI);
    Assert.assertTrue(ring intersects ray);
  }
  
  @Test
  def testRayOriginatesFromTheInsideOfTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val ray = Ray(Point(1, 11), PI);
    Assert.assertTrue(ring intersects ray);
  }
}