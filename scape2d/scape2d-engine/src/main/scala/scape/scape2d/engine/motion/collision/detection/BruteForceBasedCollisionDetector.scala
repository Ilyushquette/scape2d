package scape.scape2d.engine.motion.collision.detection

import scala.collection.Iterable

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.motion.collision.Collision

class BruteForceBasedCollisionDetector[T <: Movable[T]](
  val detect:(T, T, Double) => Option[Double])
extends CollisionDetector[T] {  
  def detect(movables:Iterable[T], timestep:Double) = {
    val combinations = movables.toSeq.combinations(2);
    val detections = combinations.map(c => (c, detect(c(0), c(1), timestep)));
    detections.collect {
      case (Seq(a, b), Some(time)) => Collision((a, b), time);
    }
  }
}