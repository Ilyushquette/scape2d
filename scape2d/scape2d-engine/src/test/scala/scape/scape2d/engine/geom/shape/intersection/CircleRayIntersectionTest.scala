package scape.scape2d.engine.geom.shape.intersection

import java.lang.Math.PI
import scape.scape2d.engine.geom.shape.Circle
import org.junit.Test
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Ray
import org.junit.Assert
import scape.scape2d.engine.geom.HalfPI

class CircleRayIntersectionTest {
  @Test
  def testRayStartingPointInCircle = {
    val circle = Circle(Point(5, 5), 5);
    val ray = Ray(Point(3, 5), HalfPI);
    Assert.assertTrue(circle.intersects(ray));
  }
  
  @Test
  def testRayDirectionAwayFromCircleDontIntersect = {
    val circle = Circle(Point(5, 5), 5);
    val ray = Ray(Point(0, -3), 4.7298422729);
    Assert.assertFalse(circle.intersects(ray));
  }
  
  @Test
  def testCircleRayNearestPointNotInCircleDontIntersect = {
    val circle = Circle(Point(5, 5), 5);
    val ray = Ray(Point(0, -3), 0.4363323129);
    Assert.assertFalse(circle.intersects(ray));
  }
  
  @Test
  def testCircleRayNearestPointInCircleDoIntersect = {
    val circle = Circle(Point(5, 5), 5);
    val ray = Ray(Point(0, -3), 0.7853981633);
    Assert.assertTrue(circle.intersects(ray));
  }
}