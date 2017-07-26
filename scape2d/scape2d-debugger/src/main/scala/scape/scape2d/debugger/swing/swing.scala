package scape.scape2d.debugger

import java.awt.{Shape => SwingShape}
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.geom.Path2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Polygon
import scape.scape2d.engine.geom.shape.Segment
import scape.scape2d.engine.geom.shape.Shape

package object swing {
  def map(shape:Shape, pixelate:Double => Double):SwingShape = shape match {
    case point:Point => new Line2D.Double(mapPoint(point, pixelate), mapPoint(point, pixelate));
    case Segment(p1, p2) => // Swing is not consistent about drawing same segment but with swapped points.
      if(p1.distanceTo(Point.origin) <= p2.distanceTo(Point.origin))
        new Line2D.Double(mapPoint(p1, pixelate), mapPoint(p2, pixelate));
      else
        new Line2D.Double(mapPoint(p2, pixelate), mapPoint(p1, pixelate));
    case Circle(Point(cx, cy), radius) =>
      val r = pixelate(radius);
      new Ellipse2D.Double(pixelate(cx).toInt - r, pixelate(cy).toInt - r, r * 2, r * 2);
    case AxisAlignedRectangle(bottomLeft, width, height) =>
      new Rectangle2D.Double(pixelate(bottomLeft.x), pixelate(bottomLeft.y),
                             pixelate(width), pixelate(height));
    case polygon:Polygon =>
      val path = new Path2D.Double;
      path.moveTo(pixelate(polygon.points.first.x), pixelate(polygon.points.first.y));
      polygon.points.foreach(point => path.lineTo(point.x, point.y));
      path.closePath;
      path;
    case circleSweep:CircleSweep =>
      val complexShape = new Area(map(circleSweep.circle, pixelate));
      complexShape.add(new Area(map(circleSweep.destinationCircle, pixelate)));
      complexShape.add(new Area(map(circleSweep.connector._1, pixelate)));
      complexShape.add(new Area(map(circleSweep.connector._2, pixelate)));
      complexShape;
    case _ => throw new ShapeMappingException("No mapping for infinite shapes");
  }
  
  def mapPoint(point:Point, pixelate:Double => Double) = {
    new Point2D.Double(pixelate(point.x), pixelate(point.y));
  }
}