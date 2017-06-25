package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class SegmentPointIntersectionTest {
  @Test
  def testSegmentPointDoIntersect = {
    val segment = Segment(Point(6, 1), Point(1, 6));
    Assert.assertTrue(segment.intersects(Point(2, 5)));
  }
  
  @Test
  def testSegmentPointDontIntersect = {
    val segment = Segment(Point(6, 1), Point(1, 6));
    Assert.assertFalse(segment.intersects(Point(0, 7)));
  }
}