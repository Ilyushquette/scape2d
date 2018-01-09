package scape.scape2d.engine.motion.collision.detection.rotation

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent

trait RotationalCollisionDetector[T <: Movable] {
  def detect(movables:Iterable[T], timestep:Double):Iterator[CollisionEvent[T]];
}