package scape.scape2d.graphics.rasterizer.cache

import scape.scape2d.engine.geom.shape.Segment

private[cache] class SegmentCacheKey(segment:Segment) {
  val truncatedLength = Math.round(segment.length * 10000000000d) / 10000000000d;
  val truncatedSlope = segment.line.slope.map(slope => Math.round(slope * 1000d) / 1000d);
  
  override def hashCode = truncatedSlope.hashCode + truncatedLength.hashCode;
    
  override def equals(any:Any) = {
    val other = any.asInstanceOf[SegmentCacheKey];
    truncatedSlope == other.truncatedSlope && truncatedLength == other.truncatedLength;
  }
}