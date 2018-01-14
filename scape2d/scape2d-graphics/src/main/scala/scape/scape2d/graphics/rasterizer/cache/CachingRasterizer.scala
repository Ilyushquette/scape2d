package scape.scape2d.graphics.rasterizer.cache

import scala.collection.mutable.HashMap

import scape.scape2d.engine.geom.Components
import scape.scape2d.engine.geom.shape.PointInteger
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.shape.ShapeBounds
import scape.scape2d.engine.geom.shape.ShapeInteger.widenShape
import scape.scape2d.graphics.rasterizer.Rasterizer

class CachingRasterizer[S <: Shape](
  val rasterizer:Rasterizer[S],
  val keygen:S => Object) extends Rasterizer[S] {
  private val cache = HashMap[Object, CacheValue]();
  
  def rasterize(shape:S) = {
    val cacheKey = keygen(shape);
    val cacheValue = cache.get(cacheKey);
    if(cacheValue.isDefined) copyFromCache(cacheValue.get, shape);
    else evaluateAndCache(cacheKey, shape);
  }
  
  private def copyFromCache(cacheValue:CacheValue, currentShape:S) = {
    val currentBounds = ShapeBounds(currentShape);
    val offset = Components(currentBounds.bottomLeft.x - cacheValue.bounds.bottomLeft.x,
                              currentBounds.bottomLeft.y - cacheValue.bounds.bottomLeft.y);
    cacheValue.points.map(_.+(offset).toInt);
  }
  
  private def evaluateAndCache(cacheKey:Object, shape:S) = {
    val points = rasterizer.rasterize(shape);
    cache.put(cacheKey, CacheValue(shape, points));
    points;
  }
  
  private case class CacheValue(shape:S, points:Iterable[PointInteger]) {
    lazy val bounds = ShapeBounds(shape).toInt;
  }
}