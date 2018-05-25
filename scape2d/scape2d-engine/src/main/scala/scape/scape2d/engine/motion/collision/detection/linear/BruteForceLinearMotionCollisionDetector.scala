package scape.scape2d.engine.motion.collision.detection.linear

import scala.collection.Iterable
import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.time.Duration

case class BruteForceLinearMotionCollisionDetector[T <: Movable](
  val detectionStrategy:LinearMotionCollisionDetectionStrategy[T])
extends LinearMotionCollisionDetector[T] {  
  def detect(movables:Set[T], timestep:Duration) = {
    val combinations = Combination2.selectFrom(movables);
    combinations.flatMap(detect(_, timestep));
  }
  
  private def detect(movableCombination:Combination2[T, T], timestep:Duration) = {
    val detection = detectionStrategy.detect(movableCombination._1, movableCombination._2, timestep);
    detection.map(CollisionEvent(movableCombination, _));
  }
}