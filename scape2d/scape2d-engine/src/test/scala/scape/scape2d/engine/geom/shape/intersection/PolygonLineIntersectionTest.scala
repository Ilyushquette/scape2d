package scape.scape2d.engine.geom.shape.intersection

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.PolygonBuilder

class PolygonLineIntersectionTest {
  /**      ______
   *      /      \
   *     /        \
   *    /          \
   *   |            |
   *   |            |
   *   |            |
   *    \          /   O
   *     \        /        
   *      \______/
   *   
   *   
   *                O
   *   
   *   /|\- - OCTAGON
   *   O - POINTS OF LINE
   */
  @Test
  def testOctagonLineDontIntersect = {
    val polygon = PolygonBuilder(Point(0, 3), Point(3, 0), Point(6, 0))
                  .to(Point(9, 3)).to(Point(9, 6)).to(Point(6, 9)).to(Point(3, 9)).to(Point(0, 6))
                  .build;
    val line = Line(Point(9, -3), Point(10.5, 3));
    Assert.assertFalse(polygon.intersects(line));
  }
  
  /**      ______
   *      /      \
   *     /        \
   *    /          \
   *   |            |
   *   |            |
   *   |            |
   *    \          /
   *     \        /        
   *      \______/
   *   
   *               O
   *                O
   *   
   *   /|\- - OCTAGON
   *   O - POINTS OF LINE
   */
  @Test
  def testOctagonLineDoIntersect = {
    val polygon = PolygonBuilder(Point(0, 3), Point(3, 0), Point(6, 0))
                  .to(Point(9, 3)).to(Point(9, 6)).to(Point(6, 9)).to(Point(3, 9)).to(Point(0, 6))
                  .build;
    val line = Line(Point(8, -2), Point(9, -3));
    Assert.assertTrue(polygon.intersects(line));
  }
  
  /**      ______
   *      /      \
   *     /        \
   *    /          \
   *   |            |
   *   |            |
   *   |            |
   *    \          /
   *     \        /        
   *      O______/
   * 
   * 
   *                 O
   *   
   *   /|\- - OCTAGON
   *   O - POINTS OF LINE
   */
  @Test
  def testLineTouchesVertexOfOctagonDoIntersect = {
    val polygon = PolygonBuilder(Point(0, 3), Point(3, 0), Point(6, 0))
                  .to(Point(9, 3)).to(Point(9, 6)).to(Point(6, 9)).to(Point(3, 9)).to(Point(0, 6))
                  .build;
    val line = Line(Point(3, 0), Point(9, -3));
    Assert.assertTrue(polygon.intersects(line));
  }
}