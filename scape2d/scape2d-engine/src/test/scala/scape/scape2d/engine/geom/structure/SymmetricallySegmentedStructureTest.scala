package scape.scape2d.engine.geom.structure

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment

class SymmetricallySegmentedStructureTest {
  /**
   *        |
   *        | o
   *      o |
   *    o   |   o
   *      o | o
   *        |
   *        |
   * 
   *  o - point
   *  | - line of symmetry
   */
  @Test(expected=classOf[NoSuchElementException])
  def testNonSymmetricalStructureNotPermitted:Unit = {
    val points = List(Point(-1, 1), Point(-2, 2), Point(-1, 3),
                      Point(1, 1), Point(2, 2), Point(1, 4));
    val lineOfSymmetry = Line(Point.origin, Point(0, 1));
    SymmetricallySegmentedStructure(points, lineOfSymmetry);
  }
  
  /**
   *             /
   *       o o Ø
   *       o / o
   *       Ø o o
   *     /
   * 
   *  o - point
   *  / - line of symmetry
   */
  @Test
  def testSymmetricalSegmentsGeneration = {
    val points = List(Point(1, 0), Point(1, 1), Point(1, 2), Point(2, 2),
                      Point(3, 2), Point(3, 1), Point(3, 0), Point(2, 0));
    val lineOfSymmetry = Line(Point(1, 0), Point(3, 2));
    val symmetricallySegmentedStructure = SymmetricallySegmentedStructure(points, lineOfSymmetry);
    val expectedSegments = List(Segment(Point(1, 1), Point(2, 0)),
                                Segment(Point(1, 2), Point(3, 0)),
                                Segment(Point(2, 2), Point(3, 1)));
    Assert.assertEquals(expectedSegments, symmetricallySegmentedStructure.segments);
  }
}