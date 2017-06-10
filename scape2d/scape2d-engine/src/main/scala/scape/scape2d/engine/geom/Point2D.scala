package scape.scape2d.engine.geom

import java.lang.Math._

object Point2D {
  def origin = new Point2D(0, 0);
}

class Point2D(val x:Double, val y:Double) {
  def displace(components:Components2D) = new Point2D(x + components.x, y + components.y);
  
  def distanceTo(point:Point2D) = hypot(point.x - x, point.y - y);
  
  def angleTo(point:Point2D) = normalizeAngle(toDegrees(atan2(point.y - y, point.x - x)));
  
  def -(point:Point2D) = Vector2D.from(Components2D(x - point.x, y - point.y));
  
  override def hashCode = x.hashCode + y.hashCode;
  
  override def equals(a:Any) = a match {
    case point:Point2D => x == point.x && y == point.y
    case _ => false
  }
  
  override def toString = "Point2D [x=%f, y=%f]".format(x, y);
}