package scape.scape2d.engine.geom.shape

import java.lang.Math.PI
import java.lang.Math.pow

import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.geom.Epsilon
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