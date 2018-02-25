package scape.scape2d.debugger

import scape.scape2d.debugger.view.TreeNodesView
import scape.scape2d.engine.motion.collision.detection.linear.TreeLinearMotionCollisionDetector

class TreeLinearMotionCollisionDetectorDebugger(val treeNodesView:TreeNodesView) {
  def trackNodes(collisionDetector:TreeLinearMotionCollisionDetector[_]) = {
    collisionDetector.onTreeCreation(treeNodesView.renderTree);
  }
}