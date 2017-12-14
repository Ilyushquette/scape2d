package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.Components2D
import org.junit.Assert

class CircleSweepPointIntersectionTest {
  /**
   *             @@
   *           @@@@@@
   *         /@@@@@@@@
   *     O /   @@@@@@
   *     /       @@
   *   / @@       /
   *   @@@@@@   /
   *  @@@@@@@@/
   *   @@@@@@
   *     @@
   *  
   *  @/|\- - CIRCLE SWEEP
   *  O - POINT
   */
  @Test
  def testPointOutsideOfCircleSweepTwoRayIntersectionsDontIntersect = {
    val circleSweep = CircleSweep(Circle(Point(5, 5), 2), Vector.from(Components2D(-5, -5)));
    Assert.assertFalse(circleSweep.intersects(Point(0, 4)));
  }
  
  /**
   *             @@
   *           @@@@@@
   *         /@@@@@@@@
   *       /   @@@@@@
   *     /       @@
   *   / @@       /
   *   @@@@@@   /
   *  @@@@@@@@/  O
   *   @@@@@@
   *     @@
   *  
   *  @/|\- - CIRCLE SWEEP
   *  O - POINT
   */
  @Test
  def testPointOutsideOfCircleSweepNoRayIntersectionsDontIntersect = {
    val circleSweep = CircleSweep(Circle(Point(5, 5), 2), Vector.from(Components2D(-5, -5)));
    Assert.assertFalse(circleSweep.intersects(Point(4, 0)));
  }
  
  /**
   *             @@
   *           @@@@@@
   *         /@@@@@@@@
   *       /   @@@@@@
   *     /       @@
   *   / @@  O    /
   *   @@@@@@   /
   *  @@@@@@@@/
   *   @@@@@@
   *     @@
   *  
   *  @/|\- - CIRCLE SWEEP
   *  O - POINT
   */
  @Test
  def testPointInsideOfRectangularConnectorOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(5, 5), 2), Vector.from(Components2D(-5, -5)));
    Assert.assertTrue(circleSweep.intersects(Point(2, 2)));
  }
  
  /**
   *             @@
   *           @@@@@@
   *         /@@@@@@@@
   *       /   @@@@@@
   *     /       @@
   *   / @@       /
   *   @@@@@@   /
   *  @@@@@@@@/
   *   @O@@@@
   *     @@
   *  
   *  @/|\- - CIRCLE SWEEP
   *  O - POINT
   */
  @Test
  def testPointInsideOfDestinationCircleOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(5, 5), 2), Vector.from(Components2D(-5, -5)));
    Assert.assertTrue(circleSweep.intersects(Point(-1, -1)));
  }
}