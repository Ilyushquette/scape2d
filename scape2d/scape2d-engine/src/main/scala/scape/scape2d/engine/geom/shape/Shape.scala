package scape.scape2d.engine.geom.shape

import java.lang.Math._

import scape.scape2d.engine.geom._
import scape.scape2d.engine.geom.shape.intersection._

sealed trait Shape {
  def intersects(shape:Shape):Boolean;
}

object Point {
  def origin = Point(0, 0);
}

case class Point(x:Double, y:Double) extends Shape {
  def displace(components:Components2D) = Point(x + components.x, y + components.y);
  
  def distanceTo(point:Point) = hypot(point.x - x, point.y - y);
  
  def angleTo(point:Point) = normalizeAngle(toDegrees(atan2(point.y - y, point.x - x)));
  
  def -(point:Point) = Vector2D.from(Components2D(x - point.x, y - point.y));
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
  }
  
  override def hashCode = x.hashCode + y.hashCode;
  
  override def equals(a:Any) = a match {
    case Point(ox, oy) => x == ox && y == oy
    case _ => false
  }
  
  override def toString = "Point2D [x=%f, y=%f]".format(x, y);
}