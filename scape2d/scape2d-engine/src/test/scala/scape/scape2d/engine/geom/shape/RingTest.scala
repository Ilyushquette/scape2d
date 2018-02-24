package scape.scape2d.engine.geom.shape

import org.junit.Test
import scape.scape2d.engine.geom.Components
import org.junit.Assert

class RingTest {
  @Test
  def testDisplacedByComponents = {
    val ring = Ring(Circle(Point.origin, 10), 6);
    val components = Components(0, -7);
    Assert.assertEquals(Ring(Circle(Point(0, -7), 10), 6), ring displacedBy components);
  }
}