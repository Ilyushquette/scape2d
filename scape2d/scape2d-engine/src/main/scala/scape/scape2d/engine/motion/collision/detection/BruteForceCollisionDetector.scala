package scape.scape2d.engine.motion.collision.detection

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.util.Combination2

case class BruteForceCollisionDetector[T <: Movable](
  detectionStrategy:CollisionDetectionStrategy[T]
) extends CollisionDetector[T] {
  def detect(movables:Set[T], timestep:Double) = {
    val combinations = Combination2.selectFrom(movables);
    val detections = combinations.map(c => detect(c._1, c._2, timestep));
    detections.flatten;
  }
  
  def detect(checkables:Set[T], others:Set[T], timestep:Double) = {
    checkables.flatMap(detectOneToMany(_, others, timestep));
  }
  
  private def detectOneToMany(checkable:T, others:Set[T], timestep:Double) = {
    others.flatMap(detect(checkable, _, timestep));
  }
  
  private def detect(movable1:T, movable2:T, timestep:Double) = {
    val detection = detectionStrategy.detect(movable1, movable2, timestep);
    detection.map(CollisionEvent((movable1, movable2), _));
  }
}