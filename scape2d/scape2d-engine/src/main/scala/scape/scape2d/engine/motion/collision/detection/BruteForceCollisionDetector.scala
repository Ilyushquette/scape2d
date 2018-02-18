package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent

case class BruteForceCollisionDetector[T <: Movable](
  detectionStrategy:CollisionDetectionStrategy[T]
) extends CollisionDetector[T] {
  def detect(movables:Iterable[T], timestep:Double) = {
    val combinations = movables.toSeq.combinations(2);
    val collisions = combinations.map(c => detect(c(0), c(1), timestep));
    collisions.flatten;
  }
  
  private def detect(movable1:T, movable2:T, timestep:Double) = {
    val detection = detectionStrategy.detect(movable1, movable2, timestep);
    detection.map(CollisionEvent((movable1, movable2), _));
  }
}