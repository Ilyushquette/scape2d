package scape.scape2d.engine.geom.shape

import org.junit.Test
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.Components
import org.junit.Assert

class CircleSweepTest {
  @Test
  def testDestinationCircleInitialization = {
    val circleSweep = CircleSweep(Circle(Point(3, 3), 2), Vector.from(Components(-3, -3)));
    Assert.assertEquals(Circle(Point(0, 0), 2), circleSweep.destinationCircle);
  }
  
  @Test
  def testConnectorInitialization = {
    val connector1 = Segment(Point(4.4142135623, 1.5857864376), Point(1.4142135623, -1.4142135623));
    val connector2 = Segment(Point(-1.4142135623, 1.4142135623), Point(1.5857864376, 4.4142135623));
    val circleSweep = CircleSweep(Circle(Point(3, 3), 2), Vector.from(Components(-3, -3)));
    Assert.assertEquals((connector1, connector2), circleSweep.connector);
  }
  
  @Test
  def testCenterInitialization = {
    val circleSweep = CircleSweep(Circle(Point.origin, 3), Vector.from(Components(4, -4)));
    Assert.assertEquals(Point(2, -2), circleSweep.center);
  }
  
  @Test
  def testDisplacedByComponents = {
    val circleSweep = CircleSweep(Circle(Point(3, 3), 2), Vector.from(Components(-3, -3)));
    val components = Components(1, 0);
    val expectedCircleSweep = CircleSweep(Circle(Point(4, 3), 2), Vector.from(Components(-3, -3)));
    Assert.assertEquals(expectedCircleSweep, circleSweep displacedBy components);
  }
}