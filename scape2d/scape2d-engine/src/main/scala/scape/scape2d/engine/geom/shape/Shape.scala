package scape.scape2d.engine.geom.shape

import java.lang.Math.PI
import java.lang.Math.abs
import java.lang.Math.hypot
import java.lang.Math.round
import java.lang.Math.sqrt

import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.geom.AreaEpsilon
import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.Vector.toComponents
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.geom.angle.Radian
import scape.scape2d.engine.geom.angle.UnboundAngle
import scape.scape2d.engine.geom.fetchWaypoints
import scape.scape2d.engine.geom.shape.intersection.testIntersection
import scape.scape2d.engine.util.InfiniteSolutions
import scape.scape2d.engine.util.NoSolution
import scape.scape2d.engine.util.SomeSolution
import scape.scape2d.engine.util.Solution

sealed trait Shape {
  def intersects(shape:Shape):Boolean;
  
  def contains(shape:Shape):Boolean;
  
  def displacedBy(components:Components):Shape;
  
  def rotatedAround(point:Point, angle:Angle):Shape;
  
  def perimeter:Perimeter;
  
  def toInt:ShapeInteger[_ <: Shape];
}

sealed trait FiniteShape extends Shape {
  def center:Point;
  
  def area:Double;
  
  def perimeter:FinitePerimeter;
}

sealed trait ConvexShape extends FiniteShape {
  def displacedBy(components:Components):ConvexShape;
  
  def rotatedAround(point:Point, angle:Angle):ConvexShape;
}

sealed trait Sweepable[T <: Shape] extends Shape {
  def sweep(sweepVector:Vector):T;
}

sealed trait Polygon extends FiniteShape {
  lazy val points = fetchWaypoints(segments.iterator);
  
  def segments:List[Segment];
  
  def displacedBy(components:Components):Polygon;
  
  def rotatedAround(point:Point, angle:Angle):Polygon;
  
  lazy val perimeter = PolygonPerimeter(this);
}

object Point {
  def origin = Point(0, 0);
}

case class Point(x:Double, y:Double) extends ConvexShape {
  val center = this;
  val area = AreaEpsilon;
  lazy val perimeter = PointPerimeter(this);
  
  def distanceTo(point:Point) = hypot(point.x - x, point.y - y);
  
  def angleTo(point:Point) = Angle.from(Components(point.x - x, point.y - y));
  
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
  
  def rotatedAround(point:Point, angle:Angle):Point = {
    if(this != point) {
      val vectorToThis = this - point;
      val vectorToNewPoint = vectorToThis.copy(angle = vectorToThis.angle + angle);
      point + vectorToNewPoint;
    }else this;
  }
  
  override def equals(a:Any) = a match {
    case Point(ox, oy) => fuzzyEquals(x, ox, Epsilon) && fuzzyEquals(y, oy, Epsilon);
    case _ => false;
  }
  
  lazy val toInt = PointInteger(round(x).toInt, round(y).toInt);
}

case class Line(p1:Point, p2:Point) extends Shape {
  private lazy val dx = p2.x - p1.x;
  private lazy val dy = p2.y - p1.y;
  lazy val slope = if(!vertical) Some(dy / dx) else None; // slope is undefined for vertical lines
  lazy val yIntercept = if(slope.isDefined) Some(p1.y - slope.get * p1.x) else None;
  lazy val angle = p1 angleTo p2;
  lazy val perimeter = LinePerimeter(this);
  
  def horizontal = fuzzyEquals(dy, 0, Epsilon);
  
  def vertical = fuzzyEquals(dx, 0, Epsilon);
  
  def forX(x:Double) = {
    if(horizontal) SomeSolution(p1.y);
    else if(!vertical) SomeSolution(slope.get * x + yIntercept.get);
    else if(!fuzzyEquals(p1.x, x, Epsilon)) NoSolution;
    else InfiniteSolutions;
  }
  
  def forY(y:Double) = {
    if(vertical) SomeSolution(p1.x);
    else if(!horizontal) SomeSolution((y - yIntercept.get) / slope.get);
    else if(!fuzzyEquals(p1.y, y, Epsilon)) NoSolution;
    else InfiniteSolutions;
  }
  
  def clampAbscissa(x1:Double, x2:Double) = Segment(Point(x1, forX(x1).solution), Point(x2, forX(x2).solution));
  
  def clampOrdinate(y1:Double, y2:Double) = Segment(Point(forY(y1).solution, y1), Point(forY(y2).solution, y2));
  
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
  
  def rotatedAround(point:Point, angle:Angle) = Line(p1.rotatedAround(point, angle), p2.rotatedAround(point, angle));
  
  lazy val toInt = LineInteger(p1.toInt, p2.toInt);
}

case class Ray(origin:Point, angle:Angle) extends Shape {
  lazy val line = Line(origin, origin + Vector(1, angle));
  lazy val perimeter = RayPerimeter(this);
  
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
  
  def rotatedAround(point:Point, angle:Angle) = Ray(origin.rotatedAround(point, angle), this.angle + angle);
  
  lazy val toInt = RayInteger(origin.toInt, angle);
}

case class Segment(p1:Point, p2:Point) extends ConvexShape {
  lazy val line = Line(p1, p2);
  lazy val length = p1 distanceTo p2;
  lazy val center = Point(
      x = p1.x + (p2.x - p1.x) / 2,
      y = p1.y + (p2.y - p1.y) / 2
  );
  lazy val area = Epsilon * length;
  lazy val perimeter = SegmentPerimeter(this);
  
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
  
  override def equals(any:Any) = any match {
    case Segment(p1, p2) => (this.p1 == p1 && this.p2 == p2) ||
                            (this.p1 == p2 && this.p2 == p1);
    case _ => false;
  }
  
  def displacedBy(components:Components) = Segment(p1 + components, p2 + components);
  
  def rotatedAround(point:Point, angle:Angle) = Segment(p1.rotatedAround(point, angle), p2.rotatedAround(point, angle));
  
  lazy val toInt = SegmentInteger(p1.toInt, p2.toInt);
}

case class Circle(center:Point, radius:Double) extends ConvexShape with Sweepable[CircleSweep] {
  lazy val area = PI * radius * radius;
  lazy val diameter = radius * 2;
  lazy val perimeter = CirclePerimeter(this);
  
  def sweep(sweepVector:Vector) = CircleSweep(this, sweepVector);
  
  def forX(x:Double):Set[Solution[Double]] = {
    val a = center.x;
    val b = center.y;
    val r = radius;
    val B = -2 * b;
    val C = x * x + a * a + b * b - 2 * a * x - r * r;
    val discriminant = B * B - 4 * C;
    if(discriminant > 0) {
      val solutionY1 = SomeSolution((-B + sqrt(discriminant)) / 2);
      val solutionY2 = SomeSolution((-B - sqrt(discriminant)) / 2);
      Set(solutionY1, solutionY2);
    }else if(discriminant == 0) {
      val solutionY = SomeSolution(-B / 2);
      Set(solutionY);
    }else Set.empty;
  }
  
  def forY(y:Double):Set[Solution[Double]] = {
    val a = center.x;
    val b = center.y;
    val r = radius;
    val B = -2 * a;
    val C = y * y + a * a + b * b - 2 * b * y - r * r;
    val discriminant = B * B - 4 * C;
    if(discriminant > 0) {
      val solutionX1 = SomeSolution((-B + sqrt(discriminant)) / 2);
      val solutionX2 = SomeSolution((-B - sqrt(discriminant)) / 2);
      Set(solutionX1, solutionX2);
    }else if(discriminant == 0) {
      val solutionX = SomeSolution(-B / 2);
      Set(solutionX);
    }else Set.empty;
  }
  
  def forLength(length:Double) = UnboundAngle(length / radius, Radian);
  
  def forAngle(angle:UnboundAngle) = angle.radians * radius;
  
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
  
  def rotatedAround(point:Point, angle:Angle) = Circle(center.rotatedAround(point, angle), radius);
  
  override def equals(any:Any) = any match {
    case Circle(ocenter, oradius) => center == ocenter && fuzzyEquals(radius, oradius, Epsilon);
    case _ => false;
  }
  
  lazy val toInt = CircleInteger(center.toInt, round(radius).toInt);
}

object CustomPolygon {
  private[shape] def apply(segments:List[Segment]) = new CustomPolygon(segments, centroidOf(segments), areaOf(segments));
  
  private[CustomPolygon] def areaOf(segments:List[Segment], area:Double = 0):Double = segments match {
    case Segment(Point(x1, y1), Point(x2, y2))::Nil =>
      abs(area + (x1 * y2 - x2 * y1)) / 2;
    case Segment(Point(x1, y1), Point(x2, y2))::segments =>
      areaOf(segments, area + (x1 * y2 - x2 * y1));
    case Nil => throw new IllegalArgumentException("No segments - no area");
  }
  
  private[CustomPolygon] def centroidOf(segments:List[Segment], signedArea:Double = 0, centroid:Point = Point.origin):Point = segments match {
    case Segment(Point(x1, y1), Point(x2, y2))::Nil =>
      val a = (x1 * y2 - x2 * y1);
      val updatedCentroid  = centroid displacedBy Components((x1 + x2) * a, (y1 + y2) * a);
      val updatedSignedArea = (signedArea + a) * 3;
      Point(updatedCentroid.x / updatedSignedArea, updatedCentroid.y / updatedSignedArea);
    case Segment(Point(x1, y1), Point(x2, y2))::segments =>
      val a = (x1 * y2 - x2 * y1);
      val updatedCentroid  = centroid displacedBy Components((x1 + x2) * a, (y1 + y2) * a);
      centroidOf(segments, signedArea + a, updatedCentroid);
    case Nil => throw new IllegalArgumentException("No segments - no centroid");
  }
}

case class CustomPolygon private[shape] (segments:List[Segment], center:Point, area:Double) extends Polygon {
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
    val displacedCenter = center displacedBy components;
    CustomPolygon(displacedSegments, displacedCenter, area);
  }
  
  def rotatedAround(point:Point, angle:Angle) = {
    val rotatedSegments = segments.map(_.rotatedAround(point, angle));
    val rotatedCenter = center.rotatedAround(point, angle);
    CustomPolygon(rotatedSegments, rotatedCenter, area);
  }
  
  lazy val toInt = PolygonInteger(segments.map(_.toInt));
}

case class ConvexPolygon private[shape] (polygon:Polygon) extends Polygon with ConvexShape {
  val segments = polygon.segments;
  lazy val center = polygon.center;
  lazy val area = polygon.area;
  
  def intersects(shape:Shape) = polygon intersects shape;
  
  def contains(shape:Shape) = shape match {
    case Segment(p1, p2) => intersects(p1) && intersects(p2);
    case polygon:Polygon => polygon.points.forall(contains);
    case circleSweep:CircleSweep => contains(circleSweep.circle) &&
                                    contains(circleSweep.destinationCircle) &&
                                    contains(circleSweep.connector._1) &&
                                    contains(circleSweep.connector._2);
    case _:Point | _:Line | _:Ray | _:Circle | _:Ring => polygon contains shape;
  }
  
  def displacedBy(components:Components) = ConvexPolygon(polygon displacedBy components);
  
  def rotatedAround(point:Point, angle:Angle) = ConvexPolygon(polygon.rotatedAround(point, angle));
  
  lazy val toInt = PolygonInteger(segments.map(_.toInt));
}

case class AxisAlignedRectangle(bottomLeft:Point, width:Double, height:Double) extends ConvexShape with Polygon {
  lazy val topLeft = Point(bottomLeft.x, bottomLeft.y + height);
  lazy val topRight = Point(bottomLeft.x + width, bottomLeft.y + height);
  lazy val bottomRight = Point(bottomLeft.x + width, bottomLeft.y);
  lazy val polygon = ConvexPolygon(PolygonBuilder(bottomLeft, topLeft, topRight)
                                   .to(bottomRight)
                                   .build);
  lazy val center = Point(bottomLeft.x + width / 2, bottomLeft.y + height / 2);
  lazy val area = width * height;
  
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
  
  def rotatedAround(point:Point, angle:Angle) = polygon.rotatedAround(point, angle);
  
  override def equals(any:Any) = any match {
    case AxisAlignedRectangle(obottomLeft, owidth, oheight) => bottomLeft == obottomLeft &&
                                                               fuzzyEquals(width, owidth, Epsilon) &&
                                                               fuzzyEquals(height, oheight, Epsilon);
    case _ => false;
  }
  
  lazy val toInt = AxisAlignedRectangleInteger(bottomLeft.toInt, round(width).toInt, round(height).toInt);
}

case class CircleSweep(circle:Circle, sweepVector:Vector) extends ConvexShape {
  lazy val destinationCircle = Circle(circle.center + sweepVector, circle.radius);
  lazy val connector = {
    val radialVectorToConnector = Vector(circle.radius, sweepVector.angle + Angle.right);
    val connector1 = Segment(circle.center + radialVectorToConnector, 
                             destinationCircle.center + radialVectorToConnector);
    val connector2 = Segment(destinationCircle.center + radialVectorToConnector.opposite,
                             circle.center + radialVectorToConnector.opposite);
    (connector1, connector2);
  }
  lazy val center = circle.center + sweepVector / 2;
  lazy val area = circle.area + circle.diameter * sweepVector.magnitude;
  lazy val perimeter = CircleSweepPerimeter(this);
  
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
  
  def rotatedAround(point:Point, angle:Angle) = {
    val rotatedCircle = circle.rotatedAround(point, angle);
    val rotatedSweepVector = sweepVector.copy(angle = sweepVector.angle + angle);
    CircleSweep(rotatedCircle, rotatedSweepVector);
  }
  
  lazy val toInt = CircleSweepInteger(circle.toInt, sweepVector);
}

case class Ring(circle:Circle, thickness:Double) extends ConvexShape {
  lazy val outerCircle = circle.copy(radius = circle.radius + thickness / 2);
  lazy val innerCircle = circle.copy(radius = circle.radius - thickness / 2);
  lazy val area = outerCircle.area - innerCircle.area;
  lazy val perimeter = RingPerimeter(this);
  val center = circle.center;
  
  def intersects(shape:Shape) = testIntersection(this, shape);
  
  def contains(shape:Shape) = outerCircle contains shape;
  
  def displacedBy(components:Components) = Ring(circle displacedBy components, thickness);
  
  def rotatedAround(point:Point, angle:Angle) = Ring(circle.rotatedAround(point, angle), thickness);
  
  lazy val toInt = RingInteger(circle.toInt, thickness);
}