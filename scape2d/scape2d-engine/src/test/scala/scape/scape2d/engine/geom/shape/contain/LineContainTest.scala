package scape.scape2d.engine.geom.shape.contain

import org.junit.Test
import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class LineContainTest {
  @Test
  def testVerticalLinesDontContain = {
    val l1 = Line(Point(0, 1), Point(0, 2));
    val l2 = Line(Point(1, -1), Point(1, -2));
    Assert.assertFalse(l1.contains(l2));
  }
  
  @Test
  def testVerticalLinesDoContain = {
    val l1 = Line(Point(1, 1), Point(1, 2));
    val l2 = Line(Point(1, -1), Point(1, -2));
    Assert.assertTrue(l1.contains(l2));
  }
  
  @Test
  def testNonVerticalParallelLinesDontContain = {
    val l1 = Line(Point(1, 1), Point(3, 3));
    val l2 = Line(Point(3, 2), Point(1, 0));
    Assert.assertFalse(l1.contains(l2));
  }
  
  @Test
  def testNonVerticalParallelLinesDoContain = {
    val l1 = Line(Point(1, 1), Point(3, 3));
    val l2 = Line(Point(0, 0), Point(-1, -1));
    Assert.assertTrue(l1.contains(l2));
  }
  
  @Test
  def testNonParallelLinesDontContain = {
    val l1 = Line(Point(1, 1), Point(3, 3));
    val l2 = Line(Point(1, 3), Point(3, 1));
    Assert.assertFalse(l1.contains(l2));
  }
}