package scape.scape2d.engine.geom

import scape.scape2d.engine.geom.shape.Shape

trait Formed[T <: Shape] {
  def shape:T;
}