package scape.scape2d.engine.geom.shape

import org.junit.Assert
import org.junit.Test
import scape.scape2d.engine.geom.Components

class CircleTest {
  @Test
  def testCirclesNotEqualByCenter = Assert.assertNotEquals(Circle(Point(5, 5), 3),Circle(Point(5, 4), 3));
  
  @Test
  def testCirclesNotEqualByRadius = Assert.assertNotEquals(Circle(Point(5, 5), 3), Circle(Point(5, 5), 4));
  
  @Test
  def testCircleEqual = Assert.assertEquals(Circle(Point(-5, -5), 10), Circle(Point(-5, -5), 10));
  
  @Test
  def testDisplacedByComponents = {
    val circle = Circle(Point.origin, 4.7);
    val components = Components(-2, 7);
    Assert.assertEquals(Circle(Point(-2, 7), 4.7), circle displacedBy components);
  }
}