package scape.scape2d.engine.core.matter

import scape.scape2d.engine.core.TimeDependent
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.shape.FiniteShape

/**
 * Units:
 * <ul>
 *  <li>force - magnitude in Newtons</li>
 * </ul>
 */
case class ImpulseOntoRigidBody(
  rigidBody:RigidBody[_ >: Null <: FiniteShape],
  force:Vector,
  time:Duration
) extends TimeDependent {
  private[core] def integrate(timestep:Duration) = {
    val multiplier = timestep / time;
    rigidBody.exertForce(force * multiplier, rigidBody.position);
  }
}