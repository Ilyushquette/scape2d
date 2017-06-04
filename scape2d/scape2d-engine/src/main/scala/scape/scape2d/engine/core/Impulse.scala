package scape.scape2d.engine.core

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.geom.Vector2D

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
      particle.setForces(particle.forces :+ calibratedForce);
      true;
    }else false;
  }
}