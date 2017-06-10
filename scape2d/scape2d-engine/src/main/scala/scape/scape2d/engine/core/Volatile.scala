package scape.scape2d.engine.core

trait Volatile[T <: Volatile[T]] {
  def snapshot:T;
}