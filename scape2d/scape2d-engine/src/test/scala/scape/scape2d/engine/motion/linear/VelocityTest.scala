package scape.scape2d.engine.motion.linear

import org.junit.Test
import org.junit.Assert
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.time.Minute

class VelocityTest {
  @Test
  def testDifferentVelocities = {
    val velocity1 = Vector(3, Angle.zero) / Minute;
    val velocity2 = Vector(0.5, Angle.zero) / Duration(30, Second);
    Assert.assertFalse(velocity1 == velocity2);
  }
  
  @Test
  def testSameVelocities = {
    val velocity1 = Vector(10, 135(Degree)) / Second;
    val velocity2 = Vector(5, 135(Degree)) / Duration(500, Millisecond);
    Assert.assertTrue(velocity1 == velocity2);
  }
  
  @Test
  def testTimeForDistanceResolutionFromVelocity = {
    val velocity = Vector(7, 315(Degree)) / Second;
    Assert.assertEquals(Duration(2, Second), velocity.forDistance(14));
  }
}