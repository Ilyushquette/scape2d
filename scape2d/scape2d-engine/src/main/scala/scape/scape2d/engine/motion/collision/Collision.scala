package scape.scape2d.engine.motion.collision

import scape.scape2d.engine.core.Movable
import scape.scape2d.engine.geom.Formed

case class Collision[T <: Movable[T] with Formed[_]](concurrentPair:(T, T), time:Double) {
  val snapshotPair = (concurrentPair._1.snapshot, concurrentPair._2.snapshot);
}