package scape.scape2d.engine.geom.shape

import java.lang.Math.PI

import scape.scape2d.engine.geom.Epsilon
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