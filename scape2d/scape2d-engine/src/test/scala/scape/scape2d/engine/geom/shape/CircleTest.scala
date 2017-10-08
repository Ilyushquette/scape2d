package scape.scape2d.engine.geom.shape

import org.junit.Assert
import org.junit.Test

class CircleTest {
  @Test
  def testCirclesNotEqualByCenter = Assert.assertNotEquals(Circle(Point(5, 5), 3),Circle(Point(5, 4), 3));
  
  @Test
  def testCirclesNotEqualByRadius = Assert.assertNotEquals(Circle(Point(5, 5), 3), Circle(Point(5, 5), 4));
  
  @Test
  def testCircleEqual = Assert.assertEquals(Circle(Point(-5, -5), 10), Circle(Point(-5, -5), 10));
}