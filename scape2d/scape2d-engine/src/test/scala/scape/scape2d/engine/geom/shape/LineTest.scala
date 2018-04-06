package scape.scape2d.engine.geom.shape

import org.junit.Test
import org.junit.Assert
import scape.scape2d.engine.geom.Components

class LineTest {
  @Test
  def testLineSolveYForX = {
    val line = Line(Point(1, 1), Point(5, 5));
    Assert.assertEquals(7, line.forX(7), 0.00001);
  }
  
  @Test
  def testHorizontalLineSolveYForX = {
    val line = Line(Point(1, 1), Point(2, 1));
    Assert.assertEquals(1, line.forX(4), 0.00001);
  }
  
  @Test(expected=classOf[ArithmeticException])
  def testVerticalLineSolveYForX:Unit = {
    val line = Line(Point(7, 1), Point(7, 13));
    line.forX(7);
  }
  
  @Test
  def testLineSolveXForY = {
    val line = Line(Point(5, 5), Point(1, 1));
    Assert.assertEquals(-4, line.forY(-4), 0.00001);
  }
  
  @Test(expected=classOf[ArithmeticException])
  def testHorizontalLineSolveXForY:Unit = {
    val line = Line(Point(1, 1), Point(2, 1));
    line.forY(1);
  }
  
  @Test
  def testVerticalLineSolveXForY = {
    val line = Line(Point(7, 1), Point(7, 13));
    Assert.assertEquals(7, line.forY(100), 0.00001);
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