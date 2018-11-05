package scape.scape2d.engine.motion.collision.detection.broad

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.geom.shape.FiniteShape

case class BruteForceDiscreteBroadPhaseCollisionDetectionStrategy[T <: Movable[_ <: FiniteShape]]()
extends DiscreteBroadPhaseCollisionDetectionStrategy[T] {
  def prune(movables:Set[T]) = Combination2.selectFrom(movables);
}