package scape.scape2d.engine.geom.structure

import org.junit.Test
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert
import scape.scape2d.engine.geom.shape.Segment

class HingedSegmentedStructureTest {
  @Test
  def testHingedSegmentedStructureGeneration = {
    val suspension = Point(0, 1);
    val points = List(Point(1, 1), Point(2, 1), Point(-1, 0));
    val expected = List(Segment(suspension, Point(1, 1)),
                        Segment(suspension, Point(2, 1)),
                        Segment(suspension, Point(-1, 0)));
    val structure = HingedSegmentedStructure(suspension, points);
    Assert.assertEquals(expected, structure.segments);
  }
}