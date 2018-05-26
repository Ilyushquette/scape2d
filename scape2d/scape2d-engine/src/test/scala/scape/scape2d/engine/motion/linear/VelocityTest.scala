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
import scape.scape2d.engine.time.Hour
import scape.scape2d.engine.geom.Components

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
  
  @Test
  def testDisplacementVectorForTimeResolutionFromVelocity = {
    val velocity = Vector(18, 315(Degree)) / Hour;
    Assert.assertEquals(Vector(9, 315(Degree)), velocity.forTime(Duration(30, Minute)));
  }
  
  @Test
  def testVelocityAdditionToAnotherVelocity = {
    val velocity1 = Vector.from(Components(1, 1)) / Second;
    val velocity2 = Vector.from(Components(-10, 2)) / Duration(250, Millisecond);
    Assert.assertEquals(Vector.from(Components(-39, 9)) / Second, velocity1 + velocity2);
  }
  
  @Test
  def testVelocitySubtractionFromAnotherVelocity = {
    val velocity1 = Vector.from(Components(-3, 0)) / Minute;
    val velocity2 = Vector.from(Components(-1, 0)) / Duration(30, Second);
    Assert.assertEquals(Vector.from(Components(-1, 0)) / Minute, velocity1 - velocity2);
  }
}