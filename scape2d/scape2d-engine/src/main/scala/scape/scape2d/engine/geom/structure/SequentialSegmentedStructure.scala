package scape.scape2d.engine.geom.structure

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment

object SequentialSegmentedStructure {
  def closed(points:List[Point]) = {
    if(points.size > 2 && points.head != points.last)
      new SequentialSegmentedStructure(points :+ points.head);
    else new SequentialSegmentedStructure(points);
  }
}

case class SequentialSegmentedStructure(points:List[Point])
extends SegmentedStructure {
  val segments = makeSequentialStructure(points);
  
  private def makeSequentialStructure(points:List[Point], segments:List[Segment] = List()):List[Segment] = {
    points match {
      case p1::p2::Nil => segments :+ Segment(p1, p2);
      case p1::p2::ps => makeSequentialStructure(p2 :: ps, segments :+ Segment(p1, p2));
      case List(p) => throw new IllegalArgumentException("Couldn't create structure from single point");
      case Nil => segments;
    }
  }
}