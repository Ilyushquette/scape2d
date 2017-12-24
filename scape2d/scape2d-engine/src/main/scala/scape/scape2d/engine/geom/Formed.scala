package scape.scape2d.engine.geom

import scape.scape2d.engine.geom.shape.Shape

object Formed {
  implicit def exposeShape[T <: Shape](formed:Formed[T]) = formed.shape;
}

trait Formed[T <: Shape] {
  def shape:T;
}