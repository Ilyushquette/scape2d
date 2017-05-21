package scape.scape2d.engine.geom

import java.lang.Math._

object Point2D {
  def origin = new Point2D(0, 0);
}

class Point2D(var x:Double, var y:Double) {
  def displace(components:Components2D) = {
    this.x += components.x;
    this.y += components.y;
  }
  
  def locate(point:Point2D) = {
    this.x = point.x;
    this.y = point.y;
  }
  
  def distanceTo(point:Point2D) = hypot(point.x - x, point.y - y);
  
  def -(point:Point2D) = Vector2D.from(Components2D(x - point.x, y - point.y));
  
  override def clone = new Point2D(x, y);
  
  override def hashCode = x.hashCode + y.hashCode;
  
  override def equals(a:Any) = a match {
    case point:Point2D => x == point.x && y == point.y
    case _ => false
  }
  
  override def toString = "Point2D [x=%f, y=%f]".format(x, y);
}