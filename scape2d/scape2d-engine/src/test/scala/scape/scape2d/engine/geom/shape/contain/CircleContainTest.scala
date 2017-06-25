package scape.scape2d.engine.geom.shape.contain

import org.junit.Test
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class CircleContainTest {
  @Test
  def testCircleTooFarDontContain = {
    val c1 = Circle(Point(0, 0), 5);
    val c2 = Circle(Point(6, 0), 2);
    Assert.assertFalse(c1.contains(c2));
  }
  
  @Test
  def testCircleInsideOfAnotherDoContain = {
    val c1 = Circle(Point(0, 0), 5);
    val c2 = Circle(Point(3, 0), 2);
    Assert.assertTrue(c1.contains(c2));
  }
  
  @Test
  def testCirclesContainEachOtherDoContain = {
    val c1 = Circle(Point(0, 0), 5);
    val c2 = Circle(Point(0, 0), 5);
    Assert.assertTrue(c1.contains(c2));
  }
}