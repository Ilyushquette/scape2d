package scape.scape2d.engine.motion.collision.detection.linear

import scape.scape2d.engine.core.Movable

trait LinearMotionCollisionDetectionStrategy[T <: Movable] {
  def detect(movable1:T, movable2:T, timestep:Double):Option[Double];
}