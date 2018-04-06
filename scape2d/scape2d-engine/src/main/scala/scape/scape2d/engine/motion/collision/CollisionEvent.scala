package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.util.Combination2

object CollisionEvent {
  def apply[T <: Movable](movable1:T, movable2:T, time:Double) = {
    new CollisionEvent(Combination2(movable1, movable2), time);
  }
}

case class CollisionEvent[T <: Movable](concurrentPair:Combination2[T, T], time:Double) {
  val snapshotPair = Combination2(concurrentPair._1.snapshot.asInstanceOf[T], concurrentPair._2.snapshot.asInstanceOf[T]);
}