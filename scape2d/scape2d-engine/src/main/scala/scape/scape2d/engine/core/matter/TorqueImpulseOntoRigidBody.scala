package scape.scape2d.engine.core.matter

import scape.scape2d.engine.core.TimeDependent
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.time.Duration

case class TorqueImpulseOntoRigidBody[T >: Null <: FiniteShape](
  rigidBody:RigidBody[T],
  torque:Double,
  pointOfApplication:RigidBody[T] => Point,
  time:Duration
) extends TimeDependent {
  private[core] def integrate(timestep:Duration) = {
    val torque = asTorquePerTimestep(timestep);
    val pointOnBody = pointOfApplication(rigidBody);
    val force = directionVector(pointOnBody) * torque;
    rigidBody.exertForce(force, pointOnBody);
  }
  
  private def asTorquePerTimestep(timestep:Duration) = {
    val multiplier = timestep / time;
    torque * multiplier;
  }
  
  private def directionVector(pointOnBody:Point) = {
    val radialDirection = rigidBody.center angleTo pointOnBody;
    Vector(1, radialDirection + Angle.right);
  }
}