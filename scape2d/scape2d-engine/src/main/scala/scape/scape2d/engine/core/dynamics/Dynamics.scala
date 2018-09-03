package scape.scape2d.engine.core.dynamics

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.process.Process
import scape.scape2d.engine.geom.shape.Shape

trait Dynamics extends Process {
  def movables:Set[_ <: Movable[_ <: Shape]];
}