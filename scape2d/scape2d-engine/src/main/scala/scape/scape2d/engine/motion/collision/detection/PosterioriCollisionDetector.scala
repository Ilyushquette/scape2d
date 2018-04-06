package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent

trait PosterioriCollisionDetector[T <: Movable] {
  def detect(movables:Set[T]):Set[CollisionEvent[T]];
}