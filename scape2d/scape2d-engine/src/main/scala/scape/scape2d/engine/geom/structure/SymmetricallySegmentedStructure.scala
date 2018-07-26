package scape.scape2d.engine.geom.structure

import scape.scape2d.engine.geom.CounterClockwise
import scape.scape2d.engine.geom.TripletOrientation
import scape.scape2d.engine.geom.Vector.toComponents
import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment

case class SymmetricallySegmentedStructure(points:List[Point], lineOfSymmetry:Line)
extends SegmentedStructure {
  val segments = {
    val originalPoints = points.filter(TripletOrientation(lineOfSymmetry.p1, lineOfSymmetry.p2, _) == CounterClockwise);
    originalPoints.map(originalPoint => Segment(originalPoint, symmetricPointOf(originalPoint)));
  }
  
  private def symmetricPointOf(point:Point) = {
    val p1p = point - lineOfSymmetry.p1;
    val angleDiff = lineOfSymmetry.angle - p1p.angle;
    val p1sp = p1p.copy(angle = lineOfSymmetry.angle + angleDiff);
    val symmetricPoint = lineOfSymmetry.p1 displacedBy p1sp;
    if(points contains symmetricPoint) symmetricPoint;
    else throw new NoSuchElementException("There's no point symmetric to " + point + " in the structure");
  }
}