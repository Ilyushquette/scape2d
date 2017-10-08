package scape.scape2d.debugger.view.swing

import java.awt.Dimension
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt

import scape.scape2d.engine.geom.shape.AxisAlignedRectangleInteger
import scape.scape2d.engine.geom.shape.Point

class SwingBuffer(dimension:Dimension, alphaChannel:Boolean) {
  private[swing] val image = {
    val colorModel = if(alphaChannel) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB;
    new BufferedImage(dimension.width, dimension.height, colorModel);
  }
  
  private[swing] val raster = {
    val bounds = AxisAlignedRectangleInteger(Point.origin.toInt, dimension.width, dimension.height - 1);
    val dataBuffer = image.getRaster().getDataBuffer().asInstanceOf[DataBufferInt];
    new SwingDataBufferIntRaster(bounds, dataBuffer);
  }
}