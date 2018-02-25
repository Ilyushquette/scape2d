package scape.scape2d.debugger.view

import scape.scape2d.graphics.CustomizedShape
import scape.scape2d.engine.geom.partition.Node

class ShapeDrawingTreeNodesView(val shapeDrawer:ShapeDrawer)
extends TreeNodesView {
  val treeColor = 0xFFFFFFFF; // WHITE
  
  def renderTree(tree:Node[_]) = {
    shapeDrawer.draw(CustomizedShape(tree.bounds, treeColor));
    tree.nodes.foreach(renderTree);
  }
}