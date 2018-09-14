package scape.scape2d.engine

import java.lang.Math.PI

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment

package object geom {
  val Epsilon = 1E-10;
  val AreaEpsilon = Epsilon * Epsilon;
  val SecondMomentOfAreaEpsilon = Epsilon * Epsilon * Epsilon * Epsilon;
  val TwicePI = PI * 2;
  
  def fetchWaypoints(segmentsIterator:Iterator[Segment]) = {
    segmentsIterator.foldLeft(Set[Point]())((acc, cur) => acc + cur.p1 + cur.p2);
  }
}