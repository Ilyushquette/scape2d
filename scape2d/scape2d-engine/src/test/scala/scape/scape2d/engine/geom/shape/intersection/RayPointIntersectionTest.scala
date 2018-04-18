package scape.scape2d.engine.geom.shape.intersection

import java.lang.Math.PI
import org.junit.Test
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Ray
import org.junit.Assert

class RayPointIntersectionTest {
  @Test
  def testRayWithRayStartingPointDoIntersect = {
    val startingPoint = Point(2, 7);
    val ray = Ray(startingPoint, 0);
    Assert.assertTrue(ray.intersects(startingPoint));
  }
  
  @Test
  def testRayPointBehindDontIntersect = {
    val ray = Ray(Point(5, -5), 2.3561944901);
    Assert.assertFalse(ray.intersects(Point(6, -6)));
  }
  
  @Test
  def testRayPointOnTheWayDoIntersect = {
    val ray = Ray(Point(5, -5), 2.3561944901);
    Assert.assertTrue(ray.intersects(Point(-1, 1)));
  }
  
  @Test
  def testRayPointDontIntersect = {
    val ray = Ray(Point(0, 0), PI);
    Assert.assertFalse(ray.intersects(Point(-30, 1)));
  }
}