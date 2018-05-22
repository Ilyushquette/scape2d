package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.Vector
import org.junit.Assert

class RingCircleSweepIntersectionTest {
  /**
   *                OO
   *              O    O
   *             O      O
   *              O    O
   *                OO
   *        
   *         ________________
   *       @@                @@
   *     @@@@@@            @@@@@@
   *    @@@@@@@@          @@@@@@@@
   *     @@@@@@            @@@@@@
   *       @@________________@@
   *       
   *   @/|\- CIRCLE SWEEP
   *   O - RING
   */
  @Test
  def testCircleSweepOutsideOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point(9, 14), 3.5), 1);
    val circleSweep = CircleSweep(Circle(Point.origin, 4), Vector(18, 0(Degree)));
    Assert.assertFalse(ring intersects circleSweep);
  }
  
  /**
   *           OO
   *         O __ O
   *        O @__@ O
   *         O    O
   *           OO
   *       
   *   @/|\- CIRCLE SWEEP
   *   O - RING
   */
  @Test
  def testCircleSweepInTheHoleOfTheRingDontIntersect = {
    val ring = Ring(Circle(Point.origin, 3.5), 1);
    val circleSweep = CircleSweep(Circle(Point(-2, 0), 0.5), Vector(4, 0(Degree)));
    Assert.assertFalse(ring intersects circleSweep);
  }
  
  /**
   *         ________________
   *       @@       OO       @@
   *     @@@@@@   O    O   @@@@@@
   *    @@@@@@@@ O      O @@@@@@@@
   *     @@@@@@   O    O   @@@@@@
   *       @@_______OO_______@@
   *       
   *   @/|\- CIRCLE SWEEP
   *   O - RING
   */
  @Test
  def testRingBetweenConnectorsOfTheCircleSweepDoIntersect = {
    val ring = Ring(Circle(Point(9, 0), 3.5), 1);
    val circleSweep = CircleSweep(Circle(Point.origin, 4), Vector(18, 0(Degree)));
    Assert.assertTrue(ring intersects circleSweep);
  }
  
  /**
   *         ________________
   *       @@                @@    OO
   *     @@@@@@            @@@@@@O    O
   *    @@@@@@@@          @@@@@@O@     O
   *     @@@@@@            @@@@@@O    O
   *       @@________________@@    OO
   *       
   *   @/|\- CIRCLE SWEEP
   *   O - RING
   */
  @Test
  def testRingOverlapsDestinationRingDoIntersect = {
    val ring = Ring(Circle(Point(24, 0), 3.5), 1);
    val circleSweep = CircleSweep(Circle(Point.origin, 4), Vector(18, 0(Degree)));
    Assert.assertTrue(ring intersects circleSweep);
  }
  
  /**
   *                OO
   *              O    O
   *         ____O______O____
   *       @@     O    O     @@
   *     @@@@@@     OO     @@@@@@
   *    @@@@@@@@          @@@@@@@@
   *     @@@@@@            @@@@@@
   *       @@________________@@
   *       
   *   @/|\- CIRCLE SWEEP
   *   O - RING
   */
  @Test
  def testRingOverlapsConnectorDoIntersect = {
    val ring = Ring(Circle(Point(9, 6), 3.5), 1);
    val circleSweep = CircleSweep(Circle(Point.origin, 4), Vector(18, 0(Degree)));
    Assert.assertTrue(ring intersects circleSweep);
  }
}