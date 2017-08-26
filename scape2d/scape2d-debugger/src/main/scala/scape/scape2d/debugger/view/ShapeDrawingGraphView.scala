package scape.scape2d.debugger.view

import scala.collection.immutable.NumericRange
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment

class ShapeDrawingGraphView(val shapeDrawer:ShapeDrawer)
extends GraphView {
  val axisColor = 0x00ff00; // GREEN
  val graphColor = 0xff0000; // RED
  shapeDrawer.draw(CustomizedShape(Segment(Point.origin, Point(0, 10)), axisColor, false));
  shapeDrawer.draw(CustomizedShape(Segment(Point.origin, Point(10, 0)), axisColor, false));
  
  def render(fx:Double => Double, range:NumericRange[Double]) = {
    val customizedPoints = for(x <- range) yield CustomizedShape(Point(x, fx(x)), graphColor, false);
    customizedPoints.foreach(shapeDrawer.draw);
  }
  
  def clear(fx:Double => Double, range:NumericRange[Double]) = {
    val points = for(x <- range) yield Point(x, fx(x));
    points.foreach(shapeDrawer.clear(_, false));
  }
}