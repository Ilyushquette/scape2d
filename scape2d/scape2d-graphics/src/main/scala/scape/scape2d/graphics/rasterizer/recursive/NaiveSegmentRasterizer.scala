package scape.scape2d.graphics.rasterizer.recursive

import java.lang.Math.abs

import scape.scape2d.engine.geom.shape.PointInteger
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.geom.shape.ShapeBounds
import scape.scape2d.graphics.rasterizer.Rasterizer

case class NaiveSegmentRasterizer() extends Rasterizer[Segment] {
  def rasterize(segment:Segment) = {
    if(segment.line.vertical || abs(segment.line.slope.get) > 1) rasterizeSteepLine(segment);
    else rasterizeGradualLine(segment);
  }
  
  private def rasterizeSteepLine(segment:Segment) = {
    val segmentBounds = ShapeBounds(segment).toInt;
    val verticalRange = segmentBounds.bottomLeft.y to segmentBounds.topLeft.y - 1;
    if(!segment.line.vertical)
      for(y <- verticalRange) yield PointInteger(segment.line.forY(y).toInt, y);
    else
      for(y <- verticalRange) yield PointInteger(segmentBounds.bottomLeft.x, y);
  }
  
  private def rasterizeGradualLine(segment:Segment) = {
    val segmentBounds = ShapeBounds(segment).toInt;
    val horizontalRange = segmentBounds.bottomLeft.x to segmentBounds.bottomRight.x - 1;
    if(!segment.line.horizontal)
      for(x <- horizontalRange) yield PointInteger(x, segment.line.forX(x).toInt);
    else
      for(x <- horizontalRange) yield PointInteger(x, segmentBounds.bottomLeft.y);
  }
}