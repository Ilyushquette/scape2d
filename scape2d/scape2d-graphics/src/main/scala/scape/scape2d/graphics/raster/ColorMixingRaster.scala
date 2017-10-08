package scape.scape2d.graphics.raster

class ColorMixingRaster(
  val raster:Raster,
  val mix:(Int, Int) => Int)
extends Raster {
  def bounds = raster.bounds;
  
  def setRgb(x:Int, y:Int, rgb:Int) = {
    val currentRgb = getRgb(x, y);
    val mixedRgb = mix(currentRgb, rgb);
    raster.setRgb(x, y, mixedRgb);
  }
  
  def getRgb(x:Int, y:Int) = raster.getRgb(x, y);
}