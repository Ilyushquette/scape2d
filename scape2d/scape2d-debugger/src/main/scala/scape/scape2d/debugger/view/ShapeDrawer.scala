package scape.scape2d.debugger.view

import scape.scape2d.engine.geom.shape.Shape
import scape.scape2d.graphics.CustomizedShape

trait ShapeDrawer {
  def draw(shape:CustomizedShape[_ <: Shape]);
  
  def clear(shape:Shape) = draw(CustomizedShape(shape, 0x00000000, false));
}