package scape.scape2d.engine.geom.shape

import org.junit.Test
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.geom.Components2D
import org.junit.Assert

class CircleSweepTest {
  @Test
  def testDestinationCircleInitialization = {
    val circleSweep = CircleSweep(Circle(Point(3, 3), 2), Vector2D.from(Components2D(-3, -3)));
    Assert.assertEquals(Circle(Point(0, 0), 2), circleSweep.destinationCircle);
  }
  
  @Test
  def testConnectorInitialization = {
    val connector1 = Segment(Point(4.4142135623, 1.5857864376), Point(1.4142135623, -1.4142135623));
    val connector2 = Segment(Point(-1.4142135623, 1.4142135623), Point(1.5857864376, 4.4142135623));
    val circleSweep = CircleSweep(Circle(Point(3, 3), 2), Vector2D.from(Components2D(-3, -3)));
    Assert.assertEquals((connector1, connector2), circleSweep.connector);
  }
}