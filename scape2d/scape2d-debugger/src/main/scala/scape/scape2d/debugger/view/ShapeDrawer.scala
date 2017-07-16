package scape.scape2d.debugger.view

import scape.scape2d.engine.geom.shape.Shape

trait ShapeDrawer {
  def draw(shape:CustomizedShape);
  def clear(shape:Shape, fill:Boolean);
}