package scape.scape2d.engine.motion.collision.detection.broad

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.geom.shape.FiniteShape

case class BruteForceContinuousBroadPhaseCollisionDetectionStrategy[T <: Movable[_ <: FiniteShape]]()
extends ContinuousBroadPhaseCollisionDetectionStrategy[T] {
  def prune(movables:Set[T], timestep:Duration) = Combination2.selectFrom(movables);
}