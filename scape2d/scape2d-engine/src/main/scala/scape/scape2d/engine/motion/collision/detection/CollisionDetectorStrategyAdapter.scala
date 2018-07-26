package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.time.Duration

case class CollisionDetectorStrategyAdapter[T <: Movable](
  collisionDetector:CollisionDetector[T]
) extends CollisionDetectionStrategy[T] {
  def detect(movable1:T, movable2:T, timestep:Duration) = {
    collisionDetector.detect(Set(movable1, movable2), timestep).headOption.map(_.time);
  }
}