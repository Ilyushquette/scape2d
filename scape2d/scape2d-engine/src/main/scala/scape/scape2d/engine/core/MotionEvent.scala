package scape.scape2d.engine.core

import scape.scape2d.engine.geom.shape.Shape

case class MotionEvent[T <: Movable[_ <: Shape]](old:T, concurrent:T) {
  val snapshot = concurrent.snapshot.asInstanceOf[T];
}