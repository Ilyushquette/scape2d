package scape.scape2d.engine.geom.shape

import org.junit.Test
import org.junit.Assert

class PolygonBuilderTest {
  @Test
  def testPolygonSegmentsCorrectness = {
    val p1 = Point(0, 0);
    val p2 = Point(1, 1);
    val p3 = Point(2, 0);
    val p4 = Point(1, -1);
    val p5 = Point(0, 0);
    val polygon = PolygonBuilder(p1, p2, p3).to(p4).to(p5).build;
    val expectedSegments = List(Segment(p1, p2), Segment(p2, p3), Segment(p3, p4), Segment(p4, p5))
    Assert.assertEquals(expectedSegments, polygon.segments);
  }
  
  @Test
  def testPolygonAutoclosure = {
    val polygon = PolygonBuilder(Point(0, 0), Point(1, 1), Point(2, 0))
                  .to(Point(1, -1)).build;
    Assert.assertEquals(Segment(Point(1, -1), Point(0, 0)), polygon.segments.last);
  }
}