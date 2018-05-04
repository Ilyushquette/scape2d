package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Ray
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class RaysIntersectionTest {
  @Test
  def testVerticalRaysDifferentDirectionsDontIntersect = {
    val r1 = Ray(Point(3, 1), 90);
    val r2 = Ray(Point(3, -1), 270);
    Assert.assertFalse(r1.intersects(r2));
  }
  
  @Test
  def testVerticalRaysDifferentDirectionsDoIntersect = {
    val r1 = Ray(Point(3, -1), 90);
    val r2 = Ray(Point(3, 1), 270);
    Assert.assertTrue(r1.intersects(r2));
  }
  
  @Test
  def testVerticalRaysSameDirectionsDoIntersect = {
    val r1 = Ray(Point(3, -1), 90);
    val r2 = Ray(Point(3, 1), 90);
    Assert.assertTrue(r1.intersects(r2));
  }
  
  @Test
  def testNonVerticalParallelRaysDontIntersect = {
    val r1 = Ray(Point(3, 3), 45);
    val r2 = Ray(Point(4, 3), 225);
    Assert.assertFalse(r1.intersects(r2));
  }
  
  @Test
  def testNonVerticalParallelRaysDifferentDirectionsDontIntersect = {
    val r1 = Ray(Point(3, 3), 45);
    val r2 = Ray(Point(2, 2), 225);
    Assert.assertFalse(r1.intersects(r2));
  }
  
  @Test
  def testNonVerticalParallelRaysSameDirectionsDoIntersect = {
    val r1 = Ray(Point(3, 3), 45);
    val r2 = Ray(Point(1, 1), 45);
    Assert.assertTrue(r1.intersects(r2));
  }
  
  @Test
  def testNonParallelRaysNoMutualPointCrossingDontIntersect = {
    val r1 = Ray(Point(-1, 1), 135);
    val r2 = Ray(Point(1, 1), 45);
    Assert.assertFalse(r1.intersects(r2));
  }
  
  @Test
  def testNonParallelRaysOneMutualPointCrossingDontIntersect = {
    val r1 = Ray(Point(-2, 2), 315);
    val r2 = Ray(Point(1, 1), 45);
    Assert.assertFalse(r1.intersects(r2));
  }
  
  @Test
  def testNonParallelRaysBothMutualPointCrossingDoIntersect = {
    val r1 = Ray(Point(-2, 2), 315);
    val r2 = Ray(Point(2, 2), 225);
    Assert.assertTrue(r1.intersects(r2));
  }
}