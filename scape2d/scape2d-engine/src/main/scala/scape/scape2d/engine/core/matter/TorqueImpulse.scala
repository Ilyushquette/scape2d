package scape.scape2d.engine.core.matter

import scape.scape2d.engine.core.TimeDependent
import scape.scape2d.engine.geom.normalizeAngle
import scape.scape2d.engine.geom.Vector

/**
 * Units:
 * <ul>
 *  <li>torque - magnitude in Newtons (pseudovector). Direction will always be perpendicular
 *  to radial vector from rotatable center to particle</li>
 *  <li>time - in milliseconds</li>
 * </ul>
 */
class TorqueImpulse(
  val particle:Particle,
  val torque:Double,
  val time:Double)
extends TimeDependent {
  private var timeleft = time;
  
  private[core] def integrate(timestep:Double) = {
    if(timeleft > 0) {
      timeleft -= timestep;
      val truncatedTimestep = if(timeleft < 0) timestep + timeleft else timestep;
      val torque = asTorquePerTimestep(truncatedTimestep);
      particle.exertForce(directionVector * torque, true);
      true;
    }else false;
  }
  
  private def asTorquePerTimestep(timestep:Double) = {
    val multiplier = timestep / time;
    torque * multiplier;
  }
  
  private def directionVector = {
    val radialDirection = particle.rotatable.get.center angleTo particle.shape.center;
    Vector(1, normalizeAngle(radialDirection + 90));
  }
}