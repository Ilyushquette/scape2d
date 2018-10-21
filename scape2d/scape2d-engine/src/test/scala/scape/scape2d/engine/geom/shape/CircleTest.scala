package scape.scape2d.engine.geom.shape

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.util.SomeSolution

class CircleTest {
  @Test
  def testCirclesNotEqualByCenter = Assert.assertNotEquals(Circle(Point(5, 5), 3),Circle(Point(5, 4), 3));
  
  @Test
  def testCirclesNotEqualByRadius = Assert.assertNotEquals(Circle(Point(5, 5), 3), Circle(Point(5, 5), 4));
  
  @Test
  def testCircleEqual = Assert.assertEquals(Circle(Point(-5, -5), 10), Circle(Point(-5, -5), 10));
  
  @Test
  def testYForXResolutionFromCircleNoSolutions = {
    val circle = Circle(Point.origin, 3);
    val solutions = circle forX 4;
    Assert.assertTrue(solutions.isEmpty);
  }
  
  @Test
  def testYForXResolutionFromCircleOneSolution = {
    val circle = Circle(Point(2, 2), 1);
    Assert.assertEquals(Set(SomeSolution(2d)), circle forX 1);
  }
  
  @Test
  def testYForXResolutionFromCircleTwoSolutions = {
    val circle = Circle(Point(2, 2), 2);
    Assert.assertEquals(Set(SomeSolution(0d), SomeSolution(4d)), circle forX 2);
  }
  
  @Test
  def testAngleResolutionForLength = {
    val circle = Circle(Point(5, 10), 3);
    Assert.assertEquals(UnboundAngle(7, Radian), circle forLength 21);
  }
  
  @Test
  def testLengthResolutionForAngle = {
    val circle = Circle(Point.origin, 5);
    Assert.assertEquals(7.8539816339, circle forAngle Angle.right.unbound, Epsilon);
  }
  
  @Test
  def testDisplacedByComponents = {
    val circle = Circle(Point.origin, 4.7);
    val components = Components(-2, 7);
    Assert.assertEquals(Circle(Point(-2, 7), 4.7), circle displacedBy components);
  }
}