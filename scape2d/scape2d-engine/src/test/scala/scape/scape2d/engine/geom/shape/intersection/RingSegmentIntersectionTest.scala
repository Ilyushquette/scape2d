package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment
import org.junit.Assert

class RingSegmentIntersectionTest {
  @Test
  def testSegmentOutsideOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val segment = Segment(Point(-14, 0), Point(-20, 20));
    Assert.assertFalse(ring intersects segment);
  }
  
  @Test
  def testSegmentInTheHoleOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val segment = Segment(Point(-1, 0), Point(1, 1));
    Assert.assertFalse(ring intersects segment);
  }
  
  @Test
  def testSegmentSlicesTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val segment = Segment(Point(13, 13), Point(-13, -13));
    Assert.assertTrue(ring intersects segment);
  }
  
  @Test
  def testSegmentInsideAndInTheHoleOfTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val segment = Segment(Point.origin, Point(1, 11));
    Assert.assertTrue(ring intersects segment);
  }
  
  @Test
  def testSegmentInsideTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val segment = Segment(Point(-1, 11), Point(1, 11));
    Assert.assertTrue(ring intersects segment);
  }
}