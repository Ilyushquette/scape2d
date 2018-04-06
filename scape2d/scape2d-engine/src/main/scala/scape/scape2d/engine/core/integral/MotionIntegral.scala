package scape.scape2d.engine.core.integral

import scape.scape2d.engine.core.matter.Particle

trait MotionIntegral {
  def integrate(particles:Set[Particle], timestep:Double);
}