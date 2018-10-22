package scape.scape2d.engine.geom.shape.intersection

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Perimeter
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.util.SomeSolution

class CirclePointPerimeterIntersectionPointTest {
  @Test
  def testPointInsideOfCircleNoSolution = {
    val circle = Circle(Point(5, 5), 5);
    val point = Point(7, 5);
    Assert.assertTrue(Perimeter.intersectionPointBetween(circle, point).hasNoSolution);
  }
  
  @Test
  def testPointOutsideOfCircleNoSolution = {
    val circle = Circle(Point(5, 5), 5);
    val point = Point(5, -1);
    Assert.assertTrue(Perimeter.intersectionPointBetween(circle, point).hasNoSolution);
  }
  
  @Test
  def testPointOnThePerimeterOfCircleSolution = {
    val circle = Circle(Point(5, 5), 5);
    val point = Point(8, 9);
    Assert.assertEquals(SomeSolution(point), Perimeter.intersectionPointBetween(circle, point));
  }
}