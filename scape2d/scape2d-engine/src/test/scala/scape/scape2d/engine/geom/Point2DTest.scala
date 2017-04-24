package scape.scape2d.engine.geom

import org.junit.Test
import junit.framework.Assert

class Point2DTest {
  @Test
  def testEqualsSameCoords = Assert.assertEquals(new Point2D(3, -12), new Point2D(3, -12));
  
  @Test
  def testEqualsDifferentCoords = Assert.assertNotSame(new Point2D(1, 7), new Point2D(1, 8));
  
  @Test
  def testDisplace = {
    val position = new Point2D(1, 4);
    position.displace(Components2D(4, 1));
    Assert.assertEquals(new Point2D(5, 5), position);
  }
}