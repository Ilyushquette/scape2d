package scape.scape2d.engine.geom.shape.intersection

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Perimeter
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.util.SomeSolution

class CircleLinePerimeterIntersectionPointsTest {
  @Test
  def testDiagonalLineMissesCircleNoIntersectionPoints = {
    val circle = Circle(Point.origin, 3);
    val line = Line(Point(0, 6), Point(6, 0));
    Assert.assertTrue(Perimeter.intersectionPointsBetween(circle, line).isEmpty);
  }
  
  @Test
  def testHorizontalLineTangentToCircleOneIntersectionPoint = {
    val circle = Circle(Point(2, 2), 1);
    val line = Line(Point(-1, 3), Point(0, 3));
    val expectedIntersectionPoints = Set(SomeSolution(Point(2, 3)));
    Assert.assertEquals(expectedIntersectionPoints, Perimeter.intersectionPointsBetween(circle, line));
  }
  
  @Test
  def testDiagonalLineSecantThroughCircleTwoIntersectionPoints = {
    val circle = Circle(Point(2, 2), 2);
    val line = Line(Point(0, -2), Point(1, -1));
    val expectedIntersectionPoints = Set(SomeSolution(Point(2, 0)), SomeSolution(Point(4, 2)));
    Assert.assertEquals(expectedIntersectionPoints, Perimeter.intersectionPointsBetween(circle, line));
  }
  
  @Test
  def testVerticalLineMissesCircleNoIntersectionPoints = {
    val circle = Circle(Point.origin, 3);
    val line = Line(Point(4, -1), Point(4, 1));
    Assert.assertTrue(Perimeter.intersectionPointsBetween(circle, line).isEmpty);
  }
  
  @Test
  def testVerticalLineTangentToCircleOneIntersectionPoint = {
    val circle = Circle(Point(2, 2), 1);
    val line = Line(Point(1, -2), Point(1, -3));
    val expectedIntersectionPoints = Set(SomeSolution(Point(1, 2)));
    Assert.assertEquals(expectedIntersectionPoints, Perimeter.intersectionPointsBetween(circle, line));
  }
  
  @Test
  def testVerticalLineSecantThroughCircleTwoIntersectionPoints = {
    val circle = Circle(Point(5, 5), 5);
    val line = Line(Point(1, 20), Point(1, 21));
    val expectedIntersectionPoints = Set(SomeSolution(Point(1, 2)), SomeSolution(Point(1, 8)));
    Assert.assertEquals(expectedIntersectionPoints, Perimeter.intersectionPointsBetween(circle, line));
  }
}