package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Ray
import org.junit.Assert

class SegmentRayIntersectionTest {
  @Test
  def testSegmentRayVerticalDontIntersect = {
    val segment = Segment(Point(1, 3), Point(1, 5));
    val ray = Ray(Point(1, 2), 270);
    Assert.assertFalse(segment.intersects(ray));
  }
  
  @Test
  def testSegmentRayVerticalDoIntersect = {
    val segment = Segment(Point(1, 3), Point(1, 5));
    val ray = Ray(Point(1, 4), 270);
    Assert.assertTrue(segment.intersects(ray));
  }
  
  @Test
  def testSegmentRayParallelDontIntersect = {
    val segment = Segment(Point(4, 4), Point(10,10));
    val ray = Ray(Point(3, 3), 225);
    Assert.assertFalse(segment.intersects(ray));
  }
  
  @Test
  def testSegmentRayParallelDoIntersect = {
    val segment = Segment(Point(4, 4), Point(10, 10));
    val ray = Ray(Point(7, 7), 225);
    Assert.assertTrue(segment.intersects(ray));
  }
  
  @Test
  def testRayInTheDirectionAwayFromSegmentDontIntersect = {
    val segment = Segment(Point(5, 0), Point(10, 0));
    val ray = Ray(Point(7, 1), 80);
    Assert.assertFalse(segment.intersects(ray));
  }
  
  @Test
  def testSegmentDoesntReachCrossingPointWithRayDontIntersect = {
    val segment = Segment(Point(5, 0), Point(6.5, 0));
    val ray = Ray(Point(7, 1), 280);
    Assert.assertFalse(segment.intersects(ray));
  }
  
  @Test
  def testSegmentRayDoIntersect = {
    val segment = Segment(Point(5, 0), Point(10, 0));
    val ray = Ray(Point(7, 1), 280);
    Assert.assertTrue(segment.intersects(ray));
  }
}