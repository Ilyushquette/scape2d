package scape.scape2d.debugger.view

import scape.scape2d.engine.geom.partition.QuadTree

class ShapeDrawingQuadTreeNodesView(val shapeDrawer:ShapeDrawer) extends QuadTreeNodesView {
  val quadTreeColor = 0xffffff;
  
  def renderTree(quadTree:QuadTree[_]) = {
    shapeDrawer.clear(quadTree.bounds);
    shapeDrawer.draw(CustomizedShape(quadTree.bounds, quadTreeColor, false));
    quadTree.nodes.foreach(renderTree);
  }
}