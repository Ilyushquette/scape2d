package scape.scape2d.engine.geom.shape

import scape.scape2d.engine.geom.Components
import org.junit.Assert
import org.junit.Test

class SegmentTest {
  @Test
  def testDifferentByBothPoints = {
    Assert.assertFalse(Segment(Point.origin, Point(-1, -1)) == Segment(Point(2, 7), Point(10, 6)));
  }
  
  @Test
  def testDifferentByOnePoint = {
    Assert.assertFalse(Segment(Point.origin, Point(-1, -1)) == Segment(Point.origin, Point(1, 1)));
  }
  
  @Test
  def testSameWithSameOrderOfPoints = {
    Assert.assertTrue(Segment(Point(-10, 10), Point(10, 10)) == Segment(Point(-10, 10), Point(10, 10)));
  }
  
  @Test
  def testSameWithSwappedPoints = {
    Assert.assertTrue(Segment(Point(-10, -10), Point(10, -10)) == Segment(Point(10, -10), Point(-10, -10)));
  }
  
  @Test
  def testDisplacedByComponents = {
    val segment = Segment(Point(5, 5), Point(-5, -5));
    val components = Components(-4, -1);
    Assert.assertEquals(Segment(Point(1, 4), Point(-9, -6)), segment displacedBy components);
  }
}