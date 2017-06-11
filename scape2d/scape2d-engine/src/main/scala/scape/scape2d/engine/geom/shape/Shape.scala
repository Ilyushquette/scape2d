package scape.scape2d.engine.geom.shape

sealed trait Shape {
  def intersects(shape:Shape):Boolean;
}