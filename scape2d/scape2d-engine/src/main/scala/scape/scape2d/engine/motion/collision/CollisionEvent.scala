package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.core.Movable

case class CollisionEvent[T <: Movable](concurrentPair:(T, T), time:Double) {
  val snapshotPair = (concurrentPair._1.snapshot.asInstanceOf[T], concurrentPair._2.snapshot.asInstanceOf[T]);
  
  def contains(movable:T) = concurrentPair._1 == movable || concurrentPair._2 == movable;
}