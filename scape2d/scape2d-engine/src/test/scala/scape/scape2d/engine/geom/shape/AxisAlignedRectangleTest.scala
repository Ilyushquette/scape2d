package scape.scape2d.engine.geom.shape

import org.junit.Test
import org.junit.Assert

class AxisAlignedRectangleTest {
  @Test(expected=classOf[IllegalArgumentException])
  def testSlicePiecesNegativeValue:Unit = {
    val aabb = AxisAlignedRectangle(Point(10, 10), 100, 100);
    aabb.slice(-1);
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def testSlicePiecesNotPerfectlySquared:Unit = {
    val aabb = AxisAlignedRectangle(Point(10, 10), 100, 100);
    aabb.slice(5);
  }
  
  @Test
  def testSliceByFourPieces = {
    val aabb = AxisAlignedRectangle(Point(0, 0), 100, 100);
    val expected = Array(AxisAlignedRectangle(Point(0, 0), 50, 50),
                         AxisAlignedRectangle(Point(50, 0), 50, 50),
                         AxisAlignedRectangle(Point(0, 50), 50, 50),
                         AxisAlignedRectangle(Point(50, 50), 50, 50));
    val pieces = aabb.slice(4);
    Assert.assertTrue(expected.size == pieces.size && expected.forall(pieces.contains));
  }
}