package scape.scape2d.engine.geom.shape.intersection

import java.lang.Math.PI
import org.junit.Test
import scape.scape2d.engine.geom.HalfPI
import scape.scape2d.engine.geom.shape.Ray
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class RaysIntersectionTest {
  @Test
  def testVerticalRaysDifferentDirectionsDontIntersect = {
    val r1 = Ray(Point(3, 1), HalfPI);
    val r2 = Ray(Point(3, -1), 4.7123889803);
    Assert.assertFalse(r1.intersects(r2));
  }
  
  @Test
  def testVerticalRaysDifferentDirectionsDoIntersect = {
    val r1 = Ray(Point(3, -1), HalfPI);
    val r2 = Ray(Point(3, 1), 4.7123889803);
    Assert.assertTrue(r1.intersects(r2));
  }
  
  @Test
  def testVerticalRaysSameDirectionsDoIntersect = {
    val r1 = Ray(Point(3, -1), HalfPI);
    val r2 = Ray(Point(3, 1), HalfPI);
    Assert.assertTrue(r1.intersects(r2));
  }
  
  @Test
  def testNonVerticalParallelRaysDontIntersect = {
    val r1 = Ray(Point(3, 3), 0.7853981633);
    val r2 = Ray(Point(4, 3), 3.9269908169);
    Assert.assertFalse(r1.intersects(r2));
  }
  
  @Test
  def testNonVerticalParallelRaysDifferentDirectionsDontIntersect = {
    val r1 = Ray(Point(3, 3), 0.7853981633);
    val r2 = Ray(Point(2, 2), 3.9269908169);
    Assert.assertFalse(r1.intersects(r2));
  }
  
  @Test
  def testNonVerticalParallelRaysSameDirectionsDoIntersect = {
    val r1 = Ray(Point(3, 3), 0.7853981633);
    val r2 = Ray(Point(1, 1), 0.7853981633);
    Assert.assertTrue(r1.intersects(r2));
  }
  
  @Test
  def testNonParallelRaysNoMutualPointCrossingDontIntersect = {
    val r1 = Ray(Point(-1, 1), 2.3561944901);
    val r2 = Ray(Point(1, 1), 0.7853981633);
    Assert.assertFalse(r1.intersects(r2));
  }
  
  @Test
  def testNonParallelRaysOneMutualPointCrossingDontIntersect = {
    val r1 = Ray(Point(-2, 2), 5.4977871437);
    val r2 = Ray(Point(1, 1), 0.7853981633);
    Assert.assertFalse(r1.intersects(r2));
  }
  
  @Test
  def testNonParallelRaysBothMutualPointCrossingDoIntersect = {
    val r1 = Ray(Point(-2, 2), 5.4977871437);
    val r2 = Ray(Point(2, 2), 3.9269908169);
    Assert.assertTrue(r1.intersects(r2));
  }
}