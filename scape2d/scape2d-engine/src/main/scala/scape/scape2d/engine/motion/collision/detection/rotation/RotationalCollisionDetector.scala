package scape.scape2d.engine.motion.collision.detection.rotation

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent

trait RotationalCollisionDetector[T <: Movable] {
  def detect(movables:Set[T], timestep:Double):Set[CollisionEvent[T]];
}