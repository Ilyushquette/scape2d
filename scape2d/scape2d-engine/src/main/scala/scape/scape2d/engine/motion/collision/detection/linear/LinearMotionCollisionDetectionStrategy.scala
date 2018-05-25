package scape.scape2d.engine.motion.collision.detection.linear

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.time.Duration

trait LinearMotionCollisionDetectionStrategy[T <: Movable] {
  def detect(movable1:T, movable2:T, timestep:Duration):Option[Duration];
}