package scape.scape2d.engine.motion.collision.detection.broad

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.geom.shape.FiniteShape

trait ContinuousBroadPhaseCollisionDetectionStrategy[T <: Movable[_ <: FiniteShape]] {
  def prune(movables:Set[T], timestep:Duration):Set[Combination2[T, T]];
}