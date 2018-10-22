package scape.scape2d.engine.geom.shape.intersection

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Point
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
   *       /            O
   *      /  ___________ \
   *     / /            \ \
   *    //                \\
   *   
   *   /|\- - HEXAGON
   *   O - POINT
   */
  @Test
  def testPointOnTheEdgeOfHexagonDoIntersect = {
    val polygon = PolygonBuilder(Point(0, 0), Point(5, 5), Point(10, 5))
                  .to(Point(15, 0)).to(Point(10, 3)).to(Point(5, 3)).build;
    val point = Point(11, 4);
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
  
  /**
   *       /\       o
   *      /  \
   *     /    \
   *    /      \
   *    \      /
   *     \    /
   *      \  /
   *       \/
   *   
   *   /|\- - RHOMBUS
   *   o - POINT
   */
  @Test
  def testPointOutsideOfRhombusDontIntersect = {
    val polygon = PolygonBuilder(Point(0, 0), Point(4, 4), Point(8, 0))
                  .to(Point(4, -4)).build;
    val point = Point(12, 4);
    Assert.assertFalse(polygon intersects point);
  }
  
  /**
   *       /\
   *      /  \
   *     /    \
   *    /   o  \
   *    \      /
   *     \    /
   *      \  /
   *       \/
   *   
   *   /|\- - RHOMBUS
   *   o - POINT
   */
  @Test
  def testPointInTheMiddleOfRhombusDoIntersect = {
    val polygon = PolygonBuilder(Point(0, 0), Point(4, 4), Point(8, 0))
                  .to(Point(4, -4)).build;
    val point = Point(4, 0);
    Assert.assertTrue(polygon intersects point);
  }
}