package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.time.Duration
import scape.scape2d.engine.util.Combination2

object RichCollisionEvent {
  def apply[T <: Movable[_ <: Shape]](movable1:T, movable2:T, contactContainer:ContactContainer) = {
    new RichCollisionEvent(Combination2(movable1, movable2), contactContainer);
  }
}

case class RichCollisionEvent[+T <: Movable[_ <: Shape]](concurrentPair:Combination2[T, T], contactContainer:ContactContainer) {
  val snapshotPair = Combination2(concurrentPair._1.snapshot.asInstanceOf[T], concurrentPair._2.snapshot.asInstanceOf[T]);
}