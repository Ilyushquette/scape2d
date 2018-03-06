package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent

trait CollisionDetector[T <: Movable] {
  def detect(movables:Set[T], timestep:Double):Set[CollisionEvent[T]];
  
  def detect(checkables:Set[T], others:Set[T], timestep:Double):Set[CollisionEvent[T]];
}