package scape.scape2d.engine.core.matter

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.motion.linear.InstantAcceleration
import scape.scape2d.engine.motion.linear.Velocity
import scape.scape2d.engine.motion.rotational.AngularVelocity
import scape.scape2d.engine.motion.rotational.InstantAngularAcceleration
import scape.scape2d.engine.time.Second
import scape.scape2d.engine.time.TimeUnit.toDuration

class RigidBodyTest {
  @Test
  def testAccelerationResolutionForForceOntoRigidBody = {
    val rigidBody = RigidBodyBuilder(AxisAlignedRectangle(Point.origin, 10, 10))
                    .withMass(Mass(10, Kilogram))
                    .build();
    val acceleration = rigidBody.accelerationForForce(Vector.from(Components(-2, 2)), Point(10, 5));
    Assert.assertEquals(InstantAcceleration(Vector(0.2, Angle.straight) / Second), acceleration);
  }
  
  @Test
  def testZeroAccelerationResolutionForForcePerpendicularToLeverArmOntoRigidBody = {
    val rigidBody = RigidBodyBuilder(AxisAlignedRectangle(Point.origin, 3, 3))
                    .withMass(Mass(9, Kilogram))
                    .build();
    val acceleration = rigidBody.accelerationForForce(Vector(5, 270(Degree)), Point(0, 1.5));
    Assert.assertEquals(InstantAcceleration(Velocity.zero), acceleration);
  }
  
  @Test
  def testAccelerationResolutionForForceOntoTheCenterOfRigidBody = {
    val rigidBody = RigidBodyBuilder(Circle(Point(3, 3), 3))
                    .withMass(Mass(5, Kilogram))
                    .build();
    val acceleration = rigidBody.accelerationForForce(Vector(5, Angle.straight), Point(3, 3));
    Assert.assertEquals(InstantAcceleration(Vector(1, Angle.straight) / Second), acceleration);
  }
  
  @Test
  def testAngularAccelerationResolutionForForceOntoRigidBody = {
    val rigidBody = RigidBodyBuilder(AxisAlignedRectangle(Point.origin, 5, 5))
                    .withMass(Mass(3, Kilogram))
                    .build();
    val angularAcceleration = rigidBody.angularAccelerationForForce(Vector(2, Angle.right), Point.origin);
    Assert.assertEquals(InstantAngularAcceleration(UnboundAngle(-0.4, Radian) / Second), angularAcceleration);
  }
  
  @Test
  def testZeroAngularAccelerationResolutionForForceAlongLeverArmOntoRigidBody = {
    val rigidBody = RigidBodyBuilder(Circle(Point(5, 5), 5))
                    .withMass(Mass(5, Kilogram))
                    .build();
    val angularAcceleration = rigidBody.angularAccelerationForForce(Vector(2, 270(Degree)), Point(5, 10));
    Assert.assertEquals(InstantAngularAcceleration(AngularVelocity.zero), angularAcceleration);
  }
  
  @Test
  def testZeroAngularAccelerationResolutionForForceOntoTheCenterOfRigidBody = {
    val rigidBody = RigidBodyBuilder(AxisAlignedRectangle(Point.origin, 10, 10))
                    .withMass(Mass(5, Kilogram))
                    .build();
    val angularAcceleration = rigidBody.angularAccelerationForForce(Vector(5, Angle.zero), Point(5, 5));
    Assert.assertEquals(InstantAngularAcceleration(AngularVelocity.zero), angularAcceleration);
  }
}