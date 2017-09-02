package scape.scape2d.engine.geom.shape

object ShapeInteger {
  implicit def widenShape[S <: Shape](shapeInteger:ShapeInteger[S]):S = shapeInteger.shapeDouble;
}

sealed trait ShapeInteger[S <: Shape] {
  def shapeDouble:S;
}