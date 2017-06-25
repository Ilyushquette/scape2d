package scape.scape2d.engine.geom.shape

import java.lang.Math._

import com.google.common.math.DoubleMath._

import scape.scape2d.engine.geom._
import scape.scape2d.engine.geom.shape.intersection._

sealed trait Shape {
  def intersects(shape:Shape):Boolean;
  def contains(shape:Shape):Boolean;
}

sealed trait Sweepable[T <: Shape] extends Shape {
  def sweep(sweepVector:Vector2D):T;
}

sealed trait Polygon extends Shape {
  def segments:Array[Segment];
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
    case line:Line => testIntersection(line, this);
    case ray:Ray => testIntersection(ray, this);
    case segment:Segment => testIntersection(segment, this);
    case circle:Circle => testIntersection(circle, this);
    case rectangle:AxisAlignedRectangle => testIntersection(rectangle, this);
    case polygon:Polygon => testIntersection(polygon, this);
    case circleSweep:CircleSweep => testIntersection(circleSweep, this);
  }
  
  def contains(shape:Shape) = shape match {
    case point:Point => intersects(point);
    case _ => false;
  }
  
  override def equals(a:Any) = a match {
    case Point(ox, oy) => fuzzyEquals(x, ox, Epsilon) && fuzzyEquals(y, oy, Epsilon);
    case _ => false;
  }
}

case class Line(p1:Point, p2:Point) extends Shape {
  private lazy val dx = p2.x - p1.x;
  private lazy val dy = p2.y - p1.y;
  lazy val slope = if(dx != 0) Some(dy / dx) else None; // slope is undefined for vertical lines
  lazy val yIntercept = if(slope.isDefined) Some(p1.y - slope.get * p1.x) else None;
  
  def horizontal = fuzzyEquals(dy, 0, Epsilon);
  
  def vertical = fuzzyEquals(dx, 0, Epsilon);
  
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
  
  def clampAbscissa(x1:Double, x2:Double) = Segment(Point(x1, forX(x1)), Point(x2, forX(x2)));
  
  def clampOrdinate(y1:Double, y2:Double) = Segment(Point(forY(y1), y1), Point(forY(y2), y2));
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case line:Line => testIntersection(this, line);
    case ray:Ray => testIntersection(ray, this);
    case segment:Segment => testIntersection(segment, this);
    case circle:Circle => testIntersection(circle, this);
    case polygon:Polygon => testIntersection(polygon, this);
    case circleSweep:CircleSweep => testIntersection(circleSweep, this);
  }
  
  def contains(shape:Shape) = shape match {
    case point:Point => intersects(point);
    case line:Line => if(vertical && line.vertical) fuzzyEquals(p1.x, line.p1.x, Epsilon);
                      else if(!vertical && !line.vertical && fuzzyEquals(slope.get, line.slope.get, Epsilon))
                        fuzzyEquals(yIntercept.get, line.yIntercept.get, Epsilon);
                      else false;
    case ray:Ray => contains(ray.line);
    case segment:Segment => contains(segment.line);
    case _ => false;
  }
}

case class Ray(origin:Point, angle:Double) extends Shape {
  lazy val line = {
    val unitVector = new Vector2D(1, angle);
    Line(origin, origin.displace(unitVector.components));
  }
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case line:Line => testIntersection(this, line);
    case ray:Ray => testIntersection(this, ray);
    case segment:Segment => testIntersection(segment, this);
    case circle:Circle => testIntersection(circle, this);
    case polygon:Polygon => testIntersection(polygon, this);
    case circleSweep:CircleSweep => testIntersection(circleSweep, this);
  }
  
  def contains(shape:Shape) = shape match {
    case point:Point => intersects(point);
    case Ray(origin2, angle2) => fuzzyEquals(angle, angle2, Epsilon) && intersects(origin2);
    case Segment(p1, p2) => intersects(p1) && intersects(p2);
    case _ => false;
  }
}

case class Segment(p1:Point, p2:Point) extends Shape {
  lazy val line = Line(p1, p2);
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case line:Line => testIntersection(this, line);
    case ray:Ray => testIntersection(this, ray);
    case segment:Segment => testIntersection(this, segment);
    case circle:Circle => testIntersection(circle, this);
    case polygon:Polygon => testIntersection(polygon, this);
    case circleSweep:CircleSweep => testIntersection(circleSweep, this);
  }
  
  def contains(shape:Shape) = throw new RuntimeException("NOT IMPLEMENTED!");
}

case class Circle(center:Point, radius:Double) extends Sweepable[CircleSweep] {
  def sweep(sweepVector:Vector2D) = CircleSweep(this, sweepVector);
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case line:Line => testIntersection(this, line);
    case ray:Ray => testIntersection(this, ray);
    case segment:Segment => testIntersection(this, segment);
    case circle:Circle => testIntersection(this, circle);
    case polygon:Polygon => testIntersection(polygon, this);
    case circleSweep:CircleSweep => testIntersection(circleSweep, this);
  }
  
  def contains(shape:Shape) = throw new RuntimeException("NOT IMPLEMENTED!");
}

case class CustomPolygon private[shape] (segments:Array[Segment]) extends Polygon {
  override def equals(any:Any) = any match {
    case polygon:Polygon => segments.deep == polygon.segments.deep;
    case _ => false;
  }
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case line:Line => testIntersection(this, line);
    case ray:Ray => testIntersection(this, ray);
    case segment:Segment => testIntersection(this, segment);
    case circle:Circle => testIntersection(this, circle);
    case polygon:Polygon => testIntersection(this, polygon);
    case circleSweep:CircleSweep => testIntersection(circleSweep, this);
  }
  
  def contains(shape:Shape) = throw new RuntimeException("NOT IMPLEMENTED!");
}

case class AxisAlignedRectangle(bottomLeft:Point, width:Double, height:Double) extends Polygon {
  lazy val topLeft = Point(bottomLeft.x, bottomLeft.y + height);
  lazy val topRight = Point(bottomLeft.x + width, bottomLeft.y + height);
  lazy val bottomRight = Point(bottomLeft.x + width, bottomLeft.y);
  lazy val polygon = PolygonBuilder(bottomLeft, topLeft, topRight).to(bottomRight).build;
  
  def segments = polygon.segments;
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case aabb:AxisAlignedRectangle => testIntersection(this, aabb);
    case untuned => polygon.intersects(untuned);
  }
  
  def contains(shape:Shape) = throw new RuntimeException("NOT IMPLEMENTED!");
}

case class CircleSweep(circle:Circle, sweepVector:Vector2D) extends Shape {
  lazy val destinationCircle = Circle(circle.center.displace(sweepVector.components), circle.radius);
  lazy val connector = {
    val origin = circle.center;
    val destination = origin.displace(sweepVector.components);
    val sweepPerpendicularToRadius = new Vector2D(circle.radius, normalizeAngle(sweepVector.angle + 90));
    val connector1 = Segment(origin.displace(sweepPerpendicularToRadius.components), 
                             destination.displace(sweepPerpendicularToRadius.components));
    val connector2 = Segment(destination.displace(sweepPerpendicularToRadius.opposite.components),
                             origin.displace(sweepPerpendicularToRadius.opposite.components));
    (connector1, connector2);
  }
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case line:Line => testIntersection(this, line);
    case ray:Ray => testIntersection(this, ray);
    case segment:Segment => testIntersection(this, segment);
    case circle:Circle => testIntersection(this, circle);
    case polygon:Polygon => testIntersection(this, polygon);
    case circleSweep:CircleSweep => testIntersection(this, circleSweep);
  }
  
  def contains(shape:Shape) = throw new RuntimeException("NOT IMPLEMENTED!");
}