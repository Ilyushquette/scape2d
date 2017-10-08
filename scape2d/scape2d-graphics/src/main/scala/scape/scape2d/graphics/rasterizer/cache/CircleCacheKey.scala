package scape.scape2d.graphics.rasterizer.cache

import scape.scape2d.engine.geom.shape.Circle

private[cache] class CircleCacheKey(circle:Circle) {
  val truncatedRadius = Math.round(circle.radius * 10000000000d) / 10000000000d;
  
  override def hashCode = truncatedRadius.hashCode;
    
  override def equals(any:Any) = {
    val other = any.asInstanceOf[CircleCacheKey];
    truncatedRadius == other.truncatedRadius;
  }
}