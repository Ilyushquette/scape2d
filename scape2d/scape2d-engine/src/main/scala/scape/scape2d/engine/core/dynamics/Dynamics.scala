package scape.scape2d.engine.core.dynamics

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.process.Process

trait Dynamics extends Process {
  def movables:Set[_ <: Movable];
}