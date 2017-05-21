package scape.scape2d.engine.core

trait TimeDependent {
  def integrate(timestep:Double);
}