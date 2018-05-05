package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Ray
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Line
import org.junit.Assert

class RayLineIntersectionTest {
  @Test
  def testRayLineVerticalsDontIntersect = {
    val ray = Ray(Point(0, 0), 90);
    val line = Line(Point(1, 1), Point(1, -1));
    Assert.assertFalse(ray.intersects(line));
  }
  
  @Test
  def testRayLineVerticalsDoIntersect = {
    val ray = Ray(Point(0, 0), 90);
    val line = Line(Point(0, 0), Point(0, -1));
    Assert.assertTrue(ray.intersects(line));
  }
  
  @Test
  def testRayLineParallelDontIntersect = {
    val ray = Ray(Point(1, 3), 0);
    val line = Line(Point(0, 2), Point(1, 2));
    Assert.assertFalse(ray.intersects(line));
  }
  
  @Test
  def testRayLineParallelDoIntersect = {
    val ray = Ray(Point(1, 3), 0);
    val line = Line(Point(-1, 3), Point(0, 3));
    Assert.assertTrue(ray.intersects(line));
  }
  
  @Test
  def testRayLineBehindDontIntersect = {
    val ray = Ray(Point(0, 0), 135);
    val line = Line(Point(2, 1), Point(0, -1));
    Assert.assertFalse(ray.intersects(line));
  }
  
  @Test
  def testRayLineBehindDoIntersect = {
    val ray = Ray(Point(0, 0), 315);
    val line = Line(Point(2, 1), Point(0, -1));
    Assert.assertTrue(ray.intersects(line));
  }
}