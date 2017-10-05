package scape.scape2d.graphics.rasterizer.cache

import scape.scape2d.engine.geom.shape.Segment

private[cache] class SegmentCacheKey(segment:Segment) {
  val truncatedLength = Math.round(segment.length * 10000000000d) / 10000000000d;
  val truncatedSlope = {
    if(segment.line.slope.isDefined)
      Some(Math.round(segment.line.slope.get * 10000000000d) / 10000000000d);
    else
      None;
  }
  
  override def hashCode = truncatedSlope.hashCode + truncatedLength.hashCode;
    
  override def equals(any:Any) = {
    val other = any.asInstanceOf[SegmentCacheKey];
    truncatedSlope == other.truncatedSlope && truncatedLength == other.truncatedLength;
  }
}