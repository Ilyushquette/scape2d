package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.Components2D
import scape.scape2d.engine.geom.shape.Ray
import org.junit.Assert

class CircleSweepRayIntersectionTest {
  /**
   *       @@
   *     @@@@@@
   *    @@@@@@@@\
   *     @@@@@@   \
   *     \ @@       \
   *       \       @@ \
   *         \   @@@@@@
   *       O   \@@@@@@@@
   *      /      @@@@@@
   *               @@
   *  
   *  @/|\- - CIRCLE SWEEP
   *  <-O - RAY WITH DIRECTION
   */
  @Test
  def testRayInTheDirectionAwayFromCircleSweepDontIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 5), 2), Vector.from(Components2D(5, -5)));
    val ray = Ray(Point(0, 0), 225);
    Assert.assertFalse(circleSweep.intersects(ray));
  }
  
  /**
   *       @@
   *     @@@@@@
   *    @@@@@@@@\
   *     @@@@@@   \
   *     \ @@       \
   *       \       @@ \
   *         \   @@@@@@
   *           \@@@@@@@@
   *             @@@@@@     <-O
   *               @@
   *  
   *  @/|\- - CIRCLE SWEEP
   *  <-O - RAY WITH DIRECTION
   */
  @Test
  def testRayThroughDestinationCircleOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 5), 2), Vector.from(Components2D(5, -5)));
    val ray = Ray(Point(10, -1), 180);
    Assert.assertTrue(circleSweep.intersects(ray));
  }
  
  /**
   *       @@
   *     @@@@@@
   *    @@@@@@@@\
   *     @@@@@@   \
   *     \ @@   /   \
   *       \   O   @@ \
   *         \   @@@@@@
   *           \@@@@@@@@
   *             @@@@@@
   *               @@
   *  
   *  @/|\- - CIRCLE SWEEP
   *  <-O - RAY WITH DIRECTION
   */
  @Test
  def testRayInsideOfRectangularConnectorOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 5), 2), Vector.from(Components2D(5, -5)));
    val ray = Ray(Point(2, 2), 45);
    Assert.assertTrue(circleSweep.intersects(ray));
  }
}