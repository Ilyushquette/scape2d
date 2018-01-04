package scape.scape2d.engine.core.matter

import scape.scape2d.engine.core.TimeDependent
import scape.scape2d.engine.geom.normalizeAngle
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.time.Duration

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
  private[core] def integrate(timestep:Double) = {
    val torque = asTorquePerTimestep(timestep);
    particle.exertForce(directionVector * torque, true);
  }
  
  private def asTorquePerTimestep(timestep:Double) = {
    val multiplier = timestep / time.milliseconds;
    torque * multiplier;
  }
  
  private def directionVector = {
    val radialDirection = particle.rotatable.get.center angleToDeg particle.position;
    Vector(1, normalizeAngle(radialDirection + 90));
  }
}