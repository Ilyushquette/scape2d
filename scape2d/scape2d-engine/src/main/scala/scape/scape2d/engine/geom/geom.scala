package scape.scape2d.engine

import java.lang.Math.cos
import java.lang.Math.PI
import java.lang.Math.sin

import com.google.common.math.DoubleMath.fuzzyEquals

import scape.scape2d.engine.geom.shape.Line
import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment

package object geom {
  val Epsilon = 1E-10;
  val AreaEpsilon = Epsilon * Epsilon;
  val TwicePI = PI * 2;
  
  def findMutualX(l1:Line, l2:Line) = {
    if(!l1.vertical && !l2.vertical && !fuzzyEquals(l1.slope.get, l2.slope.get, Epsilon)) {
      (l2.yIntercept.get - l1.yIntercept.get) / (l1.slope.get - l2.slope.get);
    }
    else if(!l1.vertical && l2.vertical) l2.p1.x;
    else if(l1.vertical && !l2.vertical) l1.p1.x;
    else throw new ArithmeticException("Mutual X for parallel lines has no or infinite solutions");
  }
  
  def findMutualPoint(l1:Line, l2:Line) = {
    val mutualX = findMutualX(l1, l2);
    val mutualY = if(!l1.vertical) l1.forX(mutualX) else l2.forX(mutualX);
    Point(mutualX, mutualY);
  }
  
  def fetchWaypoints(segmentsIterator:Iterator[Segment]) = {
    segmentsIterator.foldLeft(Set[Point]())((acc, cur) => acc + cur.p1 + cur.p2);
  }
  
  def centroidOf(segments:List[Segment], signedArea:Double = 0, centroid:Point = Point.origin):Point = segments match {
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