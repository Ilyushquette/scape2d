package scape.scape2d.engine.process

import scape.scape2d.engine.time.Duration

trait Process {
  def integrate(timestep:Duration);
}