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

case class Line(p1:Point, p2:Point) {
  private lazy val dx = p2.x - p1.x;
  private lazy val dy = p2.y - p1.y;
  lazy val slope = if(dx != 0) Some(dy / dx) else None; // slope is undefined for vertical lines
  lazy val yIntercept = if(slope.isDefined) Some(p1.y - slope.get * p1.x) else None;
  
  def horizontal = dy == 0;
  
  def vertical = dx == 0;
  
  def forX(x:Double) = {
    if(horizontal) p1.y;
    else if(!vertical) slope.get * x + yIntercept.get;
    else throw new ArithmeticException("Y resolution on vertical line has either no or infinite solutions");
  }
  
  def forY(y:Double) = {
    if(vertical) p1.x;
    else if(!horizontal) (y - yIntercept.get) / slope.get;
    else throw new ArithmeticException("X resolution on horizontal line has either no or infinite solutions");
  }
}