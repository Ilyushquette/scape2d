package scape.scape2d.engine.core

case class MotionEvent[T <: Movable](old:T, concurrent:T) {
  val snapshot = concurrent.snapshot.asInstanceOf[T];
}