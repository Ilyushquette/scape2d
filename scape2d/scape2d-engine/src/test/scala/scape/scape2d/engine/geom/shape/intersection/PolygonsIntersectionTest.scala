package scape.scape2d.engine.geom.shape.intersection

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.PolygonBuilder

class PolygonsIntersectionTest {
  /**
   * 
   *     /\       
   *    /  \      /\
   *   |    |    /  \
   *   |    |   /____\
   *   |    |
   *    \  /
   *     \/
   *   
   *   /|\- - TRIANGLE AND HEXAGON
   */
  @Test
  def testTriangleTooFarFromHexagonDontIntersect = {
    val hexagon = PolygonBuilder(Point(0, 2), Point(0, 5), Point(2, 7))
                  .to(Point(4, 5)).to(Point(4, 2)).to(Point(2, 0)).build;
    val triangle = PolygonBuilder(Point(6, 3), Point(8, 6), Point(10, 3)).build;
    Assert.assertFalse(hexagon.intersects(triangle));
  }
  
  /**
   * 
   *     /\       
   *    /  \ /\
   *   |    |  \
   *   |   /|___\
   *   |    |
   *    \  /
   *     \/
   *   
   *   /|\- - TRIANGLE AND HEXAGON
   *  
   *  Note: Triangle intersects body of one of the sides of hexagon, not the vertex!
   */
  @Test
  def testTriangleReachesBodyOfHexagonDoIntersect = {
    val hexagon = PolygonBuilder(Point(0, 2), Point(0, 5), Point(2, 7))
                  .to(Point(4, 5)).to(Point(4, 2)).to(Point(2, 0)).build;
    val triangle = PolygonBuilder(Point(3.5, 3), Point(5.5, 6), Point(7.5, 3)).build;
    Assert.assertTrue(hexagon.intersects(triangle));
  }
  
  /**
   * 
   *     /\       
   *    /  \
   *   |    |
   *   | /\ |
   *   |    |
   *    \  /
   *     \/
   *   
   *   /|\- - TRIANGLE AND HEXAGON
   */
  @Test
  def testTriangleInsideOfHexagonDoIntersect = {
    val hexagon = PolygonBuilder(Point(0, 2), Point(0, 5), Point(2, 7))
                  .to(Point(4, 5)).to(Point(4, 2)).to(Point(2, 0)).build;
    val triangle = PolygonBuilder(Point(1, 3), Point(2, 4), Point(3, 3)).build;
    Assert.assertTrue(hexagon.intersects(triangle));
  }
}