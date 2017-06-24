package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class AxisAlignedRectanglePointIntersectionTest {
  @Test
  def testPointOutsideOfRectangleByXProjectionDontIntersect = {
    val aabb = AxisAlignedRectangle(Point(0, 0), 100, 50);
    Assert.assertFalse(aabb.intersects(Point(-1, 25)));
  }
  
  @Test
  def testPointOutsideOfRectangleByYProjectionDontIntersect = {
    val aabb = AxisAlignedRectangle(Point(0, 0), 100, 50);
    Assert.assertFalse(aabb.intersects(Point(50, -1)));
  }
  
  @Test
  def testPointInsideOfRectangleDoIntersect = {
    val aabb = AxisAlignedRectangle(Point(0, 0), 100, 50);
    Assert.assertTrue(aabb.intersects(Point(50, 25)));
  }
}