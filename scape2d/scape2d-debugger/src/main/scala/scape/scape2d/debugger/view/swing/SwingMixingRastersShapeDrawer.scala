package scape.scape2d.debugger.view.swing

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D

import javax.swing.JPanel
import scape.scape2d.debugger.view.ShapeDrawer
import scape.scape2d.engine.geom.shape.PointInteger
import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.engine.geom.shape.ShapeInteger.widenShape
import scape.scape2d.graphics.CustomizedShape
import scape.scape2d.graphics.CustomizedShape.toShape
import scape.scape2d.graphics.raster.ColorMixingRaster
import scape.scape2d.graphics.raster.Raster
import scape.scape2d.graphics.rasterizer.Rasterizer
import scape.scape2d.graphics.rgb.RgbColorModel

class SwingMixingRastersShapeDrawer(
  val buffer:SwingBuffer,
  val rasterizer:Rasterizer[Shape],
  val mixColors:(Int, Int) => Int = RgbColorModel.additive)
extends JPanel with ShapeDrawer {
  def draw(customizedShape:CustomizedShape[_ <: Shape]) = {
    val bufferedRaster = prepareTargetRaster(customizedShape.mixColors);
    val points = rasterizer.rasterize(customizedShape);
    points.foreach(writeToRaster(bufferedRaster, _, customizedShape.rgb));
    repaint();
  }
  
  override def getPreferredSize = new Dimension(buffer.raster.bounds.width, buffer.raster.bounds.height);
  
  override def paint(graphics:Graphics) = {
    val graphics2d = graphics.asInstanceOf[Graphics2D];
    graphics2d.translate(0, buffer.raster.bounds.height);
    graphics2d.scale(1, -1);
    graphics2d.drawImage(buffer.image, 0, 0, null);
  }
  
  private def prepareTargetRaster(mix:Boolean) = {
    if(mix) new ColorMixingRaster(buffer.raster, mixColors);
    else buffer.raster;
  }
  
  private def writeToRaster(raster:Raster, point:PointInteger, rgb:Int) = {
    if(raster.bounds.contains(point)) raster.setRgb(point, rgb);
  }
}