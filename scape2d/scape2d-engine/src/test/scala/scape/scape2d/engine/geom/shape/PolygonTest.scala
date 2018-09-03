package scape.scape2d.engine.geom.shape

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.AreaEpsilon
import scape.scape2d.engine.geom.Components

class PolygonTest {
  @Test
  def testDisplacedByComponents = {
    val polygon = PolygonBuilder(Point(3, 3), Point(4, 4), Point(5, 3)).build;
    val components = Components(-3, -3);
    val expectedPolygon = PolygonBuilder(Point.origin, Point(1, 1), Point(2, 0)).build;
    Assert.assertEquals(expectedPolygon, polygon displacedBy components);
  }
  
  /**
   *              8
   *      <--------------->
   *   ∧  TTTTTTTTTTTTTTTTT
   *  2|  T               T
   *   ∨  TTTTTTT   TTTTTTT  ∧
   *            T   T        |
   *            T   T        |5
   *            T   T        |
   *            TTTTT        ∨
   *            
   *            <--->
   *              2
   */
  @Test
  def testAreaOfTShapedPolygon = {
    val polygon = PolygonBuilder(Point(-1, 0), Point(-1, 5), Point(-4, 5))
                  .to(Point(-4, 7)).to(Point(4, 7)).to(Point(4, 5))
                  .to(Point(1, 5)).to(Point(1, 0))
                  .build;
    Assert.assertEquals(26, polygon.area, AreaEpsilon);
  }
  
  /**
   *      /\
   *    /    \
   *   |      |
   *   |      |
   *   |      |
   *   |______|
   *  
   */
  @Test
  def testCentroidOfPentagon = {
    val polygon = PolygonBuilder(Point.origin, Point(0, 4), Point(2, 6))
                  .to(Point(4, 4)).to(Point(4, 0)).build;
    Assert.assertEquals(Point(2, 2.5333333333), polygon.center);
  }
  
  @Test
  def testCentroidOfRectangle = {
    val polygon = PolygonBuilder(Point(3, 3), Point(3, 8), Point(8, 8))
                  .to(Point(8, 3)).build;
    Assert.assertEquals(Point(5.5, 5.5), polygon.center);
  }
}