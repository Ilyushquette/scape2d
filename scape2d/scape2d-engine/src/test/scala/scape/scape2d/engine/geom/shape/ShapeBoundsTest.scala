package scape.scape2d.engine.geom.shape

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.Vector2D

class ShapeBoundsTest {
  @Test
  def testPointShapeBounds = {
    val bounds = ShapeBounds(Point(3, -1));
    Assert.assertEquals(AxisAlignedRectangle(Point(3, -1), 0, 0), bounds);
  }
  
  @Test
  def testDiagonalSegmentShapeBounds = {
    val bounds = ShapeBounds(Segment(Point(1, 1), Point(-2, -2)));
    Assert.assertEquals(AxisAlignedRectangle(Point(-2, -2), 3, 3), bounds);
  }
  
  @Test
  def testVerticalSegmentShapeBounds = {
    val bounds = ShapeBounds(Segment(Point(0, -1), Point(0, 1)));
    Assert.assertEquals(AxisAlignedRectangle(Point(0, -1), 0, 2), bounds);
  }
  
  @Test
  def testCircleShapeBounds = {
    val bounds = ShapeBounds(Circle(Point(0, 0), 10));
    Assert.assertEquals(AxisAlignedRectangle(Point(-10, -10), 20, 20), bounds);
  }
  
  @Test
  def testAxisAlignedRectangleShapeBounds = {
    val bounds = ShapeBounds(AxisAlignedRectangle(Point(-50, 0), 100, 50));
    Assert.assertEquals(AxisAlignedRectangle(Point(-50, 0), 100, 50), bounds);
  }
  
  /**
   *     /\
   *    /  \
   *   /____\
   */
  @Test
  def testPolygonShapeBounds = {
    val triangle = PolygonBuilder(Point(6, 3), Point(8, 6), Point(10, 3)).build;
    val bounds = ShapeBounds(triangle);
    Assert.assertEquals(AxisAlignedRectangle(Point(6, 3), 4, 3), bounds);
  }
  
  @Test
  def testCircleSweepShapeBounds = {
    val circleSweep = CircleSweep(Circle(Point(3, 3), 2), new Vector2D(10, 75));
    val bounds = ShapeBounds(circleSweep);
    Assert.assertEquals(AxisAlignedRectangle(Point(1, 1), 6.588190451025207, 13.659258262890683), bounds);
  }
}