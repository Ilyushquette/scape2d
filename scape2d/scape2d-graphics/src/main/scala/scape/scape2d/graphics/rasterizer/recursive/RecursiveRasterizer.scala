package scape.scape2d.graphics.rasterizer.recursive

import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Polygon
import scape.scape2d.engine.geom.shape.Ray
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.graphics.rasterizer.Rasterizer

case class RecursiveRasterizer(
  segmentRasterizer:Rasterizer[Segment] = NaiveSegmentRasterizer(),
  circleRasterizer:Rasterizer[Circle] = MidpointCircleRasterizer())
extends Rasterizer[Shape] {
  def rasterize(shape:Shape) = shape match {
    case point:Point => Set(point.toInt);
    case segment:Segment => segmentRasterizer.rasterize(segment);
    case circle:Circle => circleRasterizer.rasterize(circle);
    case polygon:Polygon => polygon.segments.flatMap(rasterize);
    case circleSweep:CircleSweep =>
      val parts = Set(circleSweep.circle,
                      circleSweep.connector._1,
                      circleSweep.connector._2,
                      circleSweep.destinationCircle);
      parts.flatMap(rasterize);
    case ring:Ring =>
      val parts = Set(ring.outerCircle, ring.innerCircle);
      parts.flatMap(rasterize);
    case _:Line | _:Ray => throw new IllegalArgumentException("Unable to rasterize infinite shape");
  }
}