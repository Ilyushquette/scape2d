package scape.scape2d.debugger.view

import scape.scape2d.engine.geom.partition.Node

trait TreeNodesView {
  def renderTree(tree:Node[_]);
}