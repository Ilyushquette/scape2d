package scape.scape2d.engine.geom.shape.intersection

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.PolygonBuilder
import scape.scape2d.engine.geom.shape.Ray

class PolygonRayIntersectionTest {
  /**
   *      /\  /|
   *     /  \/ |    O->
   *    /      |
   *   /_______|
   *   
   *   /|\- - PENTAGON
   *   O-> - RAY WITH THE DIRECTION
   */
  @Test
  def testRayInTheDirectionAwayFromPentagonDontIntersect = {
    val polygon = PolygonBuilder(Point(0, 1), Point(2, 4), Point(4, 2))
                  .to(Point(6, 4)).to(Point(6, 1)).build;
    val ray = Ray(Point(8, 3), 0);
    Assert.assertFalse(polygon.intersects(ray));
  }
  
  /**
   *      /\  /|
   *     /  \/ |    <-O
   *    /      |
   *   /_______|
   *   
   *   /|\- - PENTAGON
   *   O-> - RAY WITH THE DIRECTION
   */
  @Test
  def testRayInTheDirectionTowardsPentagonDoIntersect = {
    val polygon = PolygonBuilder(Point(0, 1), Point(2, 4), Point(4, 2))
                  .to(Point(6, 4)).to(Point(6, 1)).build;
    val ray = Ray(Point(8, 3), 180);
    Assert.assertTrue(polygon.intersects(ray));
  }
}