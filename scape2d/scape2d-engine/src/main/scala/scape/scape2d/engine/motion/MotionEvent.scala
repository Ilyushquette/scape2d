package scape.scape2d.engine.motion

import scape.scape2d.engine.core.Movable

case class MotionEvent[T <: Movable](old:T, concurrent:T) {
  val snapshot = concurrent.snapshot.asInstanceOf[T];
}