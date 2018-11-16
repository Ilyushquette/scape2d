package scape.scape2d.engine.core.matter

import org.junit.Assert
import org.junit.Test

import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.AngleUnit.toAngle
import scape.scape2d.engine.geom.angle.Degree
import scape.scape2d.engine.geom.angle.doubleToAngle
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.mass.Kilogram
import scape.scape2d.engine.mass.Mass
import scape.scape2d.engine.motion.linear.InstantAcceleration
import scape.scape2d.engine.motion.linear.Velocity
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
}