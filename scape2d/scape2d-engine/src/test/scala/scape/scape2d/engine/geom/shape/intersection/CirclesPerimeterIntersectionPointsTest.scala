package scape.scape2d.engine.geom.shape.intersection

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Perimeter
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.util.InfiniteSolutions
import scape.scape2d.engine.util.SomeSolution

class CirclesPerimeterIntersectionPointsTest {
  @Test
  def testCirclesDontIntersectNoSolutions = {
    val c1 = Circle(Point(5, 5), 5);
    val c2 = Circle(Point(5, -4), 3);
    Assert.assertTrue(Perimeter.intersectionPointsBetween(c1, c2).isEmpty);
  }
  
  @Test
  def testCircle1ContainsCircle2NoSolutions = {
    val c1 = Circle(Point(5, 5), 5);
    val c2 = Circle(Point(5, 8), 1);
    Assert.assertTrue(Perimeter.intersectionPointsBetween(c1, c2).isEmpty);
  }
  
  @Test
  def testCircle2ContainsCircle1NoSolutions = {
    val c1 = Circle(Point(9, 5), 1);
    val c2 = Circle(Point(5, 5), 5);
    Assert.assertTrue(Perimeter.intersectionPointsBetween(c1, c2).isEmpty);
  }
  
  @Test
  def testCirclesTouchEachOtherOneSolution = {
    val c1 = Circle(Point(5, 5), 5);
    val c2 = Circle(Point(13, 5), 3);
    Assert.assertEquals(Set(SomeSolution(Point(10, 5))), Perimeter.intersectionPointsBetween(c1, c2));
  }
  
  @Test
  def testCircle1IntersectsCircle2WithoutCoveringCenterTwoSolutions = {
    val c1 = Circle(Point(5, 5), 5);
    val c2 = Circle(Point(9, 1), 1);
    val expectedIntersectionPoints = Set(SomeSolution(Point(8, 1)), SomeSolution(Point(9, 2)));
    Assert.assertEquals(expectedIntersectionPoints, Perimeter.intersectionPointsBetween(c1, c2));
  }
  
  @Test
  def testCircle2IntersectsCircle1WithCoveringCenterTwoSolutions = {
    val c1 = Circle(Point(8, 8), 1);
    val c2 = Circle(Point(5, 5), 5);
    val expectedIntersectionPoints = Set(SomeSolution(Point(8, 9)), SomeSolution(Point(9, 8)));
    Assert.assertEquals(expectedIntersectionPoints, Perimeter.intersectionPointsBetween(c1, c2));
  }
  
  @Test
  def testEqualCirclesInfiniteSolutions = {
    val c1 = Circle(Point.origin, 3);
    val c2 = Circle(Point.origin, 3);
    Assert.assertEquals(Set(InfiniteSolutions), c1.perimeter intersectionPointsWith c2.perimeter);
  }
}