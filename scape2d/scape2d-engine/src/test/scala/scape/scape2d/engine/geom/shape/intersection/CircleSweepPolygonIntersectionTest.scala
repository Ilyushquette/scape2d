package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.Components2D
import scape.scape2d.engine.geom.shape.PolygonBuilder
import org.junit.Assert

class CircleSweepPolygonIntersectionTest {
  /**
   *  
   *               /\
   *              /__\
   *         _______________
   *       @@               @@
   *     @@@@@@           @@@@@@
   *    @@@@@@@@         @@@@@@@@
   *     @@@@@@           @@@@@@
   *       @@_______________@@
   *       
   *   @/|\- CIRCLE SWEEP
   *   /|\ - TRIANGLE
   */
  @Test
  def testTriangleTooFarFromCircleSweepDontIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components2D(8.5, 0)));
    val polygon = PolygonBuilder(Point(3, 3), Point(4, 4), Point(5, 3)).build;
    Assert.assertFalse(circleSweep.intersects(polygon));
  }
  
  /**
   *         _______________
   *     /\@@               @@
   *    /__\@@@           @@@@@@
   *    @@@@@@@@         @@@@@@@@
   *     @@@@@@           @@@@@@
   *       @@_______________@@
   *       
   *   @/|\- CIRCLE SWEEP
   *   /|\ - TRIANGLE
   */
  @Test
  def testTriangleReachesOriginCircleOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components2D(8.5, 0)));
    val polygon = PolygonBuilder(Point(-2, 1), Point(-1, 2), Point(0, 1)).build;
    Assert.assertTrue(circleSweep.intersects(polygon));
  }
  
  /**
   *         ______/\_______
   *       @@     /__\      @@
   *     @@@@@@           @@@@@@
   *    @@@@@@@@         @@@@@@@@
   *     @@@@@@           @@@@@@
   *       @@_______________@@
   *       
   *   @/|\- CIRCLE SWEEP
   *   /|\ - TRIANGLE
   */
  @Test
  def testTriangleThroughRectangularConnectorOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components2D(8.5, 0)));
    val polygon = PolygonBuilder(Point(3, 1), Point(4, 2), Point(5, 1)).build;
    Assert.assertTrue(circleSweep.intersects(polygon));
  }
  
  /**
   *         _______________
   *       @@               @@
   *     @@@@@@    /\     @@@@@@
   *    @@@@@@@@  /__\   @@@@@@@@
   *     @@@@@@           @@@@@@
   *       @@_______________@@
   *       
   *   @/|\- CIRCLE SWEEP
   *   /|\ - TRIANGLE
   */
  @Test
  def testTriangleInsideOfRectangularConnectorOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components2D(8.5, 0)));
    val polygon = PolygonBuilder(Point(3, 0), Point(4, 1), Point(5, 0)).build;
    Assert.assertTrue(circleSweep.intersects(polygon));
  }
}