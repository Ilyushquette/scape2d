package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.time.Duration

trait CollisionDetector[T <: Movable] {
  def detect(movables:Set[T], timestep:Duration):Set[CollisionEvent[T]];
  
  def detect(checkables:Set[T], others:Set[T], timestep:Duration):Set[CollisionEvent[T]];
}