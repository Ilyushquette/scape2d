package scape.scape2d.engine.motion.collision.detection.linear

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.time.Duration

trait LinearMotionCollisionDetector[T <: Movable] {
  def detect(movables:Set[T], timestep:Duration):Set[CollisionEvent[T]];
}