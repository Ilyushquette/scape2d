package scape.scape2d.engine.motion.rotational

import java.lang.Math.PI

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.time.Hour
import scape.scape2d.engine.time.Millisecond
import scape.scape2d.engine.time.Minute
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

class AngularVelocityTest {
  @Test
  def testDifferentAngularVelocities = {
    Assert.assertFalse(10(Degree) / Second == UnboundAngle(-5, Degree) / Duration(500, Millisecond));
  }
  
  @Test
  def testSameAngularVelocities = {
    Assert.assertTrue(Angle.full / Hour == Angle.straight / Duration(30, Minute));
  }
  
  @Test
  def testTimeForAngleResolutionFromAngularVelocity = {
    val angularVelocity = 45(Degree) / Second;
    Assert.assertEquals(Duration(6, Second), angularVelocity.forAngle(270(Degree).unbound));
  }
  
  @Test
  def testAngleForTimeResolutionFromAngularVelocity = {
    val angularVelocity = Angle.right / Duration(100, Millisecond);
    Assert.assertEquals(UnboundAngle(900, Degree), angularVelocity.forTime(Second));
  }
  
  @Test
  def testAdditionOfAngularVelocity = {
    val result = 5(Degree) / Second + 2(Degree) / Duration(500, Millisecond);
    Assert.assertEquals(9(Degree) / Second, result);
  }
  
  @Test
  def testSubtractionOfAngularVelocity = {
    val result = Angle.straight / Minute - 3(Degree) / Second;
    Assert.assertEquals(Angle.zero / Second, result);
  }
  
  @Test
  def testAngularToLinearVelocity = {
    val angularVelocity = Angle.right / Second;
    val linearVelocity = angularVelocity.toLinearVelocity(Point.origin, Point(-3, 0));
    Assert.assertEquals(Vector(4.7123889803, 270(Degree)) / Second, linearVelocity);
  }
  
  @Test
  def testNegativeAngularToInvertedLinearVelocity = {
    val angularVelocity = UnboundAngle(-PI, Radian) / Second;
    val linearVelocity = angularVelocity.toLinearVelocity(Point.origin, Point(-10, 0));
    Assert.assertEquals(Vector(31.4159265358, 90(Degree)) / Second, linearVelocity);
  }
}