package scape.scape2d.engine.geom.shape

case class ShapeUnitConverter(multiplier:Double) {
  def scale[S <: Shape](shape:S):S = {
    val scaledShape = scaleShape(shape);
    scaledShape.asInstanceOf[S];
  }
  
  private def scaleShape(shape:Shape):Shape = shape match {
    case Point(x, y) => Point(x * multiplier, y * multiplier);
    case Segment(p1, p2) => Segment(scale(p1), scale(p2));
    case Circle(center, radius) => Circle(scale(center), radius * multiplier);
    case AxisAlignedRectangle(bottomLeft, width, height) =>
      AxisAlignedRectangle(scale(bottomLeft), width * multiplier, height * multiplier);
    case polygon:Polygon =>
      val scaledSegments = polygon.segments.map(scale(_));
      val scaledCenter = scale(polygon.center);
      CustomPolygon(scaledSegments, scaledCenter);
    case CircleSweep(circle, sweepVector) =>
      CircleSweep(scale(circle), sweepVector * multiplier);
    case Ring(circle, thickness) => Ring(scale(circle), thickness * multiplier);
    case _:Line | _:Ray =>
      throw new IllegalArgumentException("Couldn't scale line/ray due to infinite nature");
  }
}