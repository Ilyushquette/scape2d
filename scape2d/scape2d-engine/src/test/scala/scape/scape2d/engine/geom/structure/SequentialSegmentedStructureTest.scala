package scape.scape2d.engine.geom.structure

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment

class SequentialSegmentedStructureTest {
  @Test
  def testSequentialStructureEmptyFromNoPoints = {
    val structure = SequentialSegmentedStructure(List());
    Assert.assertEquals(Nil, structure.segments);
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def testNoSequentialStructureFromSinglePoint:Unit = {
    SequentialSegmentedStructure(List(Point.origin));
  }
  
  @Test
  def testSequentialStructureGeneration = {
    val points = List(Point.origin, Point(5, 3), Point(-1, 10));
    val expected = List(Segment(Point.origin, Point(5, 3)), Segment(Point(5, 3), Point(-1, 10)));
    val structure = SequentialSegmentedStructure(points);
    Assert.assertEquals(expected, structure.segments);
  }
  
  @Test
  def testSequentialStructureClosedGeneration = {
    val points = List(Point.origin, Point(5, 3), Point(-1, 10));
    val expected = List(Segment(Point.origin, Point(5, 3)),
                        Segment(Point(5, 3), Point(-1, 10)),
                        Segment(Point(-1, 10), Point.origin));
    val structure = SequentialSegmentedStructure.closed(points);
    Assert.assertEquals(expected, structure.segments);
  }
  
  @Test
  def testSequentialStructureAlreadyClosedGeneration = {
    val points = List(Point.origin, Point(5, 3), Point(-1, 10), Point.origin);
    val expected = List(Segment(Point.origin, Point(5, 3)),
                        Segment(Point(5, 3), Point(-1, 10)),
                        Segment(Point(-1, 10), Point.origin));
    val structure = SequentialSegmentedStructure.closed(points);
    Assert.assertEquals(expected, structure.segments);
  }
  
  @Test
  def testSequentialStructureTwoPointsUncloseableGeneration = {
    val points = List(Point(-5, 5), Point(5, 5));
    val structure = SequentialSegmentedStructure.closed(points);
    Assert.assertEquals(List(Segment(Point(-5, 5), Point(5, 5))), structure.segments);
  }
}