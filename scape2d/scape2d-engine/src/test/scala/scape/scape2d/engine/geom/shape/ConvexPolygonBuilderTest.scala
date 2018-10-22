package scape.scape2d.engine.geom.shape

import org.junit.Assert
import org.junit.Test

class ConvexPolygonBuilderTest {
  @Test
  def testInitialTripletCollinearWithCollinearExtension = {
    val builder = ConvexPolygonBuilder(Point.origin, Point(1, 1), Point(2, 2))
                  .to(Point(5, 5));
    val expectedSegments = List(Segment(Point.origin, Point(1, 1)),
                                Segment(Point(1, 1), Point(2, 2)),
                                Segment(Point(2, 2), Point(5, 5)));
    Assert.assertEquals(expectedSegments, builder.segments);
  }
  
  @Test
  def testInitialTripletCollinearWithClockwiseExtension = {
    val builder = ConvexPolygonBuilder(Point.origin, Point(-1, 1), Point(-2, 2))
                  .to(Point(0, 4));
    val expectedSegments = List(Segment(Point.origin, Point(-1, 1)),
                                Segment(Point(-1, 1), Point(-2, 2)),
                                Segment(Point(-2, 2), Point(0, 4)));
    Assert.assertEquals(expectedSegments, builder.segments);
  }
  
  @Test
  def testInitialTripletCollinearWithCounterClockwiseExtension = {
    val builder = ConvexPolygonBuilder(Point.origin, Point(0, -1), Point(0, -2))
                  .to(Point(10, 0));
    val expectedSegments = List(Segment(Point.origin, Point(0, -1)),
                                Segment(Point(0, -1), Point(0, -2)),
                                Segment(Point(0, -2), Point(10, 0)));
    Assert.assertEquals(expectedSegments, builder.segments);
  }
  
  @Test
  def testClockwiseConvergingPolygonWithCollinearExtension = {
    val builder = ConvexPolygonBuilder(Point.origin, Point(1, 3), Point(3, 3))
                  .to(Point(4, 3));
    val expectedSegments = List(Segment(Point.origin, Point(1, 3)),
                                Segment(Point(1, 3), Point(3, 3)),
                                Segment(Point(3, 3), Point(4, 3)));
    Assert.assertEquals(expectedSegments, builder.segments);
  }
  
  @Test
  def testClockwiseConvergingPolygonWithClockwiseExtension = {
    val builder = ConvexPolygonBuilder(Point.origin, Point(-3, 1), Point(-3, 3))
                  .to(Point(0, 3));
    val expectedSegments = List(Segment(Point.origin, Point(-3, 1)),
                                Segment(Point(-3, 1), Point(-3, 3)),
                                Segment(Point(-3, 3), Point(0, 3)));
    Assert.assertEquals(expectedSegments, builder.segments);
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def testClockwiseConvergingPolygonWithCounterClockwiseExtensionNotPermitted:Unit = {
    ConvexPolygonBuilder(Point.origin, Point(1, 3), Point(3, 3))
    .to(Point(3, 4));
  }
  
  @Test
  def testSegmentsChainAutoclosure = {
    val convexPolygon = ConvexPolygonBuilder(Point.origin, Point(2, -2), Point(3, -1))
                        .to(Point(3, 0))
                        .build();
    val expectedSegments = List(Segment(Point.origin, Point(2, -2)),
                                Segment(Point(2, -2), Point(3, -1)),
                                Segment(Point(3, -1), Point(3, 0)),
                                Segment(Point(3, 0), Point.origin));
    val expectedConvexPolygon = ConvexPolygon(CustomPolygon(expectedSegments));
    Assert.assertEquals(expectedConvexPolygon, convexPolygon);
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def testSegmentsChainAutoclosureWithCaveNotPermitted:Unit = {
    ConvexPolygonBuilder(Point.origin, Point(2, -3), Point(3, -1))
    .to(Point(1, -1))
    .build();
  }
}