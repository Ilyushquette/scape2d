package scape.scape2d.engine.geom.shape

import org.junit.Test
import org.junit.Assert

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
}