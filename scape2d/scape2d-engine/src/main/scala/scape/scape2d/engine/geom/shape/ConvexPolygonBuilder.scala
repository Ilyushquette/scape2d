package scape.scape2d.engine.geom.shape

import scape.scape2d.engine.geom.TripletOrientation
import scape.scape2d.engine.geom.Collinear

object ConvexPolygonBuilder {
  def apply(p1:Point, p2:Point, p3:Point):ConvexPolygonBuilder = {
    ConvexPolygonBuilder(List(Segment(p1, p2), Segment(p2, p3)), TripletOrientation(p1, p2, p3));
  }
}

case class ConvexPolygonBuilder private[shape] (
  segments:List[Segment],
  convergenceOrientation:TripletOrientation
) {
  def to(next:Point) = {
    val lastSegment = segments.last;
    val p1 = lastSegment.p1;
    val p2 = lastSegment.p2;
    val nextOrientation = TripletOrientation(p1, p2, next);
    if(convergenceOrientation == Collinear)
      ConvexPolygonBuilder(segments :+ Segment(p2, next), nextOrientation);
    else if(nextOrientation == Collinear || nextOrientation == convergenceOrientation)
      copy(segments :+ Segment(p2, next));
    else throw new IllegalArgumentException(s"Triplet of $p1, $p2 and $next form cave");
  }
  
  def build() = {
    val closedConvexPolygonBuilder = closed(segments);
    ConvexPolygon(CustomPolygon(closedConvexPolygonBuilder.segments));
  }
  
  private def closed(segments:List[Segment]) = {
    val firstPoint = segments.head.p1;
    val lastPoint = segments.last.p2;
    if(firstPoint != lastPoint) to(firstPoint);
    else this;
  }
}