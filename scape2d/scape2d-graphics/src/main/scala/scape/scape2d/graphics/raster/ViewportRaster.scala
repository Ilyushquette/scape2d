package scape.scape2d.graphics.raster

import scape.scape2d.engine.geom.shape.AxisAlignedRectangleInteger
import scape.scape2d.engine.geom.shape.Point

class ViewportRaster(val parentRaster:Raster, val bounds:AxisAlignedRectangleInteger) extends Raster {
  if(!parentRaster.bounds.contains(bounds))
    throw new IllegalArgumentException(parentRaster.bounds + " doesn't contain " + bounds);
  
  def setRgb(x:Int, y:Int, rgb:Int) = {
    checkBounds(x, y);
    parentRaster.setRgb(x, y, rgb);
  }
  
  def getRgb(x:Int, y:Int) = {
    checkBounds(x, y);
    parentRaster.getRgb(x, y);
  }
  
  private def checkBounds(x:Int, y:Int) = {
    val point = Point(x, y);
    if(!bounds.contains(point))
      throw new OutOfRasterBoundsException("Viewport bounds %s doesn't contain %s".format(bounds, point));
  }
}