package scape.scape2d.engine.mass.angular

import org.junit.Test
import org.junit.Assert

import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.mass.doubleToMass
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.motion.rotational.InstantAngularAcceleration
import scape.scape2d.engine.time.Second

class MomentOfInertiaTest {
  @Test
  def testMomentsOfInertiaNotEqual = {
    Assert.assertFalse(MomentOfInertia(1(Kilogram), 3) == MomentOfInertia(3(Kilogram), 1));
  }
  
  @Test
  def testMomentsOfInertiaEqual = {
    Assert.assertTrue(MomentOfInertia(25(Kilogram), 2) == MomentOfInertia(4(Kilogram), 5));
  }
  
  @Test
  def testInstantAngularAccelerationResolutionForTorqueFromMomentOfInertia = {
    val momentOfInertia = MomentOfInertia(5(Kilogram), 4);
    val torque = 40;
    val expectedAngularAcceleration = InstantAngularAcceleration(UnboundAngle(-0.5, Radian) / Second);
    Assert.assertEquals(expectedAngularAcceleration, momentOfInertia forTorque -40);
  }
}