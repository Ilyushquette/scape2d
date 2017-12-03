package scape.scape2d.engine.core.matter.shell

import scape.scape2d.engine.core.matter.BodyBuilder
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.structure.SequentialSegmentedStructure
import scape.scape2d.engine.geom.structure.HingedSegmentedStructure

case class RectangularBodyBuilder(
  bodyBuilder:BodyBuilder = BodyBuilder(),
  step:Double = 1.5) {
  def withBodyBuilder(bb:BodyBuilder) = copy(bodyBuilder = bb);
  
  def withStep(s:Double) = copy(step = s);
  
  def build(bounds:AxisAlignedRectangle) = {
    val center = Point(bounds.bottomLeft.x + bounds.width / 2, bounds.bottomLeft.y + bounds.height / 2);
    val boundingPoints = makeBoundingPoints(bounds);
    val rectangularStructure = SequentialSegmentedStructure.closed(boundingPoints) ++
                               HingedSegmentedStructure(center, boundingPoints);
    bodyBuilder.build(center, rectangularStructure);
  }
  
  private def makeBoundingPoints(bounds:AxisAlignedRectangle) = {
    val horizontalCount = (bounds.width / step).toInt;
    val verticalCount = (bounds.height / step).toInt;
    val topLine = for(i <- 0 to horizontalCount)
                  yield Point(bounds.topLeft.x + i * step, bounds.topLeft.y);
    val leftLine = for(i <- 0 to verticalCount)
                   yield Point(topLine.last.x, bounds.topRight.y - i * step);
    val bottomLine = for(i <- 0 to horizontalCount)
                     yield Point(leftLine.last.x - i * step, leftLine.last.y);
    val rightLine = for(i <- 0 to verticalCount - 1)
                    yield Point(bounds.bottomLeft.x, leftLine.last.y + i * step);
    val pointsSequence = topLine ++ leftLine ++ bottomLine ++ rightLine;
    pointsSequence.distinct.toList;
  }
}