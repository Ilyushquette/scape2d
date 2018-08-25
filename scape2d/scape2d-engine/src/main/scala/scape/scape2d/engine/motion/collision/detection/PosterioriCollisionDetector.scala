package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.geom.shape.Shape

trait PosterioriCollisionDetector[T <: Movable[_ <: Shape]] {
  def detect(movables:Set[T]):Set[CollisionEvent[T]];
}