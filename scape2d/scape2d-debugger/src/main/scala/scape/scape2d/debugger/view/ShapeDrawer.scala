package scape.scape2d.debugger.view

trait ShapeDrawer {
  def draw(shape:Shape);
  def clearAndDraw(clearable:Shape, drawable:Shape);
  def clear(shape:Shape);
}