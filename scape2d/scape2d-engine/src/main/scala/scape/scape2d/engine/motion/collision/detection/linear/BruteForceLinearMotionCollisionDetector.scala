package scape.scape2d.engine.motion.collision.detection.linear

import scala.collection.Iterable

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.CollisionEvent

case class BruteForceLinearMotionCollisionDetector[T <: Movable](
  val detect:(T, T, Double) => Option[Double])
extends LinearMotionCollisionDetector[T] {  
  def detect(movables:Iterable[T], timestep:Double) = {
    val combinations = movables.toSeq.combinations(2);
    val detections = combinations.map(c => (c, detect(c(0), c(1), timestep)));
    detections.collect {
      case (Seq(a, b), Some(time)) => CollisionEvent((a, b), time);
    }
  }
}