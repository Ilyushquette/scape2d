package scape.scape2d.engine.geom

import java.lang.Math.PI
import java.lang.Math.abs

import com.google.common.math.DoubleMath.fuzzyCompare
import com.google.common.math.DoubleMath.fuzzyEquals
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.CircleSweep
import scape.scape2d.engine.geom.shape.FiniteShape
import scape.scape2d.engine.geom.shape.AxisAlignedRectangle
import scape.scape2d.engine.geom.shape.Ring
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Polygon
import scape.scape2d.engine.geom.shape.Segment

object SecondMomentOfArea {
  val pointSecondMomentOfArea = SecondMomentOfArea(SecondMomentOfAreaEpsilon);
  
  def apply(shape:FiniteShape):SecondMomentOfArea = shape match {
    case _:Point => pointSecondMomentOfArea;
    case segment:Segment =>
      SecondMomentOfArea(AxisAlignedRectangle(Point.origin, segment.perimeter.length, Epsilon));
    case Circle(_, r) =>
      SecondMomentOfArea((PI * r * r * r * r) / 2);
    case AxisAlignedRectangle(_, w, h) =>
      SecondMomentOfArea(((w * h) * (w * w + h * h)) / 12);
    case polygon:Polygon =>
      val displacementToOrigin = Point.origin - polygon.center;
      val polygonAtOrigin = polygon displacedBy displacementToOrigin;
      ofPolygon(polygonAtOrigin.segments, 0, 0);
    case CircleSweep(circle, sweepVector) =>
      val Ir = SecondMomentOfArea(AxisAlignedRectangle(Point.origin, sweepVector.magnitude, circle.diameter));
      val r = circle.radius;
      val Isc = SecondMomentOfArea(0.5025 * r * r * r * r);
      val dsc = sweepVector.magnitude / 2 + (4 * r) / (3 * PI);
      val Asc = (PI * r * r) / 2;
      val Idsc = SecondMomentOfArea(Isc.value + Asc * dsc * dsc);
      Idsc * 2 + Ir;
    case ring:Ring =>
      SecondMomentOfArea(ring.outerCircle) - SecondMomentOfArea(ring.innerCircle);
  }
  
  private[SecondMomentOfArea] def ofPolygon(segments:List[Segment], sumX:Double, sumY:Double):SecondMomentOfArea = segments match {
    case Segment(Point(x1, y1), Point(x2, y2))::Nil =>
      val a = x1 * y2 - x2 * y1;
      val finalSumX = sumX + (y1 * y1 + y1 * y2 + y2 * y2) * a;
      val finalSumY = sumY + (x1 * x1 + x1 * x2 + x2 * x2) * a;
      val Ix = finalSumX / 12;
      val Iy = finalSumY / 12;
      SecondMomentOfArea(abs(Ix + Iy));
    case Segment(Point(x1, y1), Point(x2, y2))::segments =>
      val a = x1 * y2 - x2 * y1;
      val updatedSumX = sumX + (y1 * y1 + y1 * y2 + y2 * y2) * a;
      val updatedSumY = sumY + (x1 * x1 + x1 * x2 + x2 * x2) * a;
      ofPolygon(segments, updatedSumX, updatedSumY);
    case Nil => throw new IllegalArgumentException("No segments - no second moment of area");
  }
}

case class SecondMomentOfArea(value:Double) extends Ordered[SecondMomentOfArea] {
  def +(secondMomentOfArea:SecondMomentOfArea) = SecondMomentOfArea(value + secondMomentOfArea.value);
  
  def -(secondMomentOfArea:SecondMomentOfArea) = SecondMomentOfArea(value - secondMomentOfArea.value);
  
  def *(multiplier:Double) = SecondMomentOfArea(value * multiplier);
  
  def /(divider:Double) = SecondMomentOfArea(value / divider);
  
  override def equals(any:Any) = any match {
    case SecondMomentOfArea(value) =>
      fuzzyEquals(this.value, value, SecondMomentOfAreaEpsilon);
    case _ => false;
  }
  
  def compare(secondMomentOfArea:SecondMomentOfArea) = {
    fuzzyCompare(this.value, secondMomentOfArea.value, SecondMomentOfAreaEpsilon);
  }
}