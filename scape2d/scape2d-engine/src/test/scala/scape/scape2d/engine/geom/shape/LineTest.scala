package scape.scape2d.engine.geom.shape

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.util.InfiniteSolutions
import scape.scape2d.engine.util.NoSolution

class LineTest {
  @Test
  def testLineSolveYForX = {
    val line = Line(Point(1, 1), Point(5, 5));
    Assert.assertEquals(7, line.forX(7).solution, Epsilon);
  }
  
  @Test
  def testHorizontalLineSolveYForX = {
    val line = Line(Point(1, 1), Point(2, 1));
    Assert.assertEquals(1, line.forX(4).solution, Epsilon);
  }
  
  @Test
  def testVerticalLineSolveYForXNoSolutions = {
    val line = Line(Point(-3, -1), Point(-3, -3));
    Assert.assertEquals(NoSolution, line.forX(100));
  }
  
  @Test
  def testVerticalLineSolveYForXInfiniteSolutions = {
    val line = Line(Point(7, 1), Point(7, 13));
    Assert.assertEquals(InfiniteSolutions, line.forX(7));
  }
  
  @Test
  def testLineSolveXForY = {
    val line = Line(Point(5, 5), Point(1, 1));
    Assert.assertEquals(-4, line.forY(-4).solution, Epsilon);
  }
  
  @Test
  def testHorizontalLineSolveXForYNoSolutions = {
    val line = Line(Point(-1, -1), Point(1, -1));
    Assert.assertEquals(NoSolution, line.forY(10));
  }
  
  @Test
  def testHorizontalLineSolveXForYInfiniteSolutions = {
    val line = Line(Point(1, 1), Point(2, 1));
    Assert.assertEquals(InfiniteSolutions, line.forY(1));
  }
  
  @Test
  def testVerticalLineSolveXForY = {
    val line = Line(Point(7, 1), Point(7, 13));
    Assert.assertEquals(7, line.forY(100).solution, Epsilon);
  }
  
  @Test
  def testClampByAbscissaInterval = {
    val line = Line(Point(3, 3), Point(6, 6));
    Assert.assertEquals(Segment(Point(100, 100), Point(105, 105)), line.clampAbscissa(100, 105));
  }
  
  @Test
  def testClampByOrdinateInterval = {
    val line = Line(Point(3, 3), Point(3, 6));
    Assert.assertEquals(Segment(Point(3, 5), Point(3, 8)), line.clampOrdinate(5, 8));
  }
  
  @Test
  def testDisplacedByComponents = {
    val line = Line(Point(1, 3), Point(3, 9));
    val components = Components(-0.5, 1);
    Assert.assertEquals(Line(Point(0.5, 4), Point(2.5, 10)), line displacedBy components);
  }
}