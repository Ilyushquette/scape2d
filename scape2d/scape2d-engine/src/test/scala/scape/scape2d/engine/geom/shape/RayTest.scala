package scape.scape2d.engine.geom.shape

import org.junit.Test
import org.junit.Assert
import scape.scape2d.engine.geom.Components

class RayTest {
  @Test
  def testDisplacedByComponents = {
    val ray = Ray(Point(25, -3), 4.9741883681);
    val components = Components(5.5, -2);
    Assert.assertEquals(Ray(Point(30.5, -5), 4.9741883681), ray displacedBy components);
  }
}