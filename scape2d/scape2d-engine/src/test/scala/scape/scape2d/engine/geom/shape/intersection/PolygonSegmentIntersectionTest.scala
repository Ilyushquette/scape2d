package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert
import scape.scape2d.engine.geom.shape.PolygonBuilder

class PolygonSegmentIntersectionTest {
  /**
   *      /\oo/|
   *     /  \/ |
   *    /      |
   *   /_______|
   *   
   *   /|\- - PENTAGON
   *   o--o - SEGMENT
   */
  @Test
  def testSegmentInTheCaveOfPentagonDontIntersect = {
    val polygon = PolygonBuilder(Point(0, 1), Point(2, 4), Point(4, 2))
                  .to(Point(6, 4)).to(Point(6, 1)).build;
    val segment = Segment(Point(3, 3.5), Point(5, 3.5));
    Assert.assertFalse(polygon.intersects(segment));
  }
  
  /**
   *      /\o-/|--o
   *     /  \/ |
   *    /      |
   *   /_______|
   *   
   *   /|\- - PENTAGON
   *   o--o - SEGMENT
   */
  @Test
  def testSegmentThroughCaveOfPentagonDoIntersect = {
    val polygon = PolygonBuilder(Point(0, 1), Point(2, 4), Point(4, 2))
                  .to(Point(6, 4)).to(Point(6, 1)).build;
    val segment = Segment(Point(3, 3.5), Point(8, 3.5));
    Assert.assertTrue(polygon.intersects(segment));
  }
  
  /**
   *      /\  /|
   *     /  \/o|
   *    /    / |
   *   /____o__|
   *   
   *   # - PENTAGON
   *   o--o - SEGMENT
   */
  @Test
  def testSegmentInsideOfPentagonDoIntersect = {
    val polygon = PolygonBuilder(Point(0, 1), Point(2, 4), Point(4, 2))
                  .to(Point(6, 4)).to(Point(6, 1)).build;
    val segment = Segment(Point(4.5, 1), Point(5.5, 3));
    Assert.assertTrue(polygon.intersects(segment));
  }
}