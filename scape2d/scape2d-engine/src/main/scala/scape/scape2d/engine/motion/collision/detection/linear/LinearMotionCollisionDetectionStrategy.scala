package scape.scape2d.engine.motion.collision.detection.linear

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.geom.shape.Shape

trait LinearMotionCollisionDetectionStrategy[T <: Movable[_ <: Shape]] {
  def detect(movable1:T, movable2:T, timestep:Duration):Option[Duration];
}