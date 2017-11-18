package scape.scape2d.engine.geom.structure

import scape.scape2d.engine.geom.shape.Segment

trait SegmentedStructure {
  def segments:List[Segment];
  
  def ++(segmentedStructure:SegmentedStructure) = new SegmentedStructure {
    val segments = SegmentedStructure.this.segments ++ segmentedStructure.segments;
  }
}