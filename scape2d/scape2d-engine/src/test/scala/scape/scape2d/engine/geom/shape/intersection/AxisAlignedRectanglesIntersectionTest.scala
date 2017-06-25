package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import org.junit.Assert

import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Point

class AxisAlignedRectanglesIntersectionTest {
  @Test
  def testSeparatedXProjectionOfRectangleDontIntersect = {
    val aabb1 = AxisAlignedRectangle(Point(50, 50), 30, 30);
    val aabb2 = AxisAlignedRectangle(Point(0, 50), 25, 25);
    Assert.assertFalse(aabb1.intersects(aabb2));
  }
  
  @Test
  def testSeparatedYProjectionOfRectangleDontIntersect = {
    val aabb1 = AxisAlignedRectangle(Point(50, 50), 30, 30);
    val aabb2 = AxisAlignedRectangle(Point(40, 0), 25, 25);
    Assert.assertFalse(aabb1.intersects(aabb2));
  }
  
  @Test
  def testRectanglesDoIntersect = {
    val aabb1 = AxisAlignedRectangle(Point(50, 50), 30, 30);
    val aabb2 = AxisAlignedRectangle(Point(25, 50), 25, 25);
    Assert.assertTrue(aabb1.intersects(aabb2));
  }
}