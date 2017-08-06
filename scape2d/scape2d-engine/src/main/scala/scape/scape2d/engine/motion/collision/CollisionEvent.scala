package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.core.Movable

case class CollisionEvent[T <: Movable[T]](concurrentPair:(T, T), time:Double) {
  val snapshotPair = (concurrentPair._1.snapshot, concurrentPair._2.snapshot);
}