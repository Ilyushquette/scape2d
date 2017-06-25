package scape.scape2d.engine.geom.shape.intersection

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.PolygonBuilder

class PolygonCircleIntersectionTest {
  /**
   *        @@@@@
   *      @@@@@@@@@    _________
   *     @@@@@@@@@@@  /         |
   *      @@@@@@@@@  /          |
   *        @@@@@   /           |
   *               /____________|
   *   
   *   /|\- - TETRAGON
   *   @ - CIRCLE
   */
  @Test
  def testCircleTooFarFromTetragonDontIntersect = {
    val polygon = PolygonBuilder(Point(6, 0), Point(8, 4), Point(12, 4))
                  .to(Point(12, 0)).build;
    val circle = Circle(Point(4, 4), 3);
    Assert.assertFalse(polygon.intersects(circle));
  }
  
  /**
   *           @@@@@
   *         @@@@@@@@@ _________
   *        @@@@@@@@@@@         |
   *         @@@@@@@@@          |
   *           @@@@@/           |
   *               /____________|
   *   
   *   /|\- - TETRAGON
   *   @ - CIRCLE
   */
  @Test
  def testCircleReachesBodyOfTetragonDoIntersect = {
    val polygon = PolygonBuilder(Point(6, 0), Point(8, 4), Point(12, 4))
                  .to(Point(12, 0)).build;
    val circle = Circle(Point(5, 4), 3);
    Assert.assertTrue(polygon.intersects(circle));
  }
  
  /**
   *                  @@@@@
   *                @@@@@@@@@ __
   *               @@@@@@@@@@@  |
   *                @@@@@@@@@   |
   *                / @@@@@     |
   *               /____________|
   *   
   *   /|\- - TETRAGON
   *   @ - CIRCLE
   */
  @Test
  def testCircleCenterInsideOfTetragonDoIntersect = {
    val polygon = PolygonBuilder(Point(6, 0), Point(8, 4), Point(12, 4))
                  .to(Point(12, 0)).build;
    val circle = Circle(Point(9, 3.5), 3);
    Assert.assertTrue(polygon.intersects(circle));
  }
}