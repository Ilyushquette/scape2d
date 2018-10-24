package scape.scape2d.engine.core.matter

import scape.scape2d.engine.core.TimeDependent
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.angle.Angle

/**
 * Units:
 * <ul>
 *  <li>torque - magnitude in Newtons (pseudovector). Direction will always be perpendicular
 *  to radial vector from rotatable center to particle</li>
 * </ul>
 */
case class TorqueImpulse(
  val particle:Particle,
  val torque:Double,
  val time:Duration)
extends TimeDependent {
  private[core] def integrate(timestep:Duration) = {
    val torque = asTorquePerTimestep(timestep);
    particle.exertForce(directionVector * torque, true);
  }
  
  private def asTorquePerTimestep(timestep:Duration) = {
    val multiplier = timestep / time;
    torque * multiplier;
  }
  
  private def directionVector = {
    val radialDirection = particle.rotatable.get.center angleTo particle.position;
    Vector.unit(radialDirection + Angle.right);
  }
}