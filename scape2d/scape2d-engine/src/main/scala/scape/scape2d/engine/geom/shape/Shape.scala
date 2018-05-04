package scape.scape2d.engine.geom.shape

import java.lang.Math._

import com.google.common.math.DoubleMath._

import scape.scape2d.engine.geom._
import scape.scape2d.engine.geom.shape.intersection._

sealed trait Shape {
  def intersects(shape:Shape):Boolean;
  def contains(shape:Shape):Boolean;
  def displacedBy(components:Components):Shape;
  def toInt:ShapeInteger[_ <: Shape];
}

sealed trait Sweepable[T <: Shape] extends Shape {
  def sweep(sweepVector:Vector):T;
}

sealed trait Polygon extends Shape {
  lazy val points = fetchWaypoints(segments.iterator);
  
  def segments:List[Segment];
}

object Point {
  def origin = Point(0, 0);
}

case class Point(x:Double, y:Double) extends Shape {  
  def distanceTo(point:Point) = hypot(point.x - x, point.y - y);
  
  def angleTo(point:Point) = normalizeRadians(atan2(point.y - y, point.x - x));
  
  def angleToDeg(point:Point) = normalizeDegrees(toDegrees(atan2(point.y - y, point.x - x)));
  
  def +(components:Components) = Point(x + components.x, y + components.y);
  
  def -(point:Point) = Vector.from(Components(x - point.x, y - point.y));
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case line:Line => testIntersection(line, this);
    case ray:Ray => testIntersection(ray, this);
    case segment:Segment => testIntersection(segment, this);
    case circle:Circle => testIntersection(circle, this);
    case rectangle:AxisAlignedRectangle => testIntersection(rectangle, this);
    case polygon:Polygon => testIntersection(polygon, this);
    case circleSweep:CircleSweep => testIntersection(circleSweep, this);
    case ring:Ring => testIntersection(ring, this);
  }
  
  def contains(shape:Shape) = shape match {
    case point:Point => intersects(point);
    case _ => false;
  }
  
  def displacedBy(components:Components) = this + components;
  
  override def equals(a:Any) = a match {
    case Point(ox, oy) => fuzzyEquals(x, ox, Epsilon) && fuzzyEquals(y, oy, Epsilon);
    case _ => false;
  }
  
  lazy val toInt = PointInteger(round(x).toInt, round(y).toInt);
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
    case ring:Ring => testIntersection(ring, this);
  }
  
  def contains(shape:Shape) = shape match {
    case point:Point => intersects(point);
    case line:Line => if(vertical && line.vertical) fuzzyEquals(p1.x, line.p1.x, Epsilon);
                      else if(!vertical && !line.vertical && fuzzyEquals(slope.get, line.slope.get, Epsilon))
                        fuzzyEquals(yIntercept.get, line.yIntercept.get, Epsilon);
                      else false;
    case ray:Ray => contains(ray.line);
    case segment:Segment => contains(segment.line);
    case _:Circle | _:Polygon | _:CircleSweep | _:Ring => false;
  }
  
  def displacedBy(components:Components) = Line(p1 + components, p2 + components);
  
  lazy val toInt = LineInteger(p1.toInt, p2.toInt);
}

case class Ray(origin:Point, angle:Double) extends Shape {
  lazy val line = Line(origin, origin + Vector(1, angle));
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case line:Line => testIntersection(this, line);
    case ray:Ray => testIntersection(this, ray);
    case segment:Segment => testIntersection(segment, this);
    case circle:Circle => testIntersection(circle, this);
    case polygon:Polygon => testIntersection(polygon, this);
    case circleSweep:CircleSweep => testIntersection(circleSweep, this);
    case ring:Ring => testIntersection(ring, this);
  }
  
  def contains(shape:Shape) = shape match {
    case point:Point => intersects(point);
    case Ray(origin2, angle2) => angle == angle2 && intersects(origin2);
    case Segment(p1, p2) => intersects(p1) && intersects(p2);
    case _:Line | _:Circle | _:Polygon | _:CircleSweep | _:Ring => false;
  }
  
  def displacedBy(components:Components) = Ray(origin + components, angle);
  
  lazy val toInt = RayInteger(origin.toInt, angle);
}

case class Segment(p1:Point, p2:Point) extends Shape {
  lazy val line = Line(p1, p2);
  lazy val length = p1 distanceTo p2;
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case line:Line => testIntersection(this, line);
    case ray:Ray => testIntersection(this, ray);
    case segment:Segment => testIntersection(this, segment);
    case circle:Circle => testIntersection(circle, this);
    case polygon:Polygon => testIntersection(polygon, this);
    case circleSweep:CircleSweep => testIntersection(circleSweep, this);
    case ring:Ring => testIntersection(ring, this);
  }
  
  def contains(shape:Shape) = shape match {
    case point:Point => intersects(point);
    case Segment(p3, p4) => intersects(p3) && intersects(p4);
    case _:Line | _:Ray | _:Circle | _:Polygon | _:CircleSweep | _:Ring => false;
  }
  
  def displacedBy(components:Components) = Segment(p1 + components, p2 + components);
  
  lazy val toInt = SegmentInteger(p1.toInt, p2.toInt);
}

case class Circle(center:Point, radius:Double) extends Sweepable[CircleSweep] {
  def sweep(sweepVector:Vector) = CircleSweep(this, sweepVector);
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case line:Line => testIntersection(this, line);
    case ray:Ray => testIntersection(this, ray);
    case segment:Segment => testIntersection(this, segment);
    case circle:Circle => testIntersection(this, circle);
    case polygon:Polygon => testIntersection(polygon, this);
    case circleSweep:CircleSweep => testIntersection(circleSweep, this);
    case ring:Ring => testIntersection(ring, this);
  }
  
  def contains(shape:Shape) = shape match {
    case point:Point => intersects(point);
    case Segment(p1, p2) => intersects(p1) && intersects(p2);
    case Circle(center2, radius2) => center.distanceTo(center2) + radius2 <= radius;
    case polygon:Polygon => polygon.points.forall(intersects);
    case circleSweep:CircleSweep => contains(circleSweep.circle) && contains(circleSweep.destinationCircle);
    case ring:Ring => contains(ring.outerCircle);
    case _:Line | _:Ray => false;
  }
  
  def displacedBy(components:Components) = Circle(center + components, radius);
  
  override def equals(any:Any) = any match {
    case Circle(ocenter, oradius) => center == ocenter && fuzzyEquals(radius, oradius, Epsilon);
    case _ => false;
  }
  
  lazy val toInt = CircleInteger(center.toInt, round(radius).toInt);
}

case class CustomPolygon private[shape] (segments:List[Segment]) extends Polygon {
  override def equals(any:Any) = any match {
    case polygon:Polygon => segments == polygon.segments;
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
    case ring:Ring => testIntersection(ring, this);
  }
  
  def contains(shape:Shape) = shape match {
    case point:Point => intersects(point);
    case segment:Segment => segments.forall(!_.intersects(segment));
    case circle:Circle => intersects(circle.center) && segments.forall(!_.intersects(circle));
    case polygon:Polygon => polygon.segments.forall(contains);
    case circleSweep:CircleSweep => contains(circleSweep.circle) &&
                                    contains(circleSweep.destinationCircle) &&
                                    contains(circleSweep.connector._1) &&
                                    contains(circleSweep.connector._2);
    case ring:Ring => contains(ring.outerCircle);
    case _:Line | _:Ray => false;
  }
  
  def displacedBy(components:Components) = {
    val displacedSegments = segments.map(_ displacedBy components);
    CustomPolygon(displacedSegments);
  }
  
  lazy val toInt = PolygonInteger(segments.map(_.toInt));
}

case class AxisAlignedRectangle(bottomLeft:Point, width:Double, height:Double) extends Polygon {
  lazy val topLeft = Point(bottomLeft.x, bottomLeft.y + height);
  lazy val topRight = Point(bottomLeft.x + width, bottomLeft.y + height);
  lazy val bottomRight = Point(bottomLeft.x + width, bottomLeft.y);
  lazy val polygon = PolygonBuilder(bottomLeft, topLeft, topRight).to(bottomRight).build;
  
  def slice(pieces:Int) = {
    if(pieces < 4) throw new IllegalArgumentException("Pieces param must be greater than or equal to 4");
    val floor = sqrt(pieces).toInt;
    if(floor * floor != pieces) throw new IllegalArgumentException("Pieces param must be perfectly squared");
    val pieceWidth = width / floor;
    val pieceHeight = height / floor;
    for(v <- 0 to floor - 1; h <- 0 to floor - 1)
    yield AxisAlignedRectangle(Point(bottomLeft.x + v * pieceWidth, bottomLeft.y + h * pieceHeight),
                               pieceWidth, pieceHeight);
  }
  
  def segments = polygon.segments;
  
  def intersects(shape:Shape) = shape match {
    case point:Point => testIntersection(this, point);
    case aabb:AxisAlignedRectangle => testIntersection(this, aabb);
    case _:Line | _:Ray | _:Segment | _:Circle | _:Polygon | _:CircleSweep | _:Ring => polygon.intersects(shape);
  }
  
  def contains(shape:Shape) = shape match {
    case point:Point => intersects(point);
    case Segment(p1, p2) => intersects(p1) && intersects(p2);
    case Circle(center, radius) => center.x >= bottomLeft.x + radius && center.x <= bottomRight.x - radius &&
                                   center.y >= bottomRight.y + radius && center.y <= topLeft.y - radius;
    case polygon:Polygon => polygon.points.forall(intersects);
    case circleSweep:CircleSweep => contains(circleSweep.circle) && contains(circleSweep.destinationCircle);
    case ring:Ring => contains(ring.outerCircle);
    case _:Line | _:Ray => polygon.contains(shape);
  }
  
  def displacedBy(components:Components) = AxisAlignedRectangle(bottomLeft + components, width, height);
  
  override def equals(any:Any) = any match {
    case AxisAlignedRectangle(obottomLeft, owidth, oheight) => bottomLeft == obottomLeft &&
                                                               fuzzyEquals(width, owidth, Epsilon) &&
                                                               fuzzyEquals(height, oheight, Epsilon);
    case _ => false;
  }
  
  lazy val toInt = AxisAlignedRectangleInteger(bottomLeft.toInt, round(width).toInt, round(height).toInt);
}

case class CircleSweep(circle:Circle, sweepVector:Vector) extends Shape {
  lazy val destinationCircle = Circle(circle.center + sweepVector, circle.radius);
  lazy val connector = {
    val origin = circle.center;
    val destination = origin + sweepVector;
    val radialVectorToConnector = Vector(circle.radius, normalizeDegrees(sweepVector.angle + 90));
    val connector1 = Segment(origin + radialVectorToConnector, 
                             destination + radialVectorToConnector);
    val connector2 = Segment(destination + radialVectorToConnector.opposite,
                             origin + radialVectorToConnector.opposite);
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
    case ring:Ring => testIntersection(ring, this);
  }
  
  def contains(shape:Shape) = shape match {
    case point:Point => intersects(point);
    case Segment(p1, p2) => intersects(p1) && intersects(p2);
    case ocircle:Circle => intersects(ocircle.center) &&
                           ((circle.contains(ocircle) || destinationCircle.contains(ocircle)) ||
                            (!ocircle.intersects(connector._1) && !ocircle.intersects(connector._2)));
    case polygon:Polygon => polygon.points.forall(contains);
    case cs:CircleSweep => contains(cs.circle) && contains(cs.destinationCircle);
    case ring:Ring => contains(ring.outerCircle);
    case _:Line | _:Ray => false;
  }
  
  def displacedBy(components:Components) = CircleSweep(circle displacedBy components, sweepVector);
  
  lazy val toInt = CircleSweepInteger(circle.toInt, sweepVector);
}

case class Ring(circle:Circle, thickness:Double) extends Shape {
  lazy val outerCircle = circle.copy(radius = circle.radius + thickness / 2);
  lazy val innerCircle = circle.copy(radius = circle.radius - thickness / 2);
  
  def intersects(shape:Shape) = testIntersection(this, shape);
  
  def contains(shape:Shape) = outerCircle contains shape;
  
  def displacedBy(components:Components) = Ring(circle displacedBy components, thickness);
  
  lazy val toInt = RingInteger(circle.toInt, thickness);
}