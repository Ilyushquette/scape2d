package scape.scape2d.engine.geom.shape

import org.junit.Test
import scape.scape2d.engine.geom.Components
import org.junit.Assert

class PolygonTest {
  @Test
  def testDisplacedByComponents = {
    val polygon = PolygonBuilder(Point(3, 3), Point(4, 4), Point(5, 3)).build;
    val components = Components(-3, -3);
    val expectedPolygon = PolygonBuilder(Point.origin, Point(1, 1), Point(2, 0)).build;
    Assert.assertEquals(expectedPolygon, polygon displacedBy components);
  }
}