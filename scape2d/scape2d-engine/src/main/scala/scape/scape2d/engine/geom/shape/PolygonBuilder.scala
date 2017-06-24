package scape.scape2d.engine.geom.shape

object PolygonBuilder {
  def apply(p1:Point, p2:Point, p3:Point):PolygonBuilder = {
    PolygonBuilder(Array(Segment(p1, p2), Segment(p2, p3)));
  }
}

case class PolygonBuilder private[shape] (segments:Array[Segment]) {
  def to(next:Point) = copy(segments = segments :+ Segment(segments.last.p2, next));
  
  def build = {
    val firstSegment = segments(0);
    val lastSegment = segments(segments.size - 1);
    if(firstSegment.p1 == lastSegment.p2) CustomPolygon(segments);
    else CustomPolygon(segments :+ Segment(lastSegment.p2, firstSegment.p1));
  }
}