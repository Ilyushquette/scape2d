package scape.scape2d.engine.geom.shape.intersection

import scape.scape2d.engine.geom.shape.Circle
import org.junit.Test
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment
import org.junit.Assert

class CircleSegmentIntersection {
  @Test
  def testEdgeOfSegmentInCircleDoIntersect = {
    val circle = Circle(Point(0, 0), 10);
    val segment = Segment(Point(5, 5), Point(100, 5));
    Assert.assertTrue(circle.intersects(segment));
  }
  
  @Test
  def testSegmentP1EdgeNearestToCircleDontIntersect = {
    val circle = Circle(Point(0, 0), 5);
    val segment = Segment(Point(5, 5), Point(10, 5));
    Assert.assertFalse(circle.intersects(segment));
  }
  
  @Test
  def testSegmentP2EdgeNearestToCircleDontIntersect = {
    val circle = Circle(Point(0, 0), 5);
    val segment = Segment(Point(10, 5), Point(5, 5));
    Assert.assertFalse(circle.intersects(segment));
  }
  
  @Test
  def testCircleSegmentBodyDoIntersect = {
    val segment = Segment(Point(0, 10), Point(10, 0));
    val circle = Circle(Point(0, 0), 7.5);
    Assert.assertTrue(circle.intersects(segment));
  }
}