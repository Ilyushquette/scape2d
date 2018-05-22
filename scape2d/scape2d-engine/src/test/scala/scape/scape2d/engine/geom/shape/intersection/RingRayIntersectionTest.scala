package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.geom.shape.Ray
import org.junit.Assert
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point

class RingRayIntersectionTest {
  @Test
  def testRayFullyOutsideOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val ray = Ray(Point(100, 13.1), 180(Degree));
    Assert.assertFalse(ring intersects ray);
  }
  
  @Test
  def testRaySlicesTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val ray = Ray(Point(100, 12.9), 180(Degree));
    Assert.assertTrue(ring intersects ray);
  }
  
  @Test
  def testRayFromTheHoleOfTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val ray = Ray(Point(1, 0), 180(Degree));
    Assert.assertTrue(ring intersects ray);
  }
  
  @Test
  def testRayOriginatesFromTheInsideOfTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val ray = Ray(Point(1, 11), 180(Degree));
    Assert.assertTrue(ring intersects ray);
  }
}