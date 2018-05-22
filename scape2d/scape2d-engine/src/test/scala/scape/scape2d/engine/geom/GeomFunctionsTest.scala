package scape.scape2d.engine.geom

import org.junit.Assert
import org.junit.Test
import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment

class GeomFunctionsTest {
  @Test
  def testFindDiagonalCrossedLinesMutualX = {
    val l1 = Line(Point(0, 100), Point(100, 0));
    val l2 = Line(Point(100, 100), Point(0, 0));
    Assert.assertEquals(50, findMutualX(l1, l2), 0.00001);
  }
  
  @Test
  def testFindVerticalWithDiagonalLinesMutualX = {
    val l1 = Line(Point(0, 100), Point(0, 0));
    val l2 = Line(Point(100, 100), Point(5, 50));
    Assert.assertEquals(0, findMutualX(l1, l2), 0.00001);
  }
  
  @Test(expected=classOf[ArithmeticException])
  def testFindParallelLinesMutualX:Unit = {
    val l1 = Line(Point(10, 100), Point(10, 0));
    val l2 = Line(Point(10, 100), Point(10, 0));
    findMutualX(l1, l2);
  }
  
  @Test
  def testFindMutualPoint = {
    val l1 = Line(Point(0, 100), Point(100, 0));
    val l2 = Line(Point(100, 100), Point(0, 0));
    Assert.assertEquals(Point(50, 50), findMutualPoint(l1, l2));
  }
  
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