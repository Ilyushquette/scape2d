package scape.scape2d.engine.core.integral

import scape.scape2d.engine.core.matter.Particle
import scape.scape2d.engine.time.Duration

trait MotionIntegral {
  def integrate(particles:Set[Particle], timestep:Duration);
}