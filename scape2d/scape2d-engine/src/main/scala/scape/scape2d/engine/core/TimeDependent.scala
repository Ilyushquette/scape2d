package scape.scape2d.engine.core

import scape.scape2d.engine.time.Duration

trait TimeDependent {
  def time:Duration;
  
  private[core] def integrate(timestep:Double);
}