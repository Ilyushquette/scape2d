package scape.scape2d.engine.geom.shape.intersection

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.util.InfiniteSolutions
import scape.scape2d.engine.util.NoSolution
import scape.scape2d.engine.util.SomeSolution

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
  
  @Test
  def testParallelDiagonalNonEqualLinesNoIntersectionPoint = {
    val l1 = Line(Point(0, 1), Point(1, 2));
    val l2 = Line(Point.origin, Point(1, 1));
    Assert.assertEquals(NoSolution, intersectionPointBetween(l1, l2));
  }
  
  @Test
  def testNonEqualVerticalLinesNoIntersectionPoint = {
    val l1 = Line(Point.origin, Point(0, -1));
    val l2 = Line(Point(1, 0), Point(1, -1));
    Assert.assertEquals(NoSolution, intersectionPointBetween(l1, l2));
  }
  
  @Test
  def testParallelDiagonalEqualLinesInfiniteIntersectionPoints = {
    val l1 = Line(Point(-5, 4), Point(-1, 0));
    val l2 = Line(Point(1, -2), Point(5, -6));
    Assert.assertEquals(InfiniteSolutions, intersectionPointBetween(l1, l2));
  }
  
  @Test
  def testEqualVerticalLinesInfiniteIntersectionPoints = {
    val l1 = Line(Point(0, -5), Point(0, -1));
    val l2 = Line(Point(0, 1), Point(0, 5));
    Assert.assertEquals(InfiniteSolutions, intersectionPointBetween(l1, l2));
  }
  
  @Test
  def testCrossingLinesIntersectionPoint = {
    val l1 = Line(Point(-1, 1), Point(-2, 2));
    val l2 = Line(Point(-2, -2), Point(-1, -1));
    Assert.assertEquals(SomeSolution(Point.origin), intersectionPointBetween(l1, l2));
  }
  
  @Test
  def testVerticalAndNonVerticalLinesIntersectionPoint = {
    val l1 = Line(Point(1, 0), Point(1, 10));
    val l2 = Line(Point(3, 10), Point(2, 5));
    Assert.assertEquals(SomeSolution(Point(1, 0)), intersectionPointBetween(l1, l2));
  }
  
  @Test
  def testNonVerticalAndVerticalLinesIntersectionPoint = {
    val l1 = Line(Point(-4, 0), Point(-2, 2));
    val l2 = Line(Point.origin, Point(0, -10));
    Assert.assertEquals(SomeSolution(Point(0, 4)), intersectionPointBetween(l1, l2));
  }
}