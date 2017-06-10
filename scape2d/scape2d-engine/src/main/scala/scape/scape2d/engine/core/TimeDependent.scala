package scape.scape2d.engine.core

trait TimeDependent {
  private[core] def integrate(timestep:Double):Boolean;
}