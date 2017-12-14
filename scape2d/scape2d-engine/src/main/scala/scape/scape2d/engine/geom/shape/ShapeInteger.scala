package scape.scape2d.engine.geom.shape

import scape.scape2d.engine.geom.Vector

object ShapeInteger {
  implicit def widenShape[S <: Shape](shapeInteger:ShapeInteger[S]):S = shapeInteger.shapeDouble;
}

sealed trait ShapeInteger[S <: Shape] {
  def shapeDouble:S;
}

case class PointInteger(x:Int, y:Int) extends ShapeInteger[Point] {
  lazy val shapeDouble = Point(x, y);
}

case class LineInteger(p1:PointInteger, p2:PointInteger) extends ShapeInteger[Line] {
  lazy val shapeDouble = Line(p1, p2);
}

case class RayInteger(origin:PointInteger, angle:Double) extends ShapeInteger[Ray] {
  lazy val shapeDouble = Ray(origin, angle);
}

case class SegmentInteger(p1:PointInteger, p2:PointInteger) extends ShapeInteger[Segment] {
  lazy val shapeDouble = Segment(p1, p2);
}

case class CircleInteger(center:PointInteger, radius:Double) extends ShapeInteger[Circle] {
  lazy val shapeDouble = Circle(center, radius);
}

case class AxisAlignedRectangleInteger(bottomLeft:PointInteger, width:Int, height:Int)
extends ShapeInteger[AxisAlignedRectangle] {
  lazy val shapeDouble = AxisAlignedRectangle(bottomLeft, width, height);
  lazy val topLeft = shapeDouble.topLeft.toInt;
  lazy val topRight = shapeDouble.topRight.toInt;
  lazy val bottomRight = shapeDouble.bottomRight.toInt;
}

case class PolygonInteger(segments:Array[SegmentInteger]) extends ShapeInteger[Polygon] {
  lazy val shapeDouble = CustomPolygon(segments.map(ShapeInteger.widenShape));
  lazy val points = shapeDouble.points.map(_.toInt);
}

case class CircleSweepInteger(circle:CircleInteger, sweepVector:Vector) extends ShapeInteger[CircleSweep] {
  lazy val shapeDouble = CircleSweep(circle, sweepVector);
  lazy val destinationCircle = shapeDouble.destinationCircle.toInt;
  lazy val connector = (shapeDouble.connector._1.toInt, shapeDouble.connector._2.toInt);
}