package scape.scape2d.debugger.view

import scape.scape2d.engine.geom.partition.QuadTree

trait QuadTreeNodesView {
  def renderTree(quadTree:QuadTree[_]);
}