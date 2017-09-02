package scape.scape2d.graphics.raster

import scape.scape2d.engine.geom.shape.AxisAlignedRectangleInteger

class BufferedRaster(boundsInteger:AxisAlignedRectangleInteger) extends Raster {
  lazy val bounds = boundsInteger.copy(
      width = boundsInteger.width + 1,
      height = boundsInteger.height + 1
  );
  private lazy val pixels = Array.fill(bounds.height, bounds.width)(0x00000000);
  
  def setRgb(x:Int, y:Int, rgb:Int) = pixels(y - bounds.bottomLeft.y)(x - bounds.bottomLeft.x) = rgb;
  
  def getRgb(x:Int, y:Int) = pixels(y - bounds.bottomLeft.y)(x - bounds.bottomLeft.x);
}