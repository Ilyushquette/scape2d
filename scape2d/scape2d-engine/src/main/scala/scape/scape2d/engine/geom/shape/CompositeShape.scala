package scape.scape2d.engine.geom.shape

object CompositeShape {
  def apply(shapes:Shape*) = new CompositeShape(shapes.toList);
}

case class CompositeShape(shapes:List[Shape]);