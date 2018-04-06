package scape.scape2d.debugger

import scape.scape2d.debugger.view.TreeNodesView
import scape.scape2d.engine.motion.collision.detection.TreePosterioriCollisionDetector

class TreePosterioriCollisionDetectorDebugger(val treeNodesView:TreeNodesView) {
  def trackNodes(collisionDetector:TreePosterioriCollisionDetector[_]) = {
    collisionDetector.onTreeCreation(treeNodesView.renderTree);
  }
}