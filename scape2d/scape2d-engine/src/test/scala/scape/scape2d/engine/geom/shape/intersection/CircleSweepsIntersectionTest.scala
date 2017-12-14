package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.Components2D
import org.junit.Assert

class CircleSweepsIntersectionTest {
  /**
   *                OO
   *              OOOOOO
   *             OOOOOOOO
   *             \OOOOOO \
   *              \ OO    \
   *               \       \
   *                \       \
   *                 \       \
   *                  \    OO \
   *                   \ OOOOOO\
   *                    OOOOOOOO
   *                     OOOOOO
   *                       OO
   *         __________________________________________
   *       @@                                          @@
   *     @@@@@@                                      @@@@@@
   *    @@@@@@@@                                    @@@@@@@@
   *     @@@@@@                                      @@@@@@
   *       @@__________________________________________@@
   *       
   *    @ - CIRCLE SWEEP 1
   *    O - CIRCLE SWEEP 2
   */
  @Test
  def testSeparatedCircleSweepsDontIntersect = {
    val cs1 = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components2D(22, 0)));
    val cs2 = CircleSweep(Circle(Point(4.5, 14), 2), Vector.from(Components2D(3.5, -8)));
    Assert.assertFalse(cs1.intersects(cs2));
  }
  
  /**
   *                                                                      OO
   *                                                                    OOOOOO
   *                                                                  /OOOOOOOO
   *                                                                 /  OOOOOO/
   *                                                                /     OO /
   *                                                               /        /
   *                                                              /        /
   *                                                             /        /
   *                                                            /        /
   *                                                           /        /
   *                                                          /        /
   *                                                         /        /
   *         __________________________________________     /        /
   *       @@                                          @@  / OO     /
   *     @@@@@@                                      @@@@@@OOOOOO  /
   *    @@@@@@@@                                    @@@@@@@@OOOOOO/
   *     @@@@@@                                      @@@@@@OOOOOO
   *       @@__________________________________________@@    OO
   *       
   *    @ - CIRCLE SWEEP 1
   *    O - CIRCLE SWEEP 2
   */
  @Test
  def testCircleSweepReachesDestinationCircleOfCircleSweepDoIntersect = {
    val cs1 = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components2D(22, 0)));
    val cs2 = CircleSweep(Circle(Point(28.5, 14), 2), Vector.from(Components2D(-6.5, -14)));
    Assert.assertTrue(cs1.intersects(cs2));
  }
  
  /**
   *                OO
   *              OOOOOO
   *             OOOOOOOO
   *             \OOOOOO \
   *              \ OO    \
   *               \       \
   *                \       \
   *                 \       \
   *                  \       \
   *                   \       \
   *                    \       \
   *                     \    OO \
   *                      \ OOOOOO\
   *         ______________OOOOOOOO____________________
   *       @@               OOOOOO                     @@
   *     @@@@@@               OO                     @@@@@@
   *    @@@@@@@@                                    @@@@@@@@
   *     @@@@@@                                      @@@@@@
   *       @@__________________________________________@@
   *       
   *    @ - CIRCLE SWEEP 1
   *    O - CIRCLE SWEEP 2
   */
  @Test
  def testCircleSweepReachesRectangularConnectorOfCircleSweepDoIntersect = {
    val cs1 = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components2D(22, 0)));
    val cs2 = CircleSweep(Circle(Point(4.5, 14), 2), Vector.from(Components2D(5, -11)));
    Assert.assertTrue(cs1.intersects(cs2));
  }
  
  /**
   *         __________________________________________
   *       @@                   _______                @@
   *     @@@@@@                O       O             @@@@@@
   *    @@@@@@@@              OOO     OOO           @@@@@@@@
   *     @@@@@@                O_______O             @@@@@@
   *       @@__________________________________________@@
   *       
   *    @ - CIRCLE SWEEP 1
   *    O - CIRCLE SWEEP 2
   */
  @Test
  def testCircleSweepInsideOfAnotherCircleSweepDoIntersect = {
    val cs1 = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components2D(22, 0)));
    val cs2 = CircleSweep(Circle(Point(10, 0), 1), Vector.from(Components2D(4, 0)));
    Assert.assertTrue(cs1.intersects(cs2));
  }
}