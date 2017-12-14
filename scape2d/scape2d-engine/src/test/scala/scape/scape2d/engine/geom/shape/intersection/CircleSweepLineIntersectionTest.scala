package scape.scape2d.engine.geom.shape.intersection

import org.junit.Test
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.Components2D
import scape.scape2d.engine.geom.shape.Line
import org.junit.Assert

class CircleSweepLineIntersectionTest {
  /**
   *     @@
   *   @@@@@@
   *  @@@@@@@@
   *  |@@@@@@|
   *  |  @@  |
   *  |      |  O
   *  |      |  
   *  |  @@  |  
   *  |@@@@@@|  
   *  @@@@@@@@  
   *   @@@@@@   
   *     @@     O
   * 
   *  @/|\- - CIRCLE SWEEP
   *  O - POINTS OF LINE
   */
  @Test
  def testOutsideOfCircleSweepDontIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components2D(0, 7)));
    val line = Line(Point(3.5, -2), Point(3.5, 4));
    Assert.assertFalse(circleSweep.intersects(line));
  }
  
  /**
   *     @@
   *   @@@@@@
   *  @@@@@@@@
   *  |@@@@@@|
   *  |  @@  |
   *  |      |  
   *  |      |  
   *  |  @@  |  
   *  |@@@@@@|  
   *  @@@@@@@@  
   *   @O@@@@   
   *     @@     O
   * 
   *  @/|\- - CIRCLE SWEEP
   *  O - POINTS OF LINE
   */
  @Test
  def testLineThroughOriginalCircleOfSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components2D(0, 7)));
    val line = Line(Point(3.5, -2), Point(-1, -1));
    Assert.assertTrue(circleSweep.intersects(line));
  }
  
  /**
   *     @@
   *   @@@@@@
   *  @@@@@@@@
   *  |@@@@@@|
   *  |  @@  |
   *  |      |  O   O
   *  |      |  
   *  |  @@  |  
   *  |@@@@@@|  
   *  @@@@@@@@  
   *   @@@@@@   
   *     @@     
   * 
   *  @/|\- - CIRCLE SWEEP
   *  O - POINTS OF LINE
   */
  @Test
  def testLineThroughSegmentsOfCircleSweepDoIntersect = {
    val circleSweep = CircleSweep(Circle(Point(0, 0), 2), Vector.from(Components2D(0, 7)));
    val line = Line(Point(3.5, 4), Point(5.5, 4));
    Assert.assertTrue(circleSweep.intersects(line));
  }
}