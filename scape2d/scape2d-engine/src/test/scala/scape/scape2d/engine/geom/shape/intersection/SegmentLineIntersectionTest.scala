package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Line
import org.junit.Assert

class SegmentLineIntersectionTest {
  @Test
  def testSegmentVerticalLineDontIntersect = {
    val segment = Segment(Point(1, 1), Point(2, 0));
    val line = Line(Point(0, 0), Point(0, 1));
    Assert.assertFalse(segment.intersects(line));
  }
  
  @Test
  def testSegmentVerticalLineDoIntersect = {
    val segment = Segment(Point(2, 0), Point(0, -2));
    val line = Line(Point(0, 0), Point(0, 1));
    Assert.assertTrue(segment.intersects(line));
  }
  
  @Test
  def testSegmentHorizontalLineDontIntersect = {
    val segment = Segment(Point(1, -3), Point(1, -1));
    val line = Line(Point(0, 0), Point(1, 0));
    Assert.assertFalse(segment.intersects(line));
  }
  
  @Test
  def testSegmentHorizontalLineDoIntersect = {
    val segment = Segment(Point(1, -3), Point(1, 3));
    val line = Line(Point(0, 0), Point(1, 0));
    Assert.assertTrue(segment.intersects(line));
  }
  
  @Test
  def testVerticalSegmentDontIntersect = {
    val segment = Segment(Point(0, -3), Point(0, -1));
    val line = Line(Point(5, 5), Point(-7, -7));
    Assert.assertFalse(segment.intersects(line));
  }
  
  @Test
  def testVerticalSegmentDoIntersect = {
    val segment = Segment(Point(0, -3), Point(0, 1));
    val line = Line(Point(5, 5), Point(-7, -7));
    Assert.assertTrue(segment.intersects(line));
  }
  
  @Test
  def testDiagonalSegmentDiagonalLineDontIntersect = {
    val segment = Segment(Point(5, 10), Point(7, 8));
    val line = Line(Point(5, 5), Point(10, 10));
    Assert.assertFalse(segment.intersects(line));
  }
  
  @Test
  def testDiagonalSegmentDiagonalLineDoIntersect = {
    val segment = Segment(Point(5, 10), Point(10, 5));
    val line = Line(Point(5, 5), Point(10, 10));
    Assert.assertTrue(segment.intersects(line));
  }
  
  @Test
  def testDiagonalSegmentTouchedByDiagonalLineDoIntersect = {
    val segment = Segment(Point(5, 5), Point(4, 6));
    val line = Line(Point(3, 3), Point(2, 2));
    Assert.assertTrue(segment.intersects(line));
  }
}