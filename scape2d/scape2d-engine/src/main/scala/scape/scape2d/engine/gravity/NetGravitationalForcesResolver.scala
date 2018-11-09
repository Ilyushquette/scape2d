package scape.scape2d.engine.gravity

import scape.scape2d.engine.core.Matter
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.shape.FiniteShape

trait NetGravitationalForcesResolver {
  def resolve[T <: Matter[_ <: FiniteShape]](matters:Set[T], timestep:Duration):Map[T, Vector];
}