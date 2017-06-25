package scape.scape2d.engine.geom.shape.contain

import org.junit.Test
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Circle
import org.junit.Assert

class AxisAlignedRectangleCircleContainTest {
  @Test
  def testCircleOutsideOfRectangleDontContain = {
    val aabb = AxisAlignedRectangle(Point(0, 0), 100, 100);
    val circle = Circle(Point(150, 50), 10);
    Assert.assertFalse(aabb.contains(circle));
  }
  
  @Test
  def testCircleOutsideOfRectangleXProjectionSafezoneDontContain = {
    val aabb = AxisAlignedRectangle(Point(0, 0), 100, 100);
    val circle = Circle(Point(95, 50), 10);
    Assert.assertFalse(aabb.contains(circle));
  }
  
  @Test
  def testCircleOutsideOfRectangleYProjectionSafezoneDontContain = {
    val aabb = AxisAlignedRectangle(Point(0, 0), 100, 100);
    val circle = Circle(Point(85, 95), 10);
    Assert.assertFalse(aabb.contains(circle));
  }
  
  @Test
  def testCircleInsideOfRectangleSafezoneDontContain = {
    val aabb = AxisAlignedRectangle(Point(0, 0), 100, 100);
    val circle = Circle(Point(85, 90), 10);
    Assert.assertTrue(aabb.contains(circle));
  }
}