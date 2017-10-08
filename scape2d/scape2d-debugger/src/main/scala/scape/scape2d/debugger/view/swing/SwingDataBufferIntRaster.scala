package scape.scape2d.debugger.view.swing

import java.awt.image.DataBufferInt
import scape.scape2d.engine.geom.shape.AxisAlignedRectangleInteger
import scape.scape2d.graphics.raster.Raster
import scape.scape2d.graphics.raster.OutOfRasterBoundsException

class SwingDataBufferIntRaster(
  val bounds:AxisAlignedRectangleInteger,
  val dataBuffer:DataBufferInt) extends Raster {
  private val pixels = dataBuffer.getData();
  
  def setRgb(x:Int, y:Int, rgb:Int) = pixels(indexOf(x, y)) = rgb;
  
  def getRgb(x:Int, y:Int) = pixels(indexOf(x, y));
  
  private def indexOf(x:Int, y:Int) = {
    val index = y * bounds.width + x;
    // index of the latest element in data buffer's array is width * height - 1
    if(index == pixels.length) index - 1;
    else index;
  }
}