package scape.scape2d.engine.geom.shape

import scape.scape2d.engine.geom.Components
import org.junit.Assert
import org.junit.Test

class SegmentTest {
  @Test
  def testDisplacedByComponents = {
    val segment = Segment(Point(5, 5), Point(-5, -5));
    val components = Components(-4, -1);
    Assert.assertEquals(Segment(Point(1, 4), Point(-9, -6)), segment displacedBy components);
  }
}