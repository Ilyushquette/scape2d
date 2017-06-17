package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class SegmentsIntersectionTest {
  @Test
  def testVerticalSegmentsDifferentX = {
    val s1 = Segment(Point(1, 1), Point(1, 10));
    val s2 = Segment(Point(-1, 1), Point(-1, 10));
    Assert.assertFalse(s1.intersects(s2));
  }
  
  @Test
  def testVerticalSegmentsSameXDifferentYIntervals = {
    val s1 = Segment(Point(1, 1), Point(1, 10));
    val s2 = Segment(Point(1, -1), Point(1, -10));
    Assert.assertFalse(s1.intersects(s2));
  }
  
  @Test
  def testVerticalSegmentsDoIntersect = {
    val s1 = Segment(Point(1, -1), Point(1, 10));
    val s2 = Segment(Point(1, 1), Point(1, -10));
    Assert.assertTrue(s1.intersects(s2));
  }
  
  @Test
  def testNonVerticalParallelSegmentsDifferentYIntercepts = {
    val s1 = Segment(Point(1, 1), Point(10, 1));
    val s2 = Segment(Point(1, 2), Point(10, 2));
    Assert.assertFalse(s1.intersects(s2));
  }
  
  @Test
  def testNonVerticalParallelSegmentsSameYInterceptsDifferentXIntervals = {
    val s1 = Segment(Point(1, 1), Point(10, 1));
    val s2 = Segment(Point(11, 1), Point(13, 1));
    Assert.assertFalse(s1.intersects(s2));
  }
  
  @Test
  def testNonVerticalParallelSegmentsDoIntersect = {
    val s1 = Segment(Point(1, 1), Point(10, 1));
    val s2 = Segment(Point(-1, 1), Point(13, 1));
    Assert.assertTrue(s1.intersects(s2));
  }
  
  @Test
  def testVerticalWithDiagonalSegmentsDoIntersect = {
    val s1 = Segment(Point(1, 1), Point(1, 10));
    val s2 = Segment(Point(0, 1), Point(25, 10));
    Assert.assertTrue(s1.intersects(s2));
  }
  
  @Test
  def testVerticalWithDiagonalSegmentsDontIntersect = {
    val s1 = Segment(Point(1, 1), Point(1, 10));
    val s2 = Segment(Point(0, 1), Point(-25, 10));
    Assert.assertFalse(s1.intersects(s2));
  }
  
  @Test
  def testDiagonalSegmentsDoIntersect = {
    val s1 = Segment(Point(0, 10), Point(10, 0));
    val s2 = Segment(Point(0, 0), Point(10, 10));
    Assert.assertTrue(s1.intersects(s2));
  }
  
  @Test
  def testDiagonalSegmentsDontIntersect = {
    val s1 = Segment(Point(0, 10), Point(10, 0));
    val s2 = Segment(Point(0, 0), Point(4.9, 4.9));
    Assert.assertFalse(s1.intersects(s2));
  }
}