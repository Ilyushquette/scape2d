package scape.scape2d.graphics.rasterizer.cache

import scape.scape2d.graphics.rasterizer.Rasterizer
import scape.scape2d.engine.geom.shape.Circle
import scape.scape2d.engine.geom.shape.Segment

object CachingRasterizers {
  def enhanceCircleRasterizer(circleRasterizer:Rasterizer[Circle]) = {
    new CachingRasterizer[Circle](circleRasterizer, new CircleCacheKey(_));
  }
  
  def enhanceSegmentRasterizer(segmentRasterizer:Rasterizer[Segment]) = {
    new CachingRasterizer[Segment](segmentRasterizer, new SegmentCacheKey(_));
  }
}