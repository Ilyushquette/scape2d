package scape.scape2d.graphics.rasterizer

import scape.scape2d.engine.geom.shape.PointInteger
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.shape.ShapeUnitConverter

case class UnitConvertingRasterizer[S <: Shape](
  converter:ShapeUnitConverter,
  rasterizer:Rasterizer[S])
extends Rasterizer[S] {
  def rasterize(shape:S) = {
    val scaledShape = converter.scale(shape);
    rasterizer.rasterize(scaledShape);
  }
}