package scape.scape2d.engine.geom.shape

import java.lang.Math.max
import java.lang.Math.min

object ShapeBounds {
  def apply(shape:Shape):AxisAlignedRectangle = shape match {
    case Point(x, y) =>
      createBounds(x, x, y, y);
    case Segment(p1, p2) =>
      createBounds(min(p1.x, p2.x), max(p1.x, p2.x), min(p1.y, p2.y), max(p1.y, p2.y));
    case Circle(center, radius) =>
      createBounds(center.x - radius, center.x + radius, center.y - radius, center.y + radius);
    case aabb:AxisAlignedRectangle => aabb;
    case polygon:Polygon =>
      val xs = polygon.points.map(_.x);
      val ys = polygon.points.map(_.y);
      createBounds(xs.min, xs.max, ys.min, ys.max);
    case circleSweep:CircleSweep =>
      val originBounds = ShapeBounds(circleSweep.circle);
      val destinationBounds = ShapeBounds(circleSweep.destinationCircle);
      createBounds(min(originBounds.bottomLeft.x, destinationBounds.bottomLeft.x),
                   max(originBounds.bottomRight.x, destinationBounds.bottomRight.x),
                   min(originBounds.bottomLeft.y, destinationBounds.bottomLeft.y),
                   max(originBounds.topLeft.y, destinationBounds.topLeft.y));
    case _:Line | _:Ray => throw new IllegalArgumentException("Infinite shapes unlimited");
  }
  
  private def createBounds(minX:Double, maxX:Double, minY:Double, maxY:Double) = {
    AxisAlignedRectangle(Point(minX, minY), maxX - minX, maxY - minY);
  }
}