package scape.scape2d.engine.core.matter

import scape.scape2d.engine.geom.Vector2D
import scape.scape2d.engine.core.TimeDependent

/**
 * Units:
 * <ul>
 *  <li>force - magnitude in Newtons, angle in degrees</li>
 *  <li>time - in milliseconds</li>
 * </ul>
 */
case class Impulse(particle:Particle, force:Vector2D, time:Double) extends TimeDependent {
  private var timeleft = time;
  
  private[core] def integrate(timestep:Double) = {
    if(timeleft > 0) {
      timeleft = timeleft - timestep;
      val calibratedTime = if(timeleft < 0) timestep + timeleft else timestep;
      val multiplier = calibratedTime / time;
      val calibratedForce = force * multiplier;
      particle.exertForce(calibratedForce, true);
      true;
    }else false;
  }
}