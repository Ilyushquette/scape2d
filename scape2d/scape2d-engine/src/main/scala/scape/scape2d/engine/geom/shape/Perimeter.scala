package scape.scape2d.engine.geom.shape

import java.lang.Math.PI
import java.lang.Math.pow
import java.lang.Math.sqrt

import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.geom.Epsilon
import scape.scape2d.engine.geom.Vector
import scape.scape2d.engine.geom.Vector.toComponents
import scape.scape2d.engine.geom.angle.Angle
import scape.scape2d.engine.util.InfiniteSolutions
import scape.scape2d.engine.util.NoSolution
import scape.scape2d.engine.util.Solution
import scape.scape2d.engine.util.SomeSolution

sealed trait Perimeter {
  def shape:Shape;
}

sealed trait FinitePerimeter extends Perimeter {
  def shape:FiniteShape;
  
  def length:Double;
}

object Perimeter {
  def intersectionPointBetween(p1:Point, p2:Point):Solution[Point] = {
    if(p1 == p2) SomeSolution(p1);
    else NoSolution;
  }
  
  def intersectionPointBetween(line:Line, point:Point):Solution[Point] = {
    if(line intersects point) SomeSolution(point);
    else NoSolution;
  }
  
  def intersectionPointBetween(l1:Line, l2:Line):Solution[Point] = {
    if(!l1.vertical && !l2.vertical) {
      if(!fuzzyEquals(l1.slope.get, l2.slope.get, Epsilon)) {
        val x = (l2.yIntercept.get - l1.yIntercept.get) / (l1.slope.get - l2.slope.get);
        l1.forX(x).map(Point(x, _));
      }
      else if(!fuzzyEquals(l1.yIntercept.get, l2.yIntercept.get, Epsilon)) NoSolution;
      else InfiniteSolutions;
    }
    else if(!l1.vertical && l2.vertical) SomeSolution(Point(l2.p1.x, l1.forX(l2.p1.x).solution));
    else if(l1.vertical && !l2.vertical) SomeSolution(Point(l1.p1.x, l2.forX(l1.p1.x).solution));
    else if(!fuzzyEquals(l1.p1.x, l2.p2.x, Epsilon)) NoSolution;
    else InfiniteSolutions;
  }
  
  def intersectionPointBetween(ray:Ray, point:Point):Solution[Point] = {
    if(ray intersects point) SomeSolution(point);
    else NoSolution;
  }
  
  def intersectionPointBetween(ray:Ray, line:Line):Solution[Point] = {
    val intersectionPointBetweenLines = intersectionPointBetween(ray.line, line);
    if(intersectionPointBetweenLines.hasSolution)
      if(ray.intersects(intersectionPointBetweenLines.solution))
        intersectionPointBetweenLines;
      else NoSolution;
    else intersectionPointBetweenLines;
  }
  
  def intersectionPointBetween(r1:Ray, r2:Ray):Solution[Point] = {
    if(r1 intersects r2) intersectionPointBetween(r1.line, r2.line);
    else NoSolution;
  }
  
  def intersectionPointBetween(segment:Segment, point:Point):Solution[Point] = {
    if(segment intersects point) SomeSolution(point);
    else NoSolution;
  }
  
  def intersectionPointBetween(segment:Segment, line:Line):Solution[Point] = {
    if(segment intersects line) intersectionPointBetween(segment.line, line);
    else NoSolution;
  }
  
  def intersectionPointBetween(segment:Segment, ray:Ray):Solution[Point] = {
    if(segment intersects ray) intersectionPointBetween(segment.line, ray.line);
    else NoSolution;
  }
  
  def intersectionPointBetween(s1:Segment, s2:Segment):Solution[Point] = {
    if(s1 intersects s2) intersectionPointBetween(s1.line, s2.line);
    else NoSolution;
  }
  
  def intersectionPointBetween(circle:Circle, point:Point):Solution[Point] = {
    val center = circle.center;
    if(fuzzyEquals(pow(point.x - center.x, 2) + pow(point.y - center.y, 2), circle.radius * circle.radius, Epsilon))
      SomeSolution(point);
    else
      NoSolution;
  }
  
  def intersectionPointsBetween(circle:Circle, line:Line):Set[Solution[Point]] = {
    if(!line.vertical) {
      val a = circle.center.x;
      val b = circle.center.y;
      val r = circle.radius;
      val m = line.slope.get;
      val c = line.yIntercept.get;
      val A = m * m + 1;
      val B = 2 * (m * c - m * b - a);
      val C = c * c + a * a + b * b - 2 * b * c - r * r;
      val discriminant = (B * B) - 4 * A * C;
      if(discriminant > 0) {
        val solution1x = (-B + sqrt(discriminant)) / (2 * A);
        val solution2x = (-B - sqrt(discriminant)) / (2 * A);
        val solution1 = line.forX(solution1x).map(Point(solution1x, _));
        val solution2 = line.forX(solution2x).map(Point(solution2x, _));
        Set(solution1, solution2);
      }else if(discriminant == 0) {
        val solutionX = -B / (2 * A);
        val solution = line.forX(solutionX).map(Point(solutionX, _));
        Set(solution);
      }else Set.empty;
    }else {
      val solutions = circle forX line.p1.x;
      solutions.map(_.map(Point(line.p1.x, _)));
    }
  }
  
  def intersectionPointsBetween(circle:Circle, ray:Ray):Set[Solution[Point]] = {
    val circleLineIntersectionPoints = intersectionPointsBetween(circle, ray.line);
    circleLineIntersectionPoints.filter(_.map(ray intersects _).solutionOrElse(false));
  }
  
  def intersectionPointsBetween(circle:Circle, segment:Segment):Set[Solution[Point]] = {
    val segmentBounds = ShapeBounds(segment);
    val circleLineIntersectionPoints = intersectionPointsBetween(circle, segment.line);
    circleLineIntersectionPoints.filter(_.map(segmentBounds intersects _).solutionOrElse(false));
  }
  
  def intersectionPointsBetween(c1:Circle, c2:Circle):Set[Solution[Point]] = {
    val r1 = c1.radius;
    val r2 = c2.radius;
    val radii = r1 + r2;
    val θ = c1.center angleTo c2.center;
    val d = c1.center distanceTo c2.center;
    if(fuzzyEquals(d, radii, Epsilon)) {
      Set(SomeSolution(c1.center displacedBy Vector(r1, θ)));
    }else if(d < radii && d + r2 > r1 && d + r1 > r2) {
      val b1 = (r1 * r1 - r2 * r2 + d * d) / (2 * d);
      val a = sqrt(r1 * r1 - b1 * b1);
      val vectorToRadicalLine = Vector(b1, θ);
      val c1solution1 = vectorToRadicalLine + Vector(a, θ + Angle.right);
      val c1solution2 = vectorToRadicalLine + Vector(a, θ - Angle.right);
      Set(SomeSolution(c1.center displacedBy c1solution1), SomeSolution(c1.center displacedBy c1solution2));
    }else if(fuzzyEquals(d, 0, Epsilon) && fuzzyEquals(r1, r2, Epsilon)) {
      Set(InfiniteSolutions);
    }else Set.empty;
  }
  
  def intersectionPointBetween(polygon:Polygon, point:Point):Solution[Point] = {
    if(polygon.segments.exists(_ intersects point))
      SomeSolution(point);
    else
      NoSolution;
  }
  
  def intersectionPointsBetween(polygon:Polygon, line:Line):Set[Solution[Point]] = {
    polygon.segments.map(intersectionPointBetween(_, line)).toSet;
  }
  
  def intersectionPointsBetween(polygon:Polygon, ray:Ray):Set[Solution[Point]] = {
    polygon.segments.map(intersectionPointBetween(_, ray)).toSet;
  }
  
  def intersectionPointsBetween(polygon:Polygon, segment:Segment):Set[Solution[Point]] = {
    polygon.segments.map(intersectionPointBetween(_, segment)).toSet;
  }
  
  def intersectionPointsBetween(polygon:Polygon, circle:Circle):Set[Solution[Point]] = {
    polygon.segments.flatMap(intersectionPointsBetween(circle, _)).toSet;
  }
}

case class PointPerimeter(shape:Point) extends FinitePerimeter {
  lazy val length = Epsilon * 4;
}

case class LinePerimeter(shape:Line) extends Perimeter {
}

case class RayPerimeter(shape:Ray) extends Perimeter {
}

case class SegmentPerimeter(shape:Segment) extends FinitePerimeter {
  lazy val length = shape.p1 distanceTo shape.p2;
}

case class CirclePerimeter(shape:Circle) extends FinitePerimeter {
  lazy val length = 2 * PI * shape.radius;
}

case class PolygonPerimeter(shape:Polygon) extends FinitePerimeter {
  lazy val length = shape.segments.foldLeft(0d)(_ + _.perimeter.length);
}

case class CircleSweepPerimeter(shape:CircleSweep) extends FinitePerimeter {
  lazy val length = shape.circle.perimeter.length + shape.sweepVector.magnitude * 2;
}

case class RingPerimeter(shape:Ring) extends FinitePerimeter {
  lazy val length = shape.outerCircle.perimeter.length + shape.innerCircle.perimeter.length;
}