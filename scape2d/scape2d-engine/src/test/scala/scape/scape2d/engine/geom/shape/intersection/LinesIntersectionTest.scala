package scape.scape2d.engine.geom.shape.intersection

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point

class LinesIntersectionTest {
  @Test
  def testLinesDoIntersect = {
    val l1 = Line(Point(0, 100), Point(100, 0));
    val l2 = Line(Point(100, 100), Point(0, 0));
    Assert.assertTrue(l1.intersects(l2));
  }
  
  @Test
  def testParallelLinesDontIntersect = {
    val l1 = Line(Point(30, 30), Point(-10, -10));
    val l2 = Line(Point(35, 30), Point(-5, -10));
    Assert.assertFalse(l1.intersects(l2));
  }
  
  @Test
  def testParallelLinesDoIntersect = {
    val l1 = Line(Point(30, 30), Point(-10, -10));
    val l2 = Line(Point(40, 40), Point(45, 45));
    Assert.assertTrue(l1.intersects(l2));
  }
  
  @Test
  def testVerticalParallelLinesDoIntersect = {
    val l1 = Line(Point(0, 0), Point(0, -10));
    val l2 = Line(Point(0, 1), Point(0, 10));
    Assert.assertTrue(l1.intersects(l2));
  }
  
  @Test
  def testVerticalParallelLinesDontIntersect = {
    val l1 = Line(Point(0, 0), Point(0, -10));
    val l2 = Line(Point(1, 1), Point(1, 10));
    Assert.assertFalse(l1.intersects(l2));
  }
  
  @Test
  def testVerticalWithNonVerticalDoIntersect = {
    val l1 = Line(Point(0, 0), Point(0, -10));
    val l2 = Line(Point(1, 1), Point(3, 40));
    Assert.assertTrue(l1.intersects(l2));
  }
}