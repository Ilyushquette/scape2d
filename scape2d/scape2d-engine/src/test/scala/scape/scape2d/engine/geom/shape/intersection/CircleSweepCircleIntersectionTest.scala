package scape.scape2d.engine.geom.shape.intersection

import scape.scape2d.engine.geom.shape.CircleSweep
import org.junit.Test
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.geom.Components2D
import org.junit.Assert

class CircleSweepCircleIntersectionTest {
  /**
   *     @@
   *   @@@@@@
   *  @@@@@@@@
   *  |@@@@@@|
   *  |  @@  |
   *  |      |
   *  |      |    O
   *  |      |   OOO
   *  |      |    O
   *  |      |
   *  |  @@  |  
   *  |@@@@@@|  
   *  @@@@@@@@  
   *   @@@@@@   
   *     @@
   * 
   *  @/|\- - CIRCLE SWEEP
   *  O - CIRCLE
   */
  @Test
  def testCircleTooFarFromCircleSweepDontIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector2D.from(Components2D(0, 10)));
    val circle = Circle(Point(4.5, 5), 1);
    Assert.assertFalse(circleSweep.intersects(circle));
  }
  
  /**
   * 
   *     O
   *    OOO
   *   @@O@@@
   *  @@@@@@@@
   *  |@@@@@@|
   *  |  @@  |
   *  |      |
   *  |      |
   *  |      |
   *  |      |
   *  |      |
   *  |  @@  |  
   *  |@@@@@@|  
   *  @@@@@@@@  
   *   @@@@@@   
   *     @@
   * 
   *  @/|\- - CIRCLE SWEEP
   *  O - CIRCLE
   */
  @Test
  def testCircleReachesDestinationCircleOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector2D.from(Components2D(0, 10)));
    val circle = Circle(Point(0, 12), 1);
    Assert.assertTrue(circleSweep.intersects(circle));
  }
  
  /**
   *     @@
   *   @@@@@@
   *  @@@@@@@@
   *  |@@@@@@|
   *  |  @@  |
   *  |      |
   *  |      |O
   *  |      OOO
   *  |      |O
   *  |      |
   *  |  @@  |  
   *  |@@@@@@|  
   *  @@@@@@@@  
   *   @@@@@@   
   *     @@
   * 
   *  @/|\- - CIRCLE SWEEP
   *  O - CIRCLE
   */
  @Test
  def testCircleThroughSideOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector2D.from(Components2D(0, 10)));
    val circle = Circle(Point(2.5, 5), 1);
    Assert.assertTrue(circleSweep.intersects(circle));
  }
  
  /**
   *     @@
   *   @@@@@@
   *  @@@@@@@@
   *  |@@@@@@|
   *  |  @@  |
   *  |      |
   *  |  O   |
   *  | OOO  |
   *  |  O   |
   *  |      |
   *  |  @@  |  
   *  |@@@@@@|  
   *  @@@@@@@@  
   *   @@@@@@   
   *     @@
   * 
   *  @/|\- - CIRCLE SWEEP
   *  O - CIRCLE
   */
  @Test
  def testCircleInsideOfRectangularConnectorOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector2D.from(Components2D(0, 10)));
    val circle = Circle(Point(0, 5), 1);
    Assert.assertTrue(circleSweep.intersects(circle));
  }
}