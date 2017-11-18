package scape.scape2d.engine.geom.structure

import scala.collection.immutable.List

import scape.scape2d.engine.geom.shape.Point
import scape.scape2d.engine.geom.shape.Segment

case class HingedSegmentedStructure(suspension:Point, points:List[Point]) extends SegmentedStructure {
  val segments = points.map(Segment(suspension, _));
}