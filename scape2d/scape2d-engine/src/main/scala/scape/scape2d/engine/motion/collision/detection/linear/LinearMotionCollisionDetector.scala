package scape.scape2d.engine.motion.collision.detection.linear

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent

trait LinearMotionCollisionDetector[T <: Movable] {
  def detect(movables:Set[T], timestep:Double):Set[CollisionEvent[T]];
}