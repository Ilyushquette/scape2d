package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.Combination2
import scape.scape2d.engine.geom.shape.Shape

object CollisionEvent {
  def apply[T <: Movable[_ <: Shape]](movable1:T, movable2:T, time:Duration) = {
    new CollisionEvent(Combination2(movable1, movable2), time);
  }
}

case class CollisionEvent[T <: Movable[_ <: Shape]](concurrentPair:Combination2[T, T], time:Duration) {
  val snapshotPair = Combination2(concurrentPair._1.snapshot.asInstanceOf[T], concurrentPair._2.snapshot.asInstanceOf[T]);
}