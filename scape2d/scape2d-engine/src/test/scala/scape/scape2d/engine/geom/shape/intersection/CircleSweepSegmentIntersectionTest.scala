package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.shape.Segment
import org.junit.Assert

class CircleSweepSegmentIntersectionTest {
  /**
   *         o
   *          \
   *           o
   *         ______________
   *       @@              @@
   *     @@@@@@          @@@@@@
   *    @@@@@@@@        @@@@@@@@
   *     @@@@@@          @@@@@@
   *       @@______________@@
   *       
   *   @/|\- CIRCLE SWEEP
   *   o--o - SEGMENT
   */
  @Test
  def testSegmentOutsideOfCircleSweepDontIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components(8, 0)));
    val segment = Segment(Point(1, 6), Point(2, 4));
    Assert.assertFalse(circleSweep.intersects(segment));
  }
  
  /**
   *         ______________
   *       @@              @@
   *     @@@@@@          @@@@@@
   *    @@@@@@@@        @@@@@@o@---o
   *     @@@@@@          @@@@@@
   *       @@______________@@
   *       
   *   @/|\- CIRCLE SWEEP
   *   o--o - SEGMENT
   */
  @Test
  def testSegmentEntersDestinationCircleOfCircleSweepDontIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components(8, 0)));
    val segment = Segment(Point(9, 0), Point(12, 0));
    Assert.assertTrue(circleSweep.intersects(segment));
  }
  
  /**
   *         
   *         o
   *           \
   *         ____\_________
   *       @@      \       @@
   *     @@@@@@      o   @@@@@@
   *    @@@@@@@@        @@@@@@@@
   *     @@@@@@          @@@@@@
   *       @@______________@@
   *       
   *   @/|\- CIRCLE SWEEP
   *   o--o - SEGMENT
   */
  @Test
  def testSegmentEntersRectangularConnectorOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components(8, 0)));
    val segment = Segment(Point(1, 6), Point(4.5, 1));
    Assert.assertTrue(circleSweep.intersects(segment));
  }
  
  /**
   *         
   *         o
   *          \
   *         __\___________
   *       @@   \          @@
   *     @@@@@@  \       @@@@@@
   *    @@@@@@@@  \     @@@@@@@@
   *     @@@@@@    \     @@@@@@
   *       @@_______\______@@
   *                 \
   *                  o
   *                 
   *                 
   *   @/|\- CIRCLE SWEEP
   *   o--o - SEGMENT
   */
  @Test
  def testSegmentSlicesRectangularConnectorOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components(8, 0)));
    val segment = Segment(Point(1, 6), Point(5.5, -4));
    Assert.assertTrue(circleSweep.intersects(segment));
  }
  
  /**
   *         ______________
   *       @@              @@
   *     @@@@@@     o    @@@@@@
   *    @@@@@@@@   /    @@@@@@@@
   *     @@@@@@   o      @@@@@@
   *       @@______________@@
   *       
   *   @/|\- CIRCLE SWEEP
   *   o--o - SEGMENT
   */
  @Test
  def testSegmentInsideOfRectangularConnectorOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components(8, 0)));
    val segment = Segment(Point(3, -1), Point(4, 1));
    Assert.assertTrue(circleSweep.intersects(segment));
  }
}