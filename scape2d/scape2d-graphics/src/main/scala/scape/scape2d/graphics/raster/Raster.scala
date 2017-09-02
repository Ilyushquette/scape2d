package scape.scape2d.graphics.raster

import scape.scape2d.engine.geom.shape.AxisAlignedRectangleInteger
import scape.scape2d.engine.geom.shape.PointInteger

trait Raster {
  def bounds:AxisAlignedRectangleInteger;
  
  def getRgb(point:PointInteger):Int = getRgb(point.x, point.y);
  
  def getRgb(x:Int, y:Int):Int;
  
  def setRgb(point:PointInteger, rgb:Int):Unit = setRgb(point.x, point.y, rgb);
  
  def setRgb(x:Int, y:Int, rgb:Int):Unit;
  
  def createViewport(viewportBounds:AxisAlignedRectangleInteger) = new ViewportRaster(this, viewportBounds);
}