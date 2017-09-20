package scape.scape2d.graphics.rasterizer

import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.shape.PointInteger

trait Rasterizer[S <: Shape] {
  def rasterize(shape:S):Iterable[PointInteger];
}