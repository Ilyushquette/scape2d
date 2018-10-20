package scape.scape2d.engine.geom.shape

import java.lang.Math.PI

import scape.scape2d.engine.geom.Epsilon

sealed trait Perimeter {
  def shape:Shape;
}

sealed trait FinitePerimeter extends Perimeter {
  def shape:FiniteShape;
  
  def length:Double;
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