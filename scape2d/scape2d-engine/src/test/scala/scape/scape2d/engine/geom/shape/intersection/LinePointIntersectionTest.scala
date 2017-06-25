package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class LinePointIntersectionTest {
  @Test
  def testDiagonalLinePointDoIntersect = {
    val line = Line(Point(1, 1), Point(5, 5));
    Assert.assertTrue(line.intersects(Point(10.555, 10.555)));
  }
  
  @Test
  def testDiagonalLinePointDontIntersect = {
    val line = Line(Point(1, 1), Point(5, 5));
    Assert.assertFalse(line.intersects(Point(-10.555, 10.555)));
  }
  
  @Test
  def testVerticalLinePointDoIntersect = {
    val line = Line(Point(7, 1), Point(7, 90));
    Assert.assertTrue(line.intersects(Point(7, -120)));
  }
  
  @Test
  def testVerticalLinePointDontIntersect = {
    val line = Line(Point(7, 1), Point(7, 90));
    Assert.assertFalse(line.intersects(Point(7.3, -120)));
  }
  
  @Test
  def testHorizontalLinePointDoIntersect = {
    val line = Line(Point(3, -6), Point(30, -6));
    Assert.assertTrue(line.intersects(Point(16, -6)));
  }
  
  @Test
  def testHorizontalLinePointDontIntersect = {
    val line = Line(Point(3, -6), Point(30, -6));
    Assert.assertFalse(line.intersects(Point(16, 2)));
  }
}