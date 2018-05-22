package scape.scape2d.engine.geom.shape

import org.junit.Test
import org.junit.Assert
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle

class RayTest {
  @Test
  def testDisplacedByComponents = {
    val ray = Ray(Point(25, -3), 285(Degree));
    val components = Components(5.5, -2);
    Assert.assertEquals(Ray(Point(30.5, -5), 285(Degree)), ray displacedBy components);
  }
}