package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.geom.shape.PolygonBuilder
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import org.junit.Assert

class RingPolygonIntersectionTest {
  /**
   *       @@
   *     @    @
   *    @      @             /_\
   *     @    @
   *       @@
   *       
   *  @ - RING
   *  /|\ - TRIANGLE
   */
  @Test
  def testPolygonFullyOutsideOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 3.5), 1);
    val polygon = PolygonBuilder(Point(18, 0), Point(19, 1), Point(20, 0)).build;
    Assert.assertFalse(ring intersects polygon);
  }
  
  /**
   *       @@
   *     @    @
   *    @  /_\ @
   *     @    @
   *       @@
   *       
   *  @ - RING
   *  /|\ - TRIANGLE
   */
  @Test
  def testPolygonInTheHoleOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 3.5), 1);
    val polygon = PolygonBuilder(Point.origin, Point(1, 1), Point(2, 0)).build;
    Assert.assertFalse(ring intersects polygon);
  }
  
  /**
   *       @@
   *     @    @
   *    @      /_\
   *     @    @
   *       @@
   *       
   *  @ - RING
   *  /|\ - TRIANGLE
   */
  @Test
  def testPolygonOverlapsWithOuterCircleOfTheRingDoIntersect = {
    val ring = Ring(Circle(Point.origin, 3.5), 1);
    val polygon = PolygonBuilder(Point(3, 0), Point(4, 1), Point(5, 0)).build;
    Assert.assertTrue(ring intersects polygon);
  }
  
  /**
   *            /\
   *           /  \
   *          /    \
   *         /      \
   *        /   @@   \
   *       /  @    @  \
   *      /  @      @  \
   *     /    @    @    \
   *    /       @@       \
   *   /__________________\
   * 
   *  @ - RING
   *  /|\ - TRIANGLE
   */
  @Test
  def testRingInsideThePolygonDoIntersect = {
    val ring = Ring(Circle(Point.origin, 3.5), 1);
    val polygon = PolygonBuilder(Point(-10, -6), Point(0, 12), Point(10, -6)).build;
    Assert.assertTrue(ring intersects polygon);
  }
}