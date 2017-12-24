package scape.scape2d.engine.core.matter

import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.core.TimeDependent
import scape.scape2d.engine.time.Duration

/**
 * Units:
 * <ul>
 *  <li>force - magnitude in Newtons, angle in degrees</li>
 * </ul>
 */
case class Impulse(
  particle:Particle,
  force:Vector,
  time:Duration)
extends TimeDependent {
  private[core] def integrate(timestep:Double) = {
    val multiplier = timestep / time.milliseconds;
    val calibratedForce = force * multiplier;
    particle.exertForce(calibratedForce, true);
  }
}