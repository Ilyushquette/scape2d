package scape.scape2d.engine.geom.shape

sealed trait Perimeter {
  def shape:Shape;
}

sealed trait FinitePerimeter extends Perimeter {
  def shape:FiniteShape;
  
  def length:Double;
}