package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment

class GeomFunctionsTest {
  @Test
  def testFetchWaypoints = {
    val segments = Array(Segment(Point(0, 0), Point(3, 1)),
                         Segment(Point(3, 1), Point(3, 10)),
                         Segment(Point(3, 10), Point(0, 10)),
                         Segment(Point(0, 10), Point(0, 0)));
    val expected = Set(Point(0, 0), Point(3, 1), Point(3, 10), Point(0, 10), Point(0, 0));
    Assert.assertEquals(expected, fetchWaypoints(segments.iterator));
  }
}
