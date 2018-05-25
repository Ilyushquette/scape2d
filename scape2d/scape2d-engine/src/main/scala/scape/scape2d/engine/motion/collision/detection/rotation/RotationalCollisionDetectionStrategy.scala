package scape.scape2d.engine.motion.collision.detection.rotation

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.time.Duration

trait RotationalCollisionDetectionStrategy[T <: Movable] {
  def detect(movable1:T, movable2:T, timestep:Duration):Option[Duration];
}