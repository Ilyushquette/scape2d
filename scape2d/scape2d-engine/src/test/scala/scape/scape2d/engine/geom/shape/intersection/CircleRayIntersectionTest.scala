package scape.scape2d.engine.geom.shape.intersection

import scape.scape2d.engine.geom.shape.Circle
import org.junit.Test
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Ray
import org.junit.Assert

class CircleRayIntersectionTest {
  @Test
  def testRayStartingPointInCircle = {
    val circle = Circle(Point(5, 5), 5);
    val ray = Ray(Point(3, 5), 90);
    Assert.assertTrue(circle.intersects(ray));
  }
  
  @Test
  def testRayDirectionAwayFromCircleDontIntersect = {
    val circle = Circle(Point(5, 5), 5);
    val ray = Ray(Point(0, -3), 271);
    Assert.assertFalse(circle.intersects(ray));
  }
  
  @Test
  def testCircleRayNearestPointNotInCircleDontIntersect = {
    val circle = Circle(Point(5, 5), 5);
    val ray = Ray(Point(0, -3), 25);
    Assert.assertFalse(circle.intersects(ray));
  }
  
  @Test
  def testCircleRayNearestPointInCircleDoIntersect = {
    val circle = Circle(Point(5, 5), 5);
    val ray = Ray(Point(0, -3), 45);
    Assert.assertTrue(circle.intersects(ray));
  }
}