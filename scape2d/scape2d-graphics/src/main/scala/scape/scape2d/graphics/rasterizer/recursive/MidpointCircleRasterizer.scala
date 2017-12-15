package scape.scape2d.graphics.rasterizer.recursive

import java.lang.Math.sqrt

import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.graphics.rasterizer.Rasterizer

case class MidpointCircleRasterizer extends Rasterizer[Circle] {
  def rasterize(circle:Circle) = {
    val zerothOctantOffsets = calculateZerothOctantOffsets(circle, 0, Set());
    zerothOctantOffsets.flatMap(reflectOctants(_, circle));
  }
  
  private def calculateZerothOctantOffsets(circle:Circle, y:Double, offsets:Set[Components]):Set[Components] = {
    val xSquared = (circle.radius * circle.radius) - (y * y);
    val x = sqrt(xSquared);
    if(x > y) calculateZerothOctantOffsets(circle, y + 1, offsets) + Components(x, y);
    else offsets;
  }
  
  private def reflectOctants(offset:Components, circle:Circle) = {
    Set(Point(circle.center.x + offset.x, circle.center.y + offset.y).toInt,
        Point(circle.center.x + offset.x, circle.center.y - offset.y).toInt,
        Point(circle.center.x - offset.x, circle.center.y + offset.y).toInt,
        Point(circle.center.x - offset.x, circle.center.y - offset.y).toInt,
        Point(circle.center.x + offset.y, circle.center.y + offset.x).toInt,
        Point(circle.center.x + offset.y, circle.center.y - offset.x).toInt,
        Point(circle.center.x - offset.y, circle.center.y + offset.x).toInt,
        Point(circle.center.x - offset.y, circle.center.y - offset.x).toInt);
  }
}