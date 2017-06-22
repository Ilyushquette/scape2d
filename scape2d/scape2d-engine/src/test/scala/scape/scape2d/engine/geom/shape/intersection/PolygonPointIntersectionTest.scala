package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Polygon
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert
import scape.scape2d.engine.geom.shape.PolygonBuilder

class PolygonPointIntersectionTest {
  /**                                 
   *         __________
   *        /          \
   *       /            \
   *      /  ___________ \
   *     / /        O   \ \
   *    //                \\
   *   
   *   /|\- - HEXAGON
   *   O - POINT
   */
  @Test
  def testPointInCaveOutsideOfHexagonDontIntersect = {
    val polygon = PolygonBuilder(Point(0, 0), Point(5, 5), Point(10, 5))
                  .to(Point(15, 0)).to(Point(10, 3)).to(Point(5, 3)).build;
    val point = Point(8, 2);
    Assert.assertFalse(polygon.intersects(point));
  }
  
  /**                                 
   *         __________
   *        /          \
   *       /            \
   *      /  ___________ \
   *     / /            \O\
   *    //                \\
   *   
   *   /|\- - HEXAGON
   *   O - POINT
   */
  @Test
  def testPointInsideOfHexagonDoIntersect = {
    val polygon = PolygonBuilder(Point(0, 0), Point(5, 5), Point(10, 5))
                  .to(Point(15, 0)).to(Point(10, 3)).to(Point(5, 3)).build;
    val point = Point(12.5, 2);
    Assert.assertTrue(polygon.intersects(point));
  }
  
  /**                                 
   *         __________
   *        /          \
   *       /            \
   *      /  ___________ \
   *     / /            \ \
   *     O                \\
   *   
   *   /|\- - HEXAGON
   *   O - POINT
   */
  @Test
  def testPointOnTheVertexOfHexagonDoIntersect = {
    val polygon = PolygonBuilder(Point(0, 0), Point(5, 5), Point(10, 5))
                  .to(Point(15, 0)).to(Point(10, 3)).to(Point(5, 3)).build;
    val point = Point(0, 0);
    Assert.assertTrue(polygon.intersects(point));
  }
  
  /**                                 
   *         __________
   *        /          \
   *       /            \
   *      /  ___________ \
   *     / /            \ \       O
   *    //                \\
   *   
   *   /|\- - HEXAGON
   *   O - POINT
   */
  @Test
  def testPointOutsideOfHexagonDontIntersect = {
    val polygon = PolygonBuilder(Point(0, 0), Point(5, 5), Point(10, 5))
                  .to(Point(15, 0)).to(Point(10, 3)).to(Point(5, 3)).build;
    val point = Point(18, 2);
    Assert.assertFalse(polygon.intersects(point));
  }
}