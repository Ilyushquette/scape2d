package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.shape.Shape

trait CollisionDetector[T <: Movable[_ <: Shape]] {
  def detect(movables:Set[T], timestep:Duration):Set[CollisionEvent[T]];
  
  def detect(checkables:Set[T], others:Set[T], timestep:Duration):Set[CollisionEvent[T]];
}