package scape.scape2d.debugger

import scape.scape2d.debugger.view.TreeNodesView
import scape.scape2d.engine.motion.collision.detection.TreeCollisionDetector

class TreeCollisionDetectorDebugger(val treeNodesView:TreeNodesView) {
  def trackNodes(collisionDetector:TreeCollisionDetector[_]) = {
    collisionDetector.onTreeCreation(treeNodesView.renderTree);
  }
}