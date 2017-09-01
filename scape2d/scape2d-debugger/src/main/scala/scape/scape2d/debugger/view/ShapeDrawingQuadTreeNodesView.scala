package scape.scape2d.debugger.view

import scape.scape2d.engine.geom.partition.QuadTree
import scape.scape2d.graphics.CustomizedShape

class ShapeDrawingQuadTreeNodesView(val shapeDrawer:ShapeDrawer)
extends QuadTreeNodesView {
  val quadTreeColor = 0xFFFFFFFF; // WHITE
  
  def renderTree(quadTree:QuadTree[_]) = {
    shapeDrawer.draw(CustomizedShape(quadTree.bounds, quadTreeColor));
    quadTree.nodes.foreach(renderTree);
  }
}